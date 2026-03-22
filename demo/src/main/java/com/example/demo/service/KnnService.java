package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnnService {

    @Autowired
    private JdbcTemplate jdbc;

    // Simple in-memory category encoding cache
    private Map<String, Double> categoryMapCache = new HashMap<>();

    // Ensure category map exists
    private void ensureCategoryMap() {
        if (!categoryMapCache.isEmpty())
            return;
        List<String> cats = jdbc.queryForList("SELECT DISTINCT category FROM renewal_training", String.class);
        double idx = 0.0;
        for (String c : cats) {
            categoryMapCache.put(c, idx++);
        }
    }

    // Load all training rows as double arrays: [cat, price, days, prev, ext, label]
    private List<double[]> loadTraining() {
        ensureCategoryMap();
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT category, price, days_to_expiry, previous_renewals, extended, renewed FROM renewal_training");
        List<double[]> out = new ArrayList<>();
        for (Map<String, Object> r : rows) {
            String catS = (String) r.get("category");
            double cat = categoryMapCache.getOrDefault(catS, -1.0);
            double price = ((Number) r.get("price")).doubleValue();
            double days = ((Number) r.get("days_to_expiry")).doubleValue();
            double prev = ((Number) r.get("previous_renewals")).doubleValue();
            double ext = ((Number) r.get("extended")).doubleValue();
            double label = ((Number) r.get("renewed")).doubleValue();
            out.add(new double[] { cat, price, days, prev, ext, label });
        }
        return out;
    }

    // Normalize / scale a feature vector. Use same scaling as training loader.
    private double[] scaleVector(double[] v) {
        double cat = v[0];
        double price = v[1] / 10000.0; // scale price by 10k
        double days = v[2] / 30.0; // scale days by 30
        double prev = v[3]; // small integer
        double ext = v[4];
        return new double[] { cat, price, days, prev, ext };
    }

    private double distance(double[] a, double[] b) {
        // a and b are already scaled vectors of length 5
        double sum = 0;
        for (int i = 0; i < 5; i++) {
            double d = a[i] - b[i];
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    /**
     * Predict renewal probability and label
     * 
     * @param category     string category (e.g. "electronics")
     * @param price        double price
     * @param daysToExpiry int
     * @param prevRenewals int
     * @param extended     int (0/1)
     * @param k            int neighbors
     * @return map with probability and neighbors info
     */
    public Map<String, Object> predict(String category, double price, int daysToExpiry, int prevRenewals, int extended,
            int k) {
        List<double[]> train = loadTraining();
        if (train.isEmpty()) {
            return Map.of("error", "no training data found");
        }

        // ensure category mapping includes the input category (if new, add)
        ensureCategoryMap();
        double catVal = categoryMapCache.computeIfAbsent(category, c -> (double) categoryMapCache.size());

        double[] queryRaw = new double[] { catVal, price, daysToExpiry, prevRenewals, extended };
        double[] query = scaleVector(queryRaw);

        // prepare list of [dist, label]
        List<double[]> dists = new ArrayList<>();
        for (double[] row : train) {
            double[] feat = Arrays.copyOf(row, 5); // features
            double[] featScaled = scaleVector(feat);
            double dist = distance(query, featScaled);
            dists.add(new double[] { dist, row[5] }); // label is at index 5
        }

        // sort ascending by distance
        dists.sort(Comparator.comparingDouble(a -> a[0]));

        k = Math.max(1, Math.min(k, dists.size()));
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += (int) dists.get(i)[1];
        }
        double prob = (double) sum / k;
        int pred = prob >= 0.5 ? 1 : 0;

        // build neighbor info (optional)
        List<Map<String, Object>> neighbors = dists.stream().limit(k)
                .map(d -> {
                    Map<String, Object> neighbor = new HashMap<>();
                    neighbor.put("distance", d[0]);
                    neighbor.put("label", (int) d[1]);
                    return neighbor;
                })
                .collect(Collectors.toList());

        Map<String, Object> res = new HashMap<>();
        res.put("k", k);
        res.put("renewal_probability", prob);
        res.put("renewal_pred", pred);
        res.put("neighbors", neighbors);
        return res;
    }
}
