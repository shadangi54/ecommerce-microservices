package com.shadangi54.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shadangi54.product.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Long>{
	
	@Query("SELECT p FROM Product p WHERE "
	         + "(:name IS NULL OR p.name LIKE %:name%) AND "
	         + "(:category IS NULL OR p.category = :category) AND "
	         + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
	         + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
	         + "(:isActive IS NULL OR p.isActive = :isActive) AND "
	         + "(:minStock IS NULL OR p.stockQuantity >= :minStock) AND "
	         + "(:maxStock IS NULL OR p.stockQuantity <= :maxStock)")
	    List<Product> find(@Param("name") String name,
	                       @Param("category") String category,
	                       @Param("minPrice") Double minPrice,
	                       @Param("maxPrice") Double maxPrice,
	                       @Param("isActive") Boolean isActive,
	                       @Param("minStock") Integer minStock,
	                       @Param("maxStock") Integer maxStock);
	
	@Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
	List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
