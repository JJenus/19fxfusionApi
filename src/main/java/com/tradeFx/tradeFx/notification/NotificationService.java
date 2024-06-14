package com.tradeFx.tradeFx.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Notification> getNotificationsForUser(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification createNotification(Notification notification) {
        notification.setStatus(NotificationStatus.UNREAD);
        Notification savedNotification = notificationRepository.save(notification);
        sendNotificationToUser(notification.getUserId(), notification);
        return savedNotification;
    }

    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Notification not found"));
        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);

        return notification;
    }

    public void sendNotificationToUser(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/queue/notifications/"+userId, notification);
    }
}
