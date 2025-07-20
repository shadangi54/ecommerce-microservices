package com.shadangi54.product.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shadangi54.product.DTO.ProductDTO;
import com.shadangi54.product.mapper.ProductMapper;
import com.shadangi54.product.repository.ProductDAO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductManager.class);
	
	private ProductDAO productDAO;
	private ProductMapper productMapper;
	
	public List<ProductDTO> getAllProducts() {
		LOGGER.info("Fetching all products from the database");
		try {
			List<ProductDTO> products = (List<ProductDTO>) productMapper.toDTOs(productDAO.findAll());
			LOGGER.info("Successfully fetched {} products", products.size());
			return products;
		} catch (Exception e) {
			LOGGER.error("Error fetching products from the database", e);
			throw new RuntimeException("Failed to fetch products", e);
		}
	}

}
