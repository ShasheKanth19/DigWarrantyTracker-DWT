package com.example.demo.ml;

import com.example.demo.model.RenewalTraining;
import com.example.demo.repository.RenewalTrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder {

    @Autowired
    private RenewalTrainingRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void seedData() {
        if (repository.count() >= 1000) {
            System.out.println("Renewal training data already seeded with sufficient records (" + repository.count() + ").");
            return;
        }
        
        if (repository.count() > 0) {
            System.out.println("Clearing insufficient training data...");
            repository.deleteAll();
        }
        
        System.out.println("Seeding 3000 realistic records for ML training...");
        List<RenewalTraining> data = new ArrayList<>();
        Random random = new Random(42); // fixed seed for reproducibility
        
        String[] categories = {"ELECTRONICS", "APPLIANCES", "AUTOMOBILE", "FURNITURE"};
        
        for (int i = 0; i < 3000; i++) {
            int customerAge = 18 + random.nextInt(60); // 18 to 77
            int productAge = random.nextInt(10); // 0 to 9
            double productCost = 1000 + (random.nextDouble() * 99000); // 1000 to 100000
            int warrantyDuration = 6 + random.nextInt(31); // 6 to 36 months
            int previousRenewals = random.nextInt(5); // 0 to 4
            int satisfactionScore = 1 + random.nextInt(10); // 1 to 10
            String category = categories[random.nextInt(categories.length)];
            
            // Label Generation Rules - Derived from business logic, NO random noise
            int renewed = calculateRealisticRenewalLabel(productAge, productCost, previousRenewals, satisfactionScore);
            
            data.add(new RenewalTraining(customerAge, productAge, productCost, warrantyDuration, previousRenewals, satisfactionScore, category, renewed));
        }
        
        repository.saveAll(data);
        System.out.println("Seeded 3000 ML records successfully.");
    }

    private int calculateRealisticRenewalLabel(int productAge, double productCost, int previousRenewals, int satisfactionScore) {
        double score = 0;
        
        // High satisfaction score is a strong positive
        if (satisfactionScore >= 8) score += 3;
        else if (satisfactionScore >= 6) score += 1;
        else if (satisfactionScore <= 4) score -= 3;
        
        // Previous renewals indicate loyalty
        if (previousRenewals >= 2) score += 2;
        else if (previousRenewals == 1) score += 1;
        else score -= 1; // 0 previous renewals
        
        // Older products are less likely to be renewed
        if (productAge >= 6) score -= 2;
        else if (productAge >= 3) score -= 1;
        else score += 1;
        
        // Expensive products are more likely to be renewed
        if (productCost > 50000) score += 2;
        else if (productCost > 20000) score += 1;
        
        return score > 0 ? 1 : 0;
    }
}
