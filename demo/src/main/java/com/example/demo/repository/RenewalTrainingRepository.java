package com.example.demo.repository;

import com.example.demo.model.RenewalTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenewalTrainingRepository extends JpaRepository<RenewalTraining, Long> {
}
