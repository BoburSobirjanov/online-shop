package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.OrderForFrontDto;
import uz.com.onlineshop.model.entity.order.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderEntity, OrderForFrontDto>{

    OrderForFrontDto toDto(OrderEntity order);

}
