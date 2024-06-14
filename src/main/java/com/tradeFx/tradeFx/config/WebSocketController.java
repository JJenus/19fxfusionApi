package com.tradeFx.tradeFx.config;

import com.tradeFx.tradeFx.HistoricalData.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        System.out.println(message);
        return message;
    }

    public void sendFx(HistoricalData message) {
        messagingTemplate.convertAndSend("/topic/fx", message);
    }
}

