package com.eshop.inventory.repository;

import com.eshop.inventory.entity.Inventory;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:test_local.properties")
class InventoryRepositoryTest {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_findBySkuCodeIn(){
        List<Inventory> inventories = getInventories();
        inventories.forEach(inventory -> testEntityManager.persist(inventory));
        testEntityManager.flush();

        List<String> skuCodes = Arrays.asList("SKU1", "SKU2");
        List<Inventory> foundInventories = inventoryRepository.findBySkuCodeIn(skuCodes);

        assertEquals(inventories.size(), foundInventories.size());

    }

    private List<Inventory> getInventories(){
        Inventory inventory1 = new Inventory(1L,"SKU1",5);
        Inventory inventory2 = new Inventory(2L,"SKU2",10);
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);
        return inventories;
    }

}