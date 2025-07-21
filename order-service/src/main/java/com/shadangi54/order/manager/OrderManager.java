package com.shadangi54.order.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shadangi54.order.DAO.OrderDAO;
import com.shadangi54.order.DTO.OrderDTO;
import com.shadangi54.order.DTO.OrderStatus;
import com.shadangi54.order.entity.Order;
import com.shadangi54.order.entity.OrderProduct;
import com.shadangi54.order.mapper.OrderMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderManager.class);
	
	private OrderDAO orderDAO;
	
	private OrderMapper orderMapper;

	public List<OrderDTO> getAllOrdersForCustomer(String customerName) {
		LOGGER.info("Fetching all orders for customer: {}", customerName);
		if (customerName == null || customerName.trim().isEmpty()) {
			LOGGER.error("Invalid customer name: {}", customerName);
			throw new IllegalArgumentException("Customer name cannot be null or empty");
		}
		
		List<Order> orders = orderDAO.getAllOrdersForCustomer(customerName);
		if (orders == null || orders.isEmpty()) {
			LOGGER.info("No orders found for customer: {}", customerName);
			return List.of(); // Return an empty list if no orders are found
		}
		LOGGER.info("Orders found for customer: {}", customerName);
		return orderMapper.toDTOList(orders);
	}

	public OrderDTO getOrderById(String orderNumber) {
		LOGGER.info("Fetching order with ID: {}", orderNumber);
		if (orderNumber == null) {
			LOGGER.error("Invalid order ID: {}", orderNumber);
			throw new IllegalArgumentException("Order ID must not be null");
		}

		Order order = orderDAO.findByOrderNumber(orderNumber);
		if (order == null || order.getIsDeleted()) {
			LOGGER.info(orderNumber);
			return null; // Return null if no order is found
		}

		LOGGER.info("Order found with ID: {}", orderNumber);
		return orderMapper.toDTO(order);
	}

	public OrderDTO createOrder(OrderDTO orderDTO) {
		LOGGER.info("Creating new order: {}", orderDTO);
		if (orderDTO == null) {
			LOGGER.error("OrderDTO cannot be null");
			throw new IllegalArgumentException("OrderDTO cannot be null");
		}
		
		Order order = orderMapper.toEntity(orderDTO);
		// Set metadata for the order
		order.setCreatedAt(String.valueOf(System.currentTimeMillis())); // Set created date
		order.setModifiedAt(String.valueOf(System.currentTimeMillis())); // Set modified date
		order.setCreatedBy("system"); // Set created by user, can be modified as needed
		order.setModifiedBy("system");
		order.setStatus(OrderStatus.PENDING); // Default status for new orders
		
		//create a unique order number using date time
		// Generate a unique order number using date-time and UUID
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String uniqueOrderNumber = dateTime + "-" + UUID.randomUUID().toString();
		order.setOrderNumber(uniqueOrderNumber); // Ensure order number is set

		// Ensure FK referencing for each product
		if (order.getProducts() != null) {
			for (OrderProduct product : order.getProducts()) {
				product.setOrder(order); // Set the order reference in each product
			}
		}

		// Ensure the order is not marked as deleted
		order.setIsDeleted(false);

		// Save the order and its products
		order = orderDAO.save(order);

		LOGGER.info("Order created successfully with ID: {}", order.getOrderNumber());
		return orderMapper.toDTO(order);
	}

	public OrderDTO updateOrder(String orderNumber, String status) {
		LOGGER.info("Updating order with ID: {} to status: {}", orderNumber, status);
		if (orderNumber == null) {
			LOGGER.error("Invalid order ID: {}", orderNumber);
			throw new IllegalArgumentException("Order ID must not be null");
		}

		Order order = orderDAO.findByOrderNumber(orderNumber);
		if (order == null || order.getIsDeleted()) {
			LOGGER.info("No order found with ID: {}", orderNumber);
			return null; // Return null if no order is found
		}

		order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
		order.setModifiedAt(String.valueOf(System.currentTimeMillis())); // Update modified date
		order.setModifiedBy("system"); // Update modified by user, can be modified as needed
		order = orderDAO.save(order);
		LOGGER.info("Order updated successfully with ID: {}", order.getOrderNumber());
		return orderMapper.toDTO(order);
	}

}
