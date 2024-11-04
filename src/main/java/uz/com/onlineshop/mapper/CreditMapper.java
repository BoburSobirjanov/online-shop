package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.CreditForFrontDto;
import uz.com.onlineshop.model.entity.credit.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper extends EntityMapper<Credit, CreditForFrontDto>{
    CreditForFrontDto toDto(Credit credit);
}
