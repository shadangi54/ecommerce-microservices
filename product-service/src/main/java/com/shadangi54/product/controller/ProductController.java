package com.shadangi54.product.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.product.DTO.ProductDTO;
import com.shadangi54.product.manager.ProductManager;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	private ProductManager productManager;
	
	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAllProducts(){
		LOGGER.info("Fetching all products");
		try {
			List<ProductDTO> products = productManager.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			LOGGER.error("Error fetching products", e);
			return ResponseEntity.status(500).body(null);
		}
	}
	
}
