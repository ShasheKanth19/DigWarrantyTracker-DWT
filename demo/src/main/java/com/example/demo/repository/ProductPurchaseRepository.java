package com.example.demo.repository;

import com.example.demo.model.ProductPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductPurchaseRepository extends JpaRepository<ProductPurchase, Integer> {
    List<ProductPurchase> findByUserId(int userId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM ProductPurchase p JOIN FETCH p.user WHERE p.warrantyExpiryDate IN :dates")
    List<ProductPurchase> findByWarrantyExpiryDateIn(
            @org.springframework.data.repository.query.Param("dates") List<java.time.LocalDate> dates);
}
