package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.CardForFrontDto;
import uz.com.onlineshop.model.entity.card.CardEntity;


@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardEntity, CardForFrontDto>{

    CardForFrontDto toDto(CardEntity card);

}
