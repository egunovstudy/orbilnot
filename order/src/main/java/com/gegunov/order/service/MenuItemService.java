package com.gegunov.order.service;

import com.gegunov.order.jpa.model.Ingredient;
import com.gegunov.order.jpa.model.MenuItem;
import com.gegunov.order.jpa.model.Product;
import com.gegunov.order.jpa.model.ProductStock;
import com.gegunov.order.jpa.repository.IngredientRepository;
import com.gegunov.order.jpa.repository.MenuItemRepository;
import com.gegunov.order.jpa.repository.ProductRepository;
import com.gegunov.order.jpa.repository.ProductStockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private static final List<ProductStock.ProductStockStatus> NEGATIVE_STATUSES = List.of(
            ProductStock.ProductStockStatus.COOKED, ProductStock.ProductStockStatus.RESERVED,
            ProductStock.ProductStockStatus.WITHDRAWN);

    private final IngredientRepository ingredientRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public UUID save(MenuItem menuItem) {
        MenuItem existing = menuItemRepository.findByCode(menuItem.getCode());
        if (existing != null) {
            throw new RuntimeException("MenuItem with code " + menuItem.getCode() + " already exists");
        }
        menuItem.setId(UUID.randomUUID());
        MenuItem saved = menuItemRepository.save(menuItem);
        menuItem.getIngredients().forEach(ingredient -> {
            ingredient.setProduct(productRepository.findByCode(ingredient.getProduct().getCode()));
            ingredient.setId(UUID.randomUUID());
            ingredient.setMenuItem(saved);
            ;
            ingredientRepository.save(ingredient);
        });
        return saved.getId();
    }

    public Iterable<MenuItem> findAll() {
        Iterable<MenuItem> menuItems = menuItemRepository.findAll();

        for (MenuItem menuItem : menuItems) {
            int minProductQuantity = Integer.MAX_VALUE;
            for (Ingredient ingredient : menuItem.getIngredients()) {
                Product product = ingredient.getProduct();
                int availableQty = productStockRepository.getSumQuantityByProductAndStatus(product,
                        ProductStock.ProductStockStatus.AVAILABLE);
                int notAvailableQty = productStockRepository.getSumQuantityByProductAndStatusIn(product,
                        NEGATIVE_STATUSES);
                int requiredProduct = ingredient.getRequiredAmount();
                minProductQuantity = min(minProductQuantity, (availableQty - notAvailableQty) / requiredProduct);
            }
            menuItem.setAvailableQuantity(max(minProductQuantity, 0));
        }
        return menuItems;
    }

}
