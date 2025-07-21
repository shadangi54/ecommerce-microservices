package com.shadangi54.order.DTO;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

	private Long id;
	private String orderNumber;
	private String customerName;
	private String status;
	private List<OrderProductDTO> products;
	private Boolean isDeleted;
	private String createdAt;
	private String modifiedAt;
	private String createdBy;
	private String modifiedBy;
}
