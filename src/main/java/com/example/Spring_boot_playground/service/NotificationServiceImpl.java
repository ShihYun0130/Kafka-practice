package com.example.Spring_boot_playground.service;

import com.example.Spring_boot_playground.kafka.NotificationProducer;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationProducer notificationProducer;

    public NotificationServiceImpl(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    @Override
    public void sendNotification(String message) {
        notificationProducer.sendMessage(message);
    }

}
