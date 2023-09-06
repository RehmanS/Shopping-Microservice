package com.eshop.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    public NewTopic notificationTopic()
    {
        return TopicBuilder.name("NOTIFICATION_TOPIC")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
