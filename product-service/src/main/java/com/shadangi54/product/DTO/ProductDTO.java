package com.shadangi54.product.DTO;

import lombok.Data;

@Data
public class ProductDTO {
	
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
