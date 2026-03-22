package com.example.demo.controller;

import com.example.demo.model.ProductPurchase;
import com.example.demo.service.ProductPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductPurchaseController {

    @Autowired
    private ProductPurchaseService service;

    @PostMapping
    public ProductPurchase addPurchase(@RequestBody ProductPurchase purchase) {
        return service.addPurchase(purchase);
    }

    @GetMapping("/user/{userId}")
    public List<ProductPurchase> getPurchasesByUser(@PathVariable int userId) {
        return service.getPurchasesByUser(userId);
    }

    @GetMapping
    public List<ProductPurchase> getAllPurchases() {
        return service.getAllPurchases();
    }
    @PutMapping("/{id}")
public ProductPurchase updatePurchase(@PathVariable int id, @RequestBody ProductPurchase updatedPurchase) {
    updatedPurchase.setId(id);
    return service.addPurchase(updatedPurchase);
}
}
