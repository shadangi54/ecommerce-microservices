package com.shadangi54.order.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
	private String skuCode;
	private Integer quantity;
	private Boolean isInStock;
}
