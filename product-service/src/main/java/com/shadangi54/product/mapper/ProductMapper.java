package com.shadangi54.product.mapper;

import org.mapstruct.Mapper;

import com.shadangi54.product.DTO.ProductDTO;
import com.shadangi54.product.entity.Product;

@Mapper
public interface ProductMapper {
	
	ProductDTO toDTO(Product product);

	Product toEntity(ProductDTO productDTO);

	Iterable<ProductDTO> toDTOs(Iterable<Product> products);

	Iterable<Product> toEntities(Iterable<ProductDTO> productDTOs);
}
