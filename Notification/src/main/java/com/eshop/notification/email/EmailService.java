package com.eshop.notification.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // log.info("Received Notification for Order - {}",orderPlacedEvent.getOrderNumber());
        sendEmail(orderPlacedEvent.getTo(), orderPlacedEvent.getSubject(),
                orderPlacedEvent.getText(), orderPlacedEvent.getOrderNumber());
    }

    public void sendEmail(String to, String subject, String text, String orderNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text + orderNumber);

        javaMailSender.send(message);
    }
}
