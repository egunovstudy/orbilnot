package com.gegunov.foodstuff.web;

import com.gegunov.foodstuff.jpa.model.Product;
import com.gegunov.foodstuff.service.FoodstuffService;
import com.gegunov.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FoodstuffController {

    private final FoodstuffService foodstuffService;

    @GetMapping
    public Iterable<Product> getAllProducts() {
        return foodstuffService.getAllProducts();
    }

    @PostMapping("/")
    public void createProduct(@RequestBody Product product) {
        foodstuffService.createOrUpdateProduct(product);
    }

    @PutMapping("/")
    public void changeProductStock(@RequestBody ProductDTO stockEventDTO) {
        foodstuffService.changeProductQuantity(stockEventDTO);
    }
}
