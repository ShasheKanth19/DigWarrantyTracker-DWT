package com.example.demo.ml;

import com.example.demo.ml.dto.PredictionInsights;
import com.example.demo.model.PredictionHistory;
import com.example.demo.model.ProductPurchase;
import com.example.demo.model.RenewalTraining;
import com.example.demo.repository.PredictionHistoryRepository;
import com.example.demo.repository.RenewalTrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MLPredictionService {

    @Autowired
    private RenewalTrainingRepository trainingRepository;

    @Autowired
    private PredictionHistoryRepository historyRepository;

    @Autowired
    private DataPreprocessingService preprocessingService;

    private static final int K_NEIGHBORS = 5;

    public PredictionHistory generateAndSavePrediction(ProductPurchase product) {
        // Prepare mock or actual data for prediction
        int customerAge = 35; // default since we don't track user age
        int productAge = product.getPurchaseDate() != null ? 
            (int) ChronoUnit.YEARS.between(product.getPurchaseDate(), LocalDate.now()) : 0;
        double productCost = product.getPrice();
        int warrantyDuration = product.getWarrantyMonths();
        int previousRenewals = 0; // new product, no previous renewals
        int satisfactionScore = 8; // optimistic default for a new product
        String category = product.getCategory() != null ? product.getCategory().toUpperCase() : "ELECTRONICS";

        // Generate prediction using KNN
        PredictionInsights insights = predictRenewal(customerAge, productAge, productCost, warrantyDuration, previousRenewals, satisfactionScore, category);

        // Fetch existing prediction or create new
        PredictionHistory history = historyRepository.findByProduct_Id(product.getId()).orElse(new PredictionHistory());
        
        history.setProduct(product);
        history.setPrediction(insights.getPrediction());
        history.setRenewalProbability(insights.getRenewalProbability());
        history.setConfidenceScore(insights.getConfidenceScore());
        history.setRiskLevel(insights.getRiskLevel());
        history.setRecommendation(insights.getRecommendation());
        history.setTopFactors(String.join(";", insights.getTopFactors()));
        
        return historyRepository.save(history);
    }

    private PredictionInsights predictRenewal(int customerAge, int productAge, double productCost, 
                                              int warrantyDuration, int previousRenewals, int satisfactionScore, String category) {
        
        List<RenewalTraining> trainingData = trainingRepository.findAll();
        if (trainingData.isEmpty()) {
            return fallbackPrediction();
        }
        
        // Analyze and preprocess bounds
        preprocessingService.analyzeAndPreprocess(trainingData);

        // Feature encoding for query
        double[] queryFeatures = preprocessingService.normalize(customerAge, productAge, productCost, warrantyDuration, previousRenewals, satisfactionScore, encodeCategory(category));

        // Find K-nearest neighbors
        List<Neighbor> neighbors = new ArrayList<>();
        for (RenewalTraining row : trainingData) {
            double[] rowFeatures = preprocessingService.normalize(row.getCustomerAge(), row.getProductAge(), row.getProductCost(), 
                                                     row.getWarrantyDuration(), row.getPreviousRenewals(), 
                                                     row.getSatisfactionScore(), encodeCategory(row.getCategory()));
            double distance = calculateEuclideanDistance(queryFeatures, rowFeatures);
            neighbors.add(new Neighbor(distance, row.getRenewed(), row));
        }

        neighbors.sort(Comparator.comparingDouble(Neighbor::getDistance));
        List<Neighbor> kNearest = neighbors.subList(0, Math.min(K_NEIGHBORS, neighbors.size()));

        int renewCount = 0;
        List<RenewalTraining> nearestRecords = new ArrayList<>();
        for (Neighbor n : kNearest) {
            if (n.getLabel() == 1) renewCount++;
            nearestRecords.add(n.getRecord());
        }

        boolean willRenew = renewCount > K_NEIGHBORS / 2;
        double renewalProbability = (double) renewCount / K_NEIGHBORS * 100;
        double confidence = Math.max(renewCount, K_NEIGHBORS - renewCount) * 100.0 / K_NEIGHBORS;

        String prediction = willRenew ? "Likely To Renew" : "Not Likely To Renew";
        String riskLevel = determineRiskLevel(willRenew, confidence);
        String recommendation = generateRecommendation(willRenew, confidence);
        List<String> topFactors = generateDynamicFactors(nearestRecords, willRenew, customerAge, productAge, productCost, previousRenewals, satisfactionScore);

        return new PredictionInsights(prediction, renewalProbability, confidence, riskLevel, recommendation, topFactors);
    }



    private double encodeCategory(String category) {
        if (category == null) return 0.0;
        switch (category.toUpperCase()) {
            case "ELECTRONICS": return 0.0;
            case "APPLIANCES": return 1.0;
            case "AUTOMOBILE": return 2.0;
            case "FURNITURE": return 3.0;
            default: return 0.0;
        }
    }

    private double calculateEuclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    private String determineRiskLevel(boolean willRenew, double confidence) {
        if (willRenew && confidence >= 80) return "Low";
        if (willRenew && confidence >= 60) return "Medium";
        if (willRenew) return "High";
        if (!willRenew && confidence >= 80) return "High";
        if (!willRenew && confidence >= 60) return "Medium";
        return "Medium";
    }

    private String generateRecommendation(boolean willRenew, double confidence) {
        if (willRenew && confidence >= 80) {
            return "Send personalized premium renewal offer 30 days before expiry. Consider a 10-15% loyalty discount.";
        } else if (willRenew) {
            return "Moderate renewal likelihood detected. Trigger automated reminder 45 days before expiry.";
        } else if (!willRenew && confidence >= 80) {
            return "Customer is unlikely to renew. Assign to customer success representative. Offer competitive discount.";
        } else {
            return "Borderline prediction. Schedule a satisfaction survey 60 days before expiry to understand pain points.";
        }
    }

    private List<String> generateDynamicFactors(List<RenewalTraining> neighbors, boolean predictedRenew, 
                                                int qCustomerAge, int qProductAge, double qProductCost, int qPreviousRenewals, int qSatisfaction) {
        List<String> factors = new ArrayList<>();
        
        // Analyze neighbors that matched the predicted outcome
        List<RenewalTraining> matchingNeighbors = neighbors.stream()
                .filter(n -> (n.getRenewed() == 1) == predictedRenew)
                .collect(Collectors.toList());
        
        if (matchingNeighbors.isEmpty()) matchingNeighbors = neighbors; // fallback

        double avgSatisfaction = matchingNeighbors.stream().mapToDouble(RenewalTraining::getSatisfactionScore).average().orElse(0);
        double avgProductAge = matchingNeighbors.stream().mapToDouble(RenewalTraining::getProductAge).average().orElse(0);
        double avgRenewals = matchingNeighbors.stream().mapToDouble(RenewalTraining::getPreviousRenewals).average().orElse(0);
        double avgCost = matchingNeighbors.stream().mapToDouble(RenewalTraining::getProductCost).average().orElse(0);

        factors.add(String.format("Found %d similar historical profiles with %s renewal tendency.", 
            neighbors.size(), predictedRenew ? "strong" : "weak"));
            
        if (qSatisfaction >= 7 && avgSatisfaction >= 7) {
            factors.add(String.format("High satisfaction score aligns with historical trend (Avg %.1f/10).", avgSatisfaction));
        } else if (qSatisfaction <= 4 && avgSatisfaction <= 5) {
            factors.add(String.format("Low satisfaction aligns with historical churn profiles (Avg %.1f/10).", avgSatisfaction));
        }
        
        if (qPreviousRenewals >= 1 && avgRenewals >= 1) {
            factors.add(String.format("Previous renewals indicate loyalty, matching similar customers (Avg %.1f renewals).", avgRenewals));
        } else if (qPreviousRenewals == 0 && avgRenewals < 1) {
            factors.add("Lack of previous renewals is a common pattern in non-renewing similar products.");
        }

        if (qProductAge >= 5 && avgProductAge >= 4) {
            factors.add(String.format("Product age (%d yrs) is typical for historical non-renewals (Avg %.1f yrs).", qProductAge, avgProductAge));
        }
        
        if (qProductCost > 40000 && avgCost > 30000) {
            factors.add("Premium product value correlates strongly with historical retention in this cluster.");
        }

        if (factors.size() == 1) {
            factors.add("Combination of baseline factors matches this nearest neighbor cluster.");
        }
        
        return factors;
    }

    private PredictionInsights fallbackPrediction() {
        return new PredictionInsights("Unknown", 50.0, 50.0, "Medium", "Insufficient training data.", Collections.singletonList("No data"));
    }

    private static class Neighbor {
        double distance;
        int label;
        RenewalTraining record;
        Neighbor(double distance, int label, RenewalTraining record) { 
            this.distance = distance; 
            this.label = label; 
            this.record = record;
        }
        double getDistance() { return distance; }
        int getLabel() { return label; }
        RenewalTraining getRecord() { return record; }
    }
}
