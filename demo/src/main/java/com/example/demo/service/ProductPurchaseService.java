package com.example.demo.service;

import com.example.demo.model.ProductPurchase;
import com.example.demo.repository.ProductPurchaseRepository;
import com.example.demo.ml.MLPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductPurchaseService {

    @Autowired
    private ProductPurchaseRepository repository;

    @Autowired
    private MLPredictionService mlPredictionService;

    public List<ProductPurchase> getPurchasesByUser(int userId) {
        return repository.findByUserId(userId);
    }

    public ProductPurchase addPurchase(ProductPurchase purchase) {
        if (purchase.getWarrantyExpiryDate() == null && purchase.getPurchaseDate() != null) {
            purchase.setWarrantyExpiryDate(purchase.getPurchaseDate().plusMonths(purchase.getWarrantyMonths()));
        }
        ProductPurchase savedPurchase = repository.save(purchase);
        
        // ML Enhancement: Trigger prediction generation seamlessly inside product lifecycle
        try {
            mlPredictionService.generateAndSavePrediction(savedPurchase);
        } catch (Exception e) {
            System.err.println("Failed to generate ML prediction: " + e.getMessage());
        }
        
        return savedPurchase;
    }

    public List<ProductPurchase> getAllPurchases() {
        return repository.findAll();
    }

    public List<ProductPurchase> getPurchasesExpiringOnDates(List<java.time.LocalDate> dates) {
        return repository.findByWarrantyExpiryDateIn(dates);
    }
}
