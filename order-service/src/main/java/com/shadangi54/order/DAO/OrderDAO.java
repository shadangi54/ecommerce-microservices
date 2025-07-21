package com.shadangi54.order.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shadangi54.order.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Long>{
	
	/**
	 * Retrieves all orders for a given customer.
	 *
	 * @param customerName the name of the customer
	 * @return a list of orders associated with the customer
	 */
	@Query("SELECT o FROM Order o WHERE o.customerName = :customerName")
	List<Order> getAllOrdersForCustomer(@Param("customerName") String customerName);

	/**
	 * Finds an order by its order number.
	 *
	 * @param orderNumber the order number of the order to find
	 * @return the order with the specified order number, or null if not found
	 */
	@Query("SELECT o FROM Order o WHERE o.orderNumber = :orderNumber")
	Order findByOrderNumber(String orderNumber);


}
