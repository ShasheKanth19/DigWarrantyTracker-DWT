package com.example.demo.controller;

import com.example.demo.service.KnnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "http://localhost:3000")
public class PredictController {

    @Autowired
    private KnnService knnService;

    @PostMapping("/renewal")
    public Map<String, Object> predict(@RequestBody Map<String, Object> body) {
        String category = (String) body.getOrDefault("category", "electronics");
        double price = ((Number) body.getOrDefault("price", 0)).doubleValue();
        int days = ((Number) body.getOrDefault("days_to_expiry", 0)).intValue();
        int prev = ((Number) body.getOrDefault("previous_renewals", 0)).intValue();
        int ext = ((Number) body.getOrDefault("extended", 0)).intValue();
        int k = ((Number) body.getOrDefault("k", 5)).intValue();

        return knnService.predict(category, price, days, prev, ext, k);
    }
}
