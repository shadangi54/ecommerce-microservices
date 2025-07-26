package com.shadangi54.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDTO {
	
	private String orderId;
	private String status;
	private String message;
}
