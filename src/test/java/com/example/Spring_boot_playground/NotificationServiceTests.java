package com.example.Spring_boot_playground;

import com.example.Spring_boot_playground.kafka.NotificationProducer;
import com.example.Spring_boot_playground.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class NotificationServiceTests {
    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationProducer notificationProducer;

    @Test
    @DisplayName("Successfully send a String message to kafka topic.")
    void testSendNotification() {
        String message = "Test notification";
        notificationService.sendNotification(message);
        verify(notificationProducer, times(1)).sendMessage(message);
    }
}
