package com.eshop.inventory.repository;

import com.eshop.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // return all Inventory objects which are match these skuCodes
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
