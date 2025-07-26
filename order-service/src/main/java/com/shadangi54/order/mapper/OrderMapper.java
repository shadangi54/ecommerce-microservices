package com.shadangi54.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.shadangi54.order.DTO.OrderDTO;
import com.shadangi54.order.entity.Order;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
	
    // Convert Order entity to OrderDTO
    @Mapping(target = "products", source = "products")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    OrderDTO toDTO(Order order);

    // Convert OrderDTO to Order entity
    @Mapping(target = "products", ignore = true) // Handle this separately in service layer
    @Mapping(target = "status", expression = "java(com.shadangi54.order.DTO.OrderStatus.fromString(orderDTO.getStatus()))")
    Order toEntity(OrderDTO orderDTO);

    // Convert list of Order entities to list of OrderDTOs
	List<OrderDTO> toDTOList(List<Order> orders);
	
    // Convert list of OrderDTOs to list of Order entities
	List<Order> toEntityList(List<OrderDTO> orderDTOs);
}
