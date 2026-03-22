package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable int userId) {
        return service.getUserNotifications(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Notification> getUnreadUserNotifications(@PathVariable int userId) {
        return service.getUnreadUserNotifications(userId);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable int id) {
        return service.markAsRead(id);
    }
}
