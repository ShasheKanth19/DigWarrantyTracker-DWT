package com.example.demo.service;

import com.example.demo.model.ProductPurchase;
import com.example.demo.repository.ProductPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductPurchaseService {

    @Autowired
    private ProductPurchaseRepository repository;

    public List<ProductPurchase> getPurchasesByUser(int userId) {
        return repository.findByUserId(userId);
    }

    public ProductPurchase addPurchase(ProductPurchase purchase) {
        if (purchase.getWarrantyExpiryDate() == null && purchase.getPurchaseDate() != null) {
            purchase.setWarrantyExpiryDate(purchase.getPurchaseDate().plusMonths(purchase.getWarrantyMonths()));
        }
        return repository.save(purchase);
    }

    public List<ProductPurchase> getAllPurchases() {
        return repository.findAll();
    }

    public List<ProductPurchase> getPurchasesExpiringOnDates(List<java.time.LocalDate> dates) {
        return repository.findByWarrantyExpiryDateIn(dates);
    }
}
