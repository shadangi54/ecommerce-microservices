package com.shadangi54.product.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.shadangi54.product.DTO.ProductDTO;
import com.shadangi54.product.entity.Product;

@Mapper
public interface ProductMapper {
	
	ProductDTO toDTO(Product product);

	Product toEntity(ProductDTO productDTO);

	List<ProductDTO> toDTOs(List<Product> products);

	List<Product> toEntities(List<ProductDTO> productDTOs);
}
