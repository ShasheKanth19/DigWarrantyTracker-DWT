package com.example.demo.ml;

import com.example.demo.ml.dto.DataQualityReport;
import com.example.demo.model.RenewalTraining;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataPreprocessingService {

    // Feature normalization bounds
    private double minAge, maxAge;
    private double minProductAge, maxProductAge;
    private double minCost, maxCost;
    private double minDuration, maxDuration;
    private double minRenewals, maxRenewals;
    private double minSatisfaction, maxSatisfaction;

    public DataQualityReport analyzeAndPreprocess(List<RenewalTraining> rawData) {
        if (rawData == null || rawData.isEmpty()) {
            return new DataQualityReport(0, 0, 0, 0, 0, 0.0);
        }

        int missingValues = 0;
        int duplicates = 0;
        int outliers = 0;

        Set<String> uniqueSignatures = new HashSet<>();
        List<RenewalTraining> validData = new ArrayList<>();

        // Statistics for outlier detection
        double sumCost = 0;
        for (RenewalTraining row : rawData) {
            sumCost += row.getProductCost();
        }
        double meanCost = sumCost / rawData.size();
        double varianceCost = 0;
        for (RenewalTraining row : rawData) {
            varianceCost += Math.pow(row.getProductCost() - meanCost, 2);
        }
        double stdDevCost = Math.sqrt(varianceCost / rawData.size());

        for (RenewalTraining row : rawData) {
            // Missing values check (simulated as Java primitives default to 0)
            if (row.getCategory() == null || row.getCategory().isEmpty()) {
                missingValues++;
                continue; // drop row
            }

            // Duplicate check
            String sig = row.getCustomerAge() + "_" + row.getProductAge() + "_" + row.getProductCost() + "_" + row.getSatisfactionScore();
            if (!uniqueSignatures.add(sig)) {
                duplicates++;
                continue; // drop duplicate
            }

            // Outlier check (Z-score > 3 for cost)
            if (stdDevCost > 0 && Math.abs((row.getProductCost() - meanCost) / stdDevCost) > 3) {
                outliers++;
                continue; // drop outlier
            }

            validData.add(row);
        }

        calculateBounds(validData);

        double score = 100.0 - ((missingValues + duplicates + outliers) * 100.0 / rawData.size());
        
        return new DataQualityReport(rawData.size(), validData.size(), missingValues, duplicates, outliers, Math.max(0, score));
    }

    private void calculateBounds(List<RenewalTraining> data) {
        minAge = data.stream().mapToDouble(RenewalTraining::getCustomerAge).min().orElse(18);
        maxAge = data.stream().mapToDouble(RenewalTraining::getCustomerAge).max().orElse(80);
        
        minProductAge = data.stream().mapToDouble(RenewalTraining::getProductAge).min().orElse(0);
        maxProductAge = data.stream().mapToDouble(RenewalTraining::getProductAge).max().orElse(10);
        
        minCost = data.stream().mapToDouble(RenewalTraining::getProductCost).min().orElse(1000);
        maxCost = data.stream().mapToDouble(RenewalTraining::getProductCost).max().orElse(100000);
        
        minDuration = data.stream().mapToDouble(RenewalTraining::getWarrantyDuration).min().orElse(6);
        maxDuration = data.stream().mapToDouble(RenewalTraining::getWarrantyDuration).max().orElse(36);
        
        minRenewals = data.stream().mapToDouble(RenewalTraining::getPreviousRenewals).min().orElse(0);
        maxRenewals = data.stream().mapToDouble(RenewalTraining::getPreviousRenewals).max().orElse(5);
        
        minSatisfaction = data.stream().mapToDouble(RenewalTraining::getSatisfactionScore).min().orElse(1);
        maxSatisfaction = data.stream().mapToDouble(RenewalTraining::getSatisfactionScore).max().orElse(10);
    }

    public double[] normalize(int customerAge, int productAge, double productCost, 
                              int warrantyDuration, int previousRenewals, int satisfactionScore, double categoryEncoded) {
        return new double[]{
            scale(customerAge, minAge, maxAge),
            scale(productAge, minProductAge, maxProductAge),
            scale(productCost, minCost, maxCost),
            scale(warrantyDuration, minDuration, maxDuration),
            scale(previousRenewals, minRenewals, maxRenewals),
            scale(satisfactionScore, minSatisfaction, maxSatisfaction),
            categoryEncoded / 3.0 // specific scaling for known category mapping
        };
    }

    private double scale(double value, double min, double max) {
        if (max == min) return 0.0;
        double scaled = (value - min) / (max - min);
        // Clamp between 0 and 1
        return Math.max(0.0, Math.min(1.0, scaled));
    }
}
