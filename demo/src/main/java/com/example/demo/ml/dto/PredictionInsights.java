package com.example.demo.ml.dto;

import java.util.List;

public class PredictionInsights {
    private String prediction;
    private double renewalProbability;
    private double confidenceScore;
    private String riskLevel;
    private String recommendation;
    private List<String> topFactors;

    public PredictionInsights() {}

    public PredictionInsights(String prediction, double renewalProbability, double confidenceScore, 
                              String riskLevel, String recommendation, List<String> topFactors) {
        this.prediction = prediction;
        this.renewalProbability = renewalProbability;
        this.confidenceScore = confidenceScore;
        this.riskLevel = riskLevel;
        this.recommendation = recommendation;
        this.topFactors = topFactors;
    }

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
    public List<String> getTopFactors() { return topFactors; }
    public void setTopFactors(List<String> topFactors) { this.topFactors = topFactors; }
}
