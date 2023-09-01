package com.eshop.inventory.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryResponse {
    String skuCode;
    boolean isInStock;
}
