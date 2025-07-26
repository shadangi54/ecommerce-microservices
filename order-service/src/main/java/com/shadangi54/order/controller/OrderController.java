package com.shadangi54.order.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.order.DTO.OrderDTO;
import com.shadangi54.order.event.OrderFailedEvent;
import com.shadangi54.order.event.OrderPlacedEvent;
import com.shadangi54.order.exception.OrderServiceException;
import com.shadangi54.order.manager.OrderManager;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	private OrderManager orderManager;
	
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@GetMapping("/customer/{customerName}")
	public ResponseEntity<Object> getAllOrdersForCustomer(@PathVariable String customerName){
		LOGGER.info("Fetching all orders for customer: {}", customerName);
		try {
			List<OrderDTO> orders = orderManager.getAllOrdersForCustomer(customerName);
			if (orders.isEmpty()) {
				LOGGER.info("No orders found for customer: {}", customerName);
				return ResponseEntity.noContent().build();
			}
			LOGGER.info("Orders found for customer: {}", customerName);
			return ResponseEntity.ok(orders);
		} catch (OrderServiceException e) {
			LOGGER.error("Invalid customer name: {}", customerName, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Error fetching orders for customer: {}", customerName, e);
			return ResponseEntity.status(500).build();
		} 
	}
	
	@GetMapping("/{orderNumber}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderNumber) {
		LOGGER.info("Fetching order with ID: {}", orderNumber);
		try {
			OrderDTO order = orderManager.getOrderById(orderNumber);
			if (order == null) {
				LOGGER.info("No order found with ID: {}", orderNumber);
				return ResponseEntity.notFound().build();
			}
			LOGGER.info("Order found with ID: {}", orderNumber);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			LOGGER.error("Error fetching order with ID: {}", orderNumber, e);
			return ResponseEntity.status(500).build();
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> createOrder(@RequestBody OrderDTO orderDTO) {
		LOGGER.info("Creating new order: {}", orderDTO);
		try {
			// Generate a unique order number using date-time and UUID
			String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			String uniqueOrderNumber = dateTime + "-" + UUID.randomUUID().toString();
			OrderDTO pendingOrder = new OrderDTO();
			pendingOrder.setOrderNumber(uniqueOrderNumber);
			pendingOrder.setStatus("PENDING");
			pendingOrder.setCustomerName(orderDTO.getCustomerName());
			
			CompletableFuture.runAsync(() ->{
				
				try {
					// Complete actual order processing in background
					orderDTO.setOrderNumber(uniqueOrderNumber);
	                OrderDTO completedOrder = orderManager.createOrder(orderDTO);
	                
	             // Send order placed event to Kafka topic
	                kafkaTemplate.send("shadangi54-notification-topic",
	                        new OrderPlacedEvent(completedOrder.getCustomerName(), completedOrder.getOrderNumber()));
	                
	                LOGGER.info("Order processed asynchronously: {}", completedOrder);
	                
				}catch (OrderServiceException e) {
					LOGGER.error("Invalid order data: {}", orderDTO, e);
	                // Send failure notification
	                kafkaTemplate.send("shadangi54-notification-topic",
	                        new OrderFailedEvent(orderDTO.getCustomerName(), uniqueOrderNumber, e.getMessage()));
				} catch (Exception e) {
					LOGGER.error("Error in async order processing: {}", orderDTO, e);
	                // Send failure notification
	                kafkaTemplate.send("shadangi54-notification-topic",
	                        new OrderFailedEvent(orderDTO.getCustomerName(), uniqueOrderNumber, e.getMessage()));
				}
			});
			
			//Return pending order response immediately
			LOGGER.info("Order created and pending: {}", pendingOrder);
			return ResponseEntity.accepted().body("Order is being processed. Order number: " + uniqueOrderNumber);
		} catch (Exception e) {
			 LOGGER.error("Error initiating order: {}", orderDTO, e);
		        return ResponseEntity.status(500).body("Failed to initiate order process: " + e.getMessage());
		}
	}
	
	@PutMapping("/{orderNumber}")
	public ResponseEntity<Object> updateOrder(@PathVariable String orderNumber, @RequestParam String status) {
		LOGGER.info("Updating order with ID: {}", orderNumber);
		try {
			OrderDTO updatedOrder = orderManager.updateOrder(orderNumber, status);
			if (updatedOrder == null) {
				LOGGER.info("No order found with ID: {}", orderNumber);
				return ResponseEntity.notFound().build();
			}
			LOGGER.info("Order updated successfully: {}", updatedOrder);
			return ResponseEntity.ok(updatedOrder);
		} catch (OrderServiceException e) {
			LOGGER.error("Invalid order ID or data: {}", orderNumber, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Error updating order with ID: {}", orderNumber, e);
			return ResponseEntity.status(500).build();
		}
	}
	
}
