package com.shadangi54.notification.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.shadangi54.notification.event.OrderFailedEvent;
import com.shadangi54.notification.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
	
	
	@KafkaListener(topics = "shadangi54-notification-topic")
	public void handleNotification(ConsumerRecord<String, Object> record) {
		LOGGER.info("Received event: {}", record);
		Object event = record.value();
		try {
			if(event instanceof OrderPlacedEvent) {
				handleOrderPlacedEvent((OrderPlacedEvent) event);
			} else if(event instanceof OrderFailedEvent) {
				handleOrderFailedEvent((OrderFailedEvent) event);
			} else {
				LOGGER.warn("Unknown event type: {}", event.getClass().getName());
			}
		}catch (Exception e) {
			LOGGER.error("Error processing event: {}", event, e);
			return;
		}
	}
	
	
	
	private void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
		// Logic to handle order placed event
		LOGGER.info("Handling order placed event: {}", orderPlacedEvent);
	}
	
	private void handleOrderFailedEvent(OrderFailedEvent orderFailedEvent) {
		// Logic to handle order failed event
		LOGGER.info("Handling order failed event: {}", orderFailedEvent);
	}
}
