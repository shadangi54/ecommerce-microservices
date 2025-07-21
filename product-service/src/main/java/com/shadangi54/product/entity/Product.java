package com.shadangi54.product.entity;

import java.io.Serializable;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
@SQLRestriction("")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "price", nullable = false)
	private Double price;

	@Column(name = "category", length = 50)
	private String category;

	@Column(name = "image_url", length = 255)
	private String imageUrl;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Column(name = "modified_by", length = 50)
	private String modifiedBy;

	@Column(name = "created_date", nullable = false)
	private String createdAt;

	@Column(name = "updated_date", nullable = false)
	private String modifiedAt;
}
