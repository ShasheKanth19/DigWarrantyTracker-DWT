package com.example.demo.repository;

import com.example.demo.model.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {
    Optional<PredictionHistory> findByProduct_Id(int productId);
}
