package com.shadangi54.product.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private Double price;
	private String category;
	private String imageUrl;
	private Integer stockQuantity;
	private Boolean isActive;
	private String createdBy;
	private String modifiedBy;
	private String createdAt;
	private String modifiedAt;
}
