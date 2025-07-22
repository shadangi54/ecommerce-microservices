package com.shadangi54.inventory.entity;

import java.util.Date;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "inventory")
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
public class Inventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "sku_code", nullable = false, unique = true, length = 100)
	private String skuCode;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
	
	@Column(name = "created_by", length = 50)
	private String createdBy;
	
	@Column(name = "modified_by", length = 50)
	private String modifiedBy;
	
	@Column(name = "created_date", nullable = false)
	private Date createdDate;
	
	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;
}
