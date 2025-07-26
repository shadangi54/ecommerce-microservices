package com.shadangi54.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.order.DTO.OrderStatusDTO;
import com.shadangi54.order.exception.OrderServiceException;
import com.shadangi54.order.manager.OrderManager;

@RestController
@RequestMapping("/orders/status")
public class OrderStatusController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusController.class);
	
	private OrderManager orderManager;
	
	@GetMapping("{orderId}")
	public ResponseEntity<Object> getOrderStatus(@PathVariable String orderId) {
		LOGGER.info("Fetching status for order ID: {}", orderId);
		try {
			OrderStatusDTO status = orderManager.getOrderStatus(orderId);
			if (status == null) {
				LOGGER.info("No status found for order ID: {}", orderId);
				return ResponseEntity.noContent().build();
			}
			LOGGER.info("Status found for order ID: {}", orderId);
			return ResponseEntity.ok(status);
		} catch (OrderServiceException e) {
			LOGGER.error("Error fetching status for order ID: {}", orderId, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected error fetching status for order ID: {}", orderId, e);
			return ResponseEntity.status(500).build();
		}
	}
}
