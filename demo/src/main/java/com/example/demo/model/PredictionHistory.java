package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "prediction_history")
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties("predictionHistory")
    private ProductPurchase product;

    private String prediction;
    private double renewalProbability;
    private double confidenceScore;
    private String riskLevel;
    
    @Column(columnDefinition = "TEXT")
    private String recommendation;

    // Storing as JSON string or comma-separated for simplicity
    @Column(columnDefinition = "TEXT")
    private String topFactors;

    private LocalDateTime createdAt;

    public PredictionHistory() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductPurchase getProduct() { return product; }
    public void setProduct(ProductPurchase product) { this.product = product; }
    public String getPrediction() { return prediction; }
    public void setPrediction(String prediction) { this.prediction = prediction; }
    public double getRenewalProbability() { return renewalProbability; }
    public void setRenewalProbability(double renewalProbability) { this.renewalProbability = renewalProbability; }
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public String getTopFactors() { return topFactors; }
    public void setTopFactors(String topFactors) { this.topFactors = topFactors; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
