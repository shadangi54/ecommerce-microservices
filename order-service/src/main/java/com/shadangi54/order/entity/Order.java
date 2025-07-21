package com.shadangi54.order.entity;

import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.shadangi54.order.DTO.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@SQLRestriction("is_deleted = false")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "order_number", nullable = false, unique = true)
	private String orderNumber;

	@Column(name = "customer_name", nullable = false)
	private String customerName;

	@Enumerated( value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status; // e.g., "active", "completed", "cancelled"
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false; // Soft delete flag
	
	@Column(name = "created_date")
	private String createdAt;
	
	@Column(name = "updated_date")
	private String modifiedAt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String modifiedBy;

}
