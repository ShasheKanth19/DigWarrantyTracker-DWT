package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "renewal_training")
public class RenewalTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int customerAge;
    private int productAge;
    private double productCost;
    private int warrantyDuration;
    private int previousRenewals;
    private int satisfactionScore;
    private String category;
    
    // 1 for renewed, 0 for not renewed
    private int renewed;

    public RenewalTraining() {}

    public RenewalTraining(int customerAge, int productAge, double productCost, int warrantyDuration, 
                           int previousRenewals, int satisfactionScore, String category, int renewed) {
        this.customerAge = customerAge;
        this.productAge = productAge;
        this.productCost = productCost;
        this.warrantyDuration = warrantyDuration;
        this.previousRenewals = previousRenewals;
        this.satisfactionScore = satisfactionScore;
        this.category = category;
        this.renewed = renewed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getCustomerAge() { return customerAge; }
    public void setCustomerAge(int customerAge) { this.customerAge = customerAge; }
    public int getProductAge() { return productAge; }
    public void setProductAge(int productAge) { this.productAge = productAge; }
    public double getProductCost() { return productCost; }
    public void setProductCost(double productCost) { this.productCost = productCost; }
    public int getWarrantyDuration() { return warrantyDuration; }
    public void setWarrantyDuration(int warrantyDuration) { this.warrantyDuration = warrantyDuration; }
    public int getPreviousRenewals() { return previousRenewals; }
    public void setPreviousRenewals(int previousRenewals) { this.previousRenewals = previousRenewals; }
    public int getSatisfactionScore() { return satisfactionScore; }
    public void setSatisfactionScore(int satisfactionScore) { this.satisfactionScore = satisfactionScore; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getRenewed() { return renewed; }
    public void setRenewed(int renewed) { this.renewed = renewed; }
}
