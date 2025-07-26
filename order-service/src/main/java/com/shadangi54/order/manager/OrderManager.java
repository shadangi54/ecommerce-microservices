package com.shadangi54.order.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shadangi54.order.DAO.OrderDAO;
import com.shadangi54.order.DTO.InventoryDTO;
import com.shadangi54.order.DTO.OrderDTO;
import com.shadangi54.order.DTO.OrderProductDTO;
import com.shadangi54.order.DTO.OrderStatus;
import com.shadangi54.order.DTO.OrderStatusDTO;
import com.shadangi54.order.entity.Order;
import com.shadangi54.order.entity.OrderProduct;
import com.shadangi54.order.exception.OrderServiceException;
import com.shadangi54.order.feign.InventoryClient;
import com.shadangi54.order.mapper.OrderMapper;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderManager.class);
	
	private OrderDAO orderDAO;
	
	private OrderMapper orderMapper;
	
	private InventoryClient inventoryClient;

	public List<OrderDTO> getAllOrdersForCustomer(String customerName) {
		LOGGER.info("Fetching all orders for customer: {}", customerName);
		if (customerName == null || customerName.trim().isEmpty()) {
			LOGGER.error("Invalid customer name: {}", customerName);
			throw new OrderServiceException("Customer name cannot be null or empty");
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
			throw new OrderServiceException("Order ID must not be null");
		}

		Order order = orderDAO.findByOrderNumber(orderNumber);
		if (order == null || order.getIsDeleted()) {
			LOGGER.info(orderNumber);
			return null; // Return null if no order is found
		}

		LOGGER.info("Order found with ID: {}", orderNumber);
		return orderMapper.toDTO(order);
	}
	
	
	@Transactional(TxType.REQUIRES_NEW)
	public OrderDTO createOrder(OrderDTO orderDTO) {
		LOGGER.info("Creating new order: {}", orderDTO);
		if (orderDTO == null) {
			LOGGER.error("OrderDTO cannot be null");
			throw new IllegalArgumentException("OrderDTO cannot be null");
		}
		
		List<String> skuCodes = orderDTO.getProducts().stream().map(OrderProductDTO::getSkuCode).toList();
		List<InventoryDTO> inventoryList = inventoryClient.checkStock(skuCodes).getBody();
		
		if (inventoryList == null || inventoryList.isEmpty()) {
			LOGGER.error("No inventory data returned for SKU codes: {}", skuCodes);
			throw new OrderServiceException("No inventory data available for ordered products");
		}
		
		// Check if all products from OrderDTO are in stock
		for (OrderProductDTO productDTO : orderDTO.getProducts()) {
			boolean inStock = inventoryList.stream()
					.anyMatch(inv -> inv.getSkuCode().equals(productDTO.getSkuCode()) && inv.getQuantity() > 0 && productDTO.getQuantity() <= inv.getQuantity());
			if (!inStock) {
				LOGGER.error("Product with SKU code {} is not in stock", productDTO.getSkuCode());
				throw new OrderServiceException(
						"Product with SKU code " + productDTO.getSkuCode() + " is not in stock");
			}
			
			//Update the inventoryList with the productDTO quantity ordered
			 inventoryList.stream()
		        .filter(inv -> inv.getSkuCode().equals(productDTO.getSkuCode()))
		        .forEach(inv -> inv.setQuantity(inv.getQuantity() - productDTO.getQuantity()));
			
		}
		
		
		
		Order order = orderMapper.toEntity(orderDTO);
		// Set metadata for the order
		order.setCreatedAt(String.valueOf(System.currentTimeMillis())); // Set created date
		order.setModifiedAt(String.valueOf(System.currentTimeMillis())); // Set modified date
		order.setCreatedBy("system"); // Set created by user, can be modified as needed
		order.setModifiedBy("system");
		order.setStatus(OrderStatus.PENDING); // Default status for new orders
		order.setOrderNumber(orderDTO.getOrderNumber()); // Ensure order number is set

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
		
		// Update inventory after order creation
		inventoryClient.updateInventory(inventoryList);
		
		return orderMapper.toDTO(order);
	}

	@Transactional(TxType.REQUIRES_NEW)
	public OrderDTO updateOrder(String orderNumber, String status) {
		LOGGER.info("Updating order with ID: {} to status: {}", orderNumber, status);
		if (orderNumber == null) {
			LOGGER.error("Invalid order ID: {}", orderNumber);
			throw new OrderServiceException("Order ID must not be null");
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

	public OrderStatusDTO getOrderStatus(String orderId) {
		if (orderId == null || orderId.trim().isEmpty()) {
		LOGGER.error("Invalid order ID: {}", orderId);
		throw new OrderServiceException("Order ID cannot be null or empty");
		}
		LOGGER.info("Fetching status for order ID: {}", orderId);
		Order order = orderDAO.findByOrderNumber(orderId);
		if (order == null || order.getIsDeleted()) {
			LOGGER.info("No order found with ID: {}", orderId);
			return null; // Return null if no order is found
		}
		OrderStatusDTO statusDTO = new OrderStatusDTO();
		statusDTO.setOrderId(order.getOrderNumber());
		statusDTO.setStatus(order.getStatus().name());
		statusDTO.setMessage("Order status retrieved successfully");
		LOGGER.info("Status found for order ID: {}", orderId);
		return statusDTO;
	}

}
