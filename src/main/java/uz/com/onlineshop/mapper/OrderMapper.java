package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.OrderDto;
import uz.com.onlineshop.model.dto.response.OrderForFrontDto;
import uz.com.onlineshop.model.entity.order.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderForFrontDto toDto(OrderEntity order);

    OrderEntity toEntity(OrderDto request);

}
