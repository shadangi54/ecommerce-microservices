package com.shadangi54.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shadangi54.product.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Long>{
	
	/**
	 * Custom query to find products based on various criteria.
	 * 
	 * @param name     the name of the product (optional)
	 * @param category the category of the product (optional)
	 * @param minPrice the minimum price of the product (optional)
	 * @param maxPrice the maximum price of the product (optional)
	 * @param isActive whether the product is active (optional)
	 * @return a list of products matching the criteria
	 */
	@Query("SELECT p FROM Product p WHERE "
	         + "(:name IS NULL OR p.name LIKE %:name%) AND "
	         + "(:category IS NULL OR p.category = :category) AND "
	         + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
	         + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
	         + "(:isActive IS NULL OR p.isActive = :isActive)")
	    List<Product> find(@Param("name") String name,
	                       @Param("category") String category,
	                       @Param("minPrice") Double minPrice,
	                       @Param("maxPrice") Double maxPrice,
	                       @Param("isActive") Boolean isActive);
	
}
