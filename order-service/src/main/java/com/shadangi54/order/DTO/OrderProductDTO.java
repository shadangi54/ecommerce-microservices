package com.shadangi54.order.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderProductDTO {

    private Long id;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;	
}
