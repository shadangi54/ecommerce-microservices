package com.shadangi54.product.manager;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.shadangi54.product.DTO.ProductDTO;
import com.shadangi54.product.mapper.ProductMapper;
import com.shadangi54.product.repository.ProductDAO;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductManager.class);

	private ProductDAO productDAO;
	private ProductMapper productMapper;

	@Cacheable(value = "PRODUCT_LIST_CACHE")
	public List<ProductDTO> getAllProducts() {
		LOGGER.info("Fetching all products from the database");
		List<ProductDTO> products = productMapper.toDTOs(productDAO.findAll());
		return products;
	}

	@Cacheable(value = "PRODUCT_CACHE", key = "#id")
	public ProductDTO getProductById(Long id) {
		LOGGER.info("Fetching product with ID: {}", id);
		return productMapper.toDTO(productDAO.findById(id).orElse(null));
	}
	
	@Transactional(TxType.REQUIRES_NEW)
	@Caching(put = { @CachePut(value = "PRODUCT_CACHE", key = "#result.id")},
			evict = {@CacheEvict(value = "PRODUCT_LIST_CACHE", allEntries = true) })
	public ProductDTO createProduct(ProductDTO productDTO) {
		LOGGER.info("Creating new product: {}", productDTO);
		if (productDTO.getId() == null) {
			productDTO.setCreatedBy("system");
			productDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		}
		productDTO.setModifiedBy("system");
		productDTO.setModifiedAt(new Timestamp(System.currentTimeMillis()));
		ProductDTO savedProduct = productMapper.toDTO(productDAO.save(productMapper.toEntity(productDTO)));
		LOGGER.info("Product created successfully with ID: {}", savedProduct.getId());
		return savedProduct;
	}
	
	
	@Transactional(TxType.REQUIRES_NEW)
	@Caching(evict = { @CacheEvict(value = "PRODUCT_CACHE", key = "#id"),
			@CacheEvict(value = "PRODUCT_LIST_CACHE", allEntries = true) })
	public void deleteProduct(Long id) {
		LOGGER.info("Deleting product with ID: {}", id);
		productDAO.deleteById(id);
	}

	public List<ProductDTO> searchProducts(String name, String category, Double minPrice, Double maxPrice,
			Boolean isActive) {
		LOGGER.info(
				"Searching products with parameters - Name: {}, Category: {}, Min Price: {}, Max Price: {}, Is Active: {}, Min Stock: {}, Max Stock: {}",
				name, category, minPrice, maxPrice, isActive);
		List<ProductDTO> products = productMapper
				.toDTOs(productDAO.find(name, category, minPrice, maxPrice, isActive));
		return products;
	}


}
