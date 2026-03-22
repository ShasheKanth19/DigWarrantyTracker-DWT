package com.example.demo.scheduler;

import com.example.demo.model.ProductPurchase;
import com.example.demo.service.NotificationService;
import com.example.demo.service.ProductPurchaseService;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class WarrantyScheduler {

    @Autowired
    private ProductPurchaseService productService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    // Run every minute (was every 10 seconds)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkWarrantyExpiry() {
        System.out.println("Scheduler running: Checking for expiring warranties at " + LocalDate.now());

        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(7); // Notify 7 days before expiry
        LocalDate yesterday = today.minusDays(1); // Notify if expired yesterday

        List<LocalDate> targetDates = java.util.Arrays.asList(today, warningDate, yesterday);
        List<ProductPurchase> products = productService.getPurchasesExpiringOnDates(targetDates);

        for (ProductPurchase product : products) {
            LocalDate expiryDate = product.getWarrantyExpiryDate();
            String message = null;

            if (expiryDate.isEqual(warningDate)) {
                message = "Warranty for " + product.getName() + " expires in 7 days on " + expiryDate;
            } else if (expiryDate.isEqual(today)) {
                message = "URGENT: Warranty for " + product.getName() + " expires TODAY (" + expiryDate + ")";
            } else if (expiryDate.isEqual(yesterday)) {
                message = "Warranty for " + product.getName() + " EXPIRED yesterday (" + expiryDate + ")";
            }

            if (message != null) {
                // Check if notification already exists for today to prevent duplicates
                boolean exists = notificationRepository.existsByUserIdAndMessageAndDate(
                        product.getUser().getId(), message, today);

                if (!exists) {
                    notificationService.createNotification(message, product.getUser());
                    System.out.println("Created notification: " + message);
                }
            }
        }
    }
}
