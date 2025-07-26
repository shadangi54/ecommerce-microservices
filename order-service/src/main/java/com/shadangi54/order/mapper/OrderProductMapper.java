package com.shadangi54.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.shadangi54.order.DTO.OrderProductDTO;
import com.shadangi54.order.entity.OrderProduct;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderProductMapper {
    
    // Convert OrderProduct entity to OrderProductDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    OrderProductDTO toDTO(OrderProduct orderProduct);
    
    // Convert OrderProductDTO to OrderProduct entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "order", ignore = true) // This needs to be set separately
    OrderProduct toEntity(OrderProductDTO orderProductDTO);
    
    // Convert list of OrderProduct entities to list of OrderProductDTOs
    List<OrderProductDTO> toDTOList(List<OrderProduct> orderProducts);
    
    // Convert list of OrderProductDTOs to list of OrderProduct entities
    List<OrderProduct> toEntityList(List<OrderProductDTO> orderProductDTOs);
}
