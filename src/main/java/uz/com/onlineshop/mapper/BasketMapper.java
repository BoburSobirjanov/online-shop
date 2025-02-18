package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.BasketDto;
import uz.com.onlineshop.model.dto.response.BasketForFrontDto;
import uz.com.onlineshop.model.entity.basket.Basket;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BasketForFrontDto toDto(Basket entity);

    Basket toEntity(BasketDto request);
}
