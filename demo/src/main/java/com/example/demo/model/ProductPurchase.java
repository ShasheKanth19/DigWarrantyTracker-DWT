package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class ProductPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productId;
    private LocalDate purchaseDate;

    private String name;
    private String category;
    private String serialNumber;
    private double price;
    private int warrantyMonths;
    private LocalDate warrantyExpiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("purchases")
    private User user;

    public ProductPurchase() {
    }

    public ProductPurchase(String productId, String name, String category, String serialNumber, double price,
            int warrantyMonths, LocalDate purchaseDate, User user) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.serialNumber = serialNumber;
        this.price = price;
        this.warrantyMonths = warrantyMonths;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.warrantyExpiryDate = purchaseDate != null ? purchaseDate.plusMonths(warrantyMonths) : null;
    }

    // ✅ Getters
    public int getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public double getPrice() {
        return price;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public User getUser() {
        return user;
    }

    // ✅ Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
        if (this.purchaseDate != null) {
            this.warrantyExpiryDate = this.purchaseDate.plusMonths(warrantyMonths);
        }
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        if (this.purchaseDate != null) {
            this.warrantyExpiryDate = this.purchaseDate.plusMonths(this.warrantyMonths);
        }
    }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
