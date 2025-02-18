package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.CardDto;
import uz.com.onlineshop.model.dto.response.CardForFrontDto;
import uz.com.onlineshop.model.entity.card.CardEntity;


@Mapper(componentModel = "spring")
public interface CardMapper {

    CardForFrontDto toDto(CardEntity card);

    CardEntity toEntity(CardDto request);

}
