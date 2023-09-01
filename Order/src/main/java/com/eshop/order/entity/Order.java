package com.eshop.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String orderNumber;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    List<OrderLineItems> orderLineItemsList;
}
