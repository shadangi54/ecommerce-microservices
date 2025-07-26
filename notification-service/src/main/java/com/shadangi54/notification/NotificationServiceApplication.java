package com.shadangi54.notification;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

import com.shadangi54.notification.event.OrderPlacedEvent;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {
	
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(NotificationServiceApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
	
//	@KafkaListener(topics = "shadangi54-notification-topic")
//	public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
//		//Send out an email notification
//		LOGGER.info("Received order placed event: {}", orderPlacedEvent);
//	}
}
