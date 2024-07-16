package com.tradeFx.tradeFx.notification;

import com.github.javafaker.Faker;
import com.tradeFx.tradeFx.HistoricalData.HistoricalData;
import com.tradeFx.tradeFx.config.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Configuration
public class NotificationTask {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    WebSocketController webSocketController;

//    @Scheduled(fixedRate = 3000)  // Schedule to run every 1000 milliseconds (1 second)
    public void sendPeriodicNotifications() {
        HistoricalData data =
                new HistoricalData(1L, LocalDateTime.now(),
                        1.99761, 1.808177, 75628276, 1.093783, 8878900, "1h");
        webSocketController.sendFx(data);
    }

//    @Scheduled(fixedRate = 3000)  // Schedule to run every 1000 milliseconds (1 second)
    public void setNotificationService() {
        Faker faker = new Faker();

        Notification notification = new Notification(1L, 1L, faker.lorem().sentence(), faker.lebowski().quote(), Priority.NORMAL, NotificationStatus.UNREAD, LocalDateTime.now());
        notificationService.sendNotificationToUser(notification.getUserId(), notification);
        notificationService.broadcast(notification);
    }
}

