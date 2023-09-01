package com.eshop.order.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemsDto> orderLineItemsDtoList
) {
}
