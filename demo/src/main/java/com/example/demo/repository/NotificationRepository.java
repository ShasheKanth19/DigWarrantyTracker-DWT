package com.example.demo.repository;

import com.example.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(int userId);

    List<Notification> findByUserIdAndIsReadFalse(int userId);

    boolean existsByUserIdAndMessageAndDate(int userId, String message, LocalDate date);
}
