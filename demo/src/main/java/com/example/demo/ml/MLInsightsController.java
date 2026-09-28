package com.example.demo.ml;

import com.example.demo.model.PredictionHistory;
import com.example.demo.repository.PredictionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ml")
@CrossOrigin(origins = "http://localhost:3000")
public class MLInsightsController {

    @Autowired
    private MLMetricsService metricsService;

    @Autowired
    private PredictionHistoryRepository historyRepository;

    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats() {
        return metricsService.getDashboardStats();
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMLMetrics() {
        return metricsService.getMLMetrics();
    }

    @GetMapping("/predictions/{productId}")
    public PredictionHistory getPredictionForProduct(@PathVariable int productId) {
        Optional<PredictionHistory> history = historyRepository.findByProduct_Id(productId);
        return history.orElse(null);
    }

    @Autowired
    private com.example.demo.repository.ProductPurchaseRepository productRepository;

    @Autowired
    private MLPredictionService predictionService;

    @PostMapping("/backfill")
    public String backfillPredictions() {
        int count = 0;
        for (com.example.demo.model.ProductPurchase product : productRepository.findAll()) {
            if (historyRepository.findByProduct_Id(product.getId()).isEmpty()) {
                predictionService.generateAndSavePrediction(product);
                count++;
            }
        }
        return "{\"status\": \"success\", \"message\": \"Backfilled " + count + " predictions.\"}";
    }
}
