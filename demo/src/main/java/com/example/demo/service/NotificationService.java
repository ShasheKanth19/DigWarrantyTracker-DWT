package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public Notification createNotification(String message, User user) {
        Notification notification = new Notification(message, LocalDate.now(), user);
        return repository.save(notification);
    }

    public List<Notification> getUserNotifications(int userId) {
        return repository.findByUserId(userId);
    }

    public List<Notification> getUnreadUserNotifications(int userId) {
        return repository.findByUserIdAndIsReadFalse(userId);
    }

    public Notification markAsRead(int id) {
        return repository.findById(id).map(notification -> {
            notification.setRead(true);
            return repository.save(notification);
        }).orElse(null);
    }
}
