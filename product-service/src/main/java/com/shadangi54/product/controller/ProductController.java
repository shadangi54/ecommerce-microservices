package com.shadangi54.product.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	private RedisTemplate<String, Object> redisTemplate;
	
	@GetMapping("/clear-cache")
	public void clearCache() {
		redisTemplate.getConnectionFactory().getConnection().flushDb();
	}

	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		LOGGER.info("Fetching all products");
		try {
			List<ProductDTO> products = productManager.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			LOGGER.error("Error fetching products", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		LOGGER.info("Fetching product with ID: {}", id);
		try {
			ProductDTO product = productManager.getProductById(id);
			if (product != null) {
				return ResponseEntity.ok(product);
			} else {
				LOGGER.warn("Product with ID: {} not found", id);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			LOGGER.error("Error fetching product with ID: {}", id, e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
		LOGGER.info("Creating new product: {}", productDTO);
		try {
			productDTO.setCreatedBy("system");
			productDTO.setModifiedBy("system");
			productDTO.setCreatedAt(String.valueOf(System.currentTimeMillis()));
			productDTO.setModifiedAt(String.valueOf(System.currentTimeMillis()));
			productManager.createProduct(productDTO);
			return ResponseEntity.status(201).body("Product created successfully");
		} catch (Exception e) {
			LOGGER.error("Error creating product", e);
			return ResponseEntity.status(500).body("Error creating product");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
		LOGGER.info("Updating product with ID: {}", id);
		try {
			ProductDTO existingProduct = productManager.getProductById(id);
			if (existingProduct == null) {
				LOGGER.warn("Product with ID: {} not found", id);
				return ResponseEntity.notFound().build();
			}
			productDTO.setId(id); // Ensure the ID is set for the update
			productDTO.setModifiedBy("system");
			productDTO.setModifiedAt(String.valueOf(System.currentTimeMillis()));
			productManager.createProduct(productDTO); // Reuse create method for simplicity
			return ResponseEntity.ok("Product updated successfully");
		} catch (Exception e) {
			LOGGER.error("Error updating product with ID: {}", id, e);
			return ResponseEntity.status(500).body("Error updating product");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		LOGGER.info("Deleting product with ID: {}", id);
		try {
			ProductDTO existingProduct = productManager.getProductById(id);
			if (existingProduct == null) {
				LOGGER.warn("Product with ID: {} not found", id);
				return ResponseEntity.notFound().build();
			}
			productManager.deleteProduct(id); // Assuming deleteProduct method exists in ProductManager
			return ResponseEntity.ok("Product deleted successfully");
		} catch (Exception e) {
			LOGGER.error("Error deleting product with ID: {}", id, e);
			return ResponseEntity.status(500).body("Error deleting product");
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam(required = false) String name,
			@RequestParam(required = false) String category, @RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Integer minStock, @RequestParam(required = false) Integer maxStock) {
		LOGGER.info("Searching products by name: {}, category: {}, price range: {}-{}, active: {}, stock range: {}-{}",
				name, category, minPrice, maxPrice, isActive, minStock, maxStock);
		try {
			List<ProductDTO> products = productManager.searchProducts(name, category, minPrice, maxPrice, isActive,
					minStock, maxStock);
			if (products.isEmpty()) {
				LOGGER.warn("No products found matching the search criteria");
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			LOGGER.error("Error searching products", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/low-stock")
	public ResponseEntity<List<ProductDTO>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
		LOGGER.info("Fetching products with stock below threshold: {}", threshold);
		try {
			List<ProductDTO> lowStockProducts = productManager.getLowStockProducts(threshold);
			if (lowStockProducts.isEmpty()) {
				LOGGER.warn("No low stock products found");
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(lowStockProducts);
		} catch (Exception e) {
			LOGGER.error("Error fetching low stock products", e);
			return ResponseEntity.status(500).body(null);
		}
	}
}
