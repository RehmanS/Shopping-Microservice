package com.eshop.order.service;

import com.eshop.order.dto.InventoryResponse;
import com.eshop.order.dto.OrderLineItemsDto;
import com.eshop.order.dto.OrderRequest;
import com.eshop.order.entity.Order;
import com.eshop.order.entity.OrderLineItems;
import com.eshop.order.event.OrderPlacedEvent;
import com.eshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {

        // create a new Order object and set orderNumber
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // get items from request, transfer and set order object
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        // get all skuCodes from request
        List<String> skuCode = orderRequest.orderLineItemsDtoList()
                .stream()
                .map(OrderLineItemsDto::skuCode)
                .toList();

        // call InventoryService and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/v1/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCode).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        // return true if all items are in stock
        boolean allProductIsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        // save to database if all items are in stock
        if (allProductIsInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("NOTIFICATION_TOPIC", getEvent(order.getOrderNumber()));
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    // mapper
    private OrderLineItems mapToDto(OrderLineItemsDto dto) {
        return OrderLineItems.builder()
                .id(dto.id())
                .skuCode(dto.skuCode())
                .price(dto.price())
                .quantity(dto.quantity())
                .build();
    }

    private OrderPlacedEvent getEvent(String orderNumber) {
        return OrderPlacedEvent.builder()
                .orderNumber(orderNumber)
                .to("userMail")
                .subject("Order")
                .text("Your order has been successfully received: ")
                .build();
    }
}