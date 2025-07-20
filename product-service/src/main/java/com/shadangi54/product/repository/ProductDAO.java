package com.shadangi54.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadangi54.product.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Long>{
	
}
