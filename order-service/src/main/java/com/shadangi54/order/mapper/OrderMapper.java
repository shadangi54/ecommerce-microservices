package com.shadangi54.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadangi54.order.DTO.OrderDTO;
import com.shadangi54.order.DTO.OrderProductDTO;
import com.shadangi54.order.entity.Order;
import com.shadangi54.order.entity.OrderProduct;

@Mapper
public interface OrderMapper {
	
	@Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "modifiedBy", source = "modifiedBy")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "isDeleted", source = "isDeleted")
	@Mapping(target = "customerName", source = "customerName")
    OrderDTO toDTO(Order order);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "modifiedBy", source = "modifiedBy")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "isDeleted", source = "isDeleted")
    @Mapping(target = "customerName", source = "customerName")
    Order toEntity(OrderDTO orderDTO);

	List<OrderDTO> toDTOList(List<Order> orders);
	
	List<Order> toEntityList(List<OrderDTO> orderDTOs);
	
}
