package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.com.onlineshop.model.dto.request.CreditDto;
import uz.com.onlineshop.model.dto.response.CreditForFrontDto;
import uz.com.onlineshop.model.entity.credit.Credit;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    CreditForFrontDto toDto(Credit credit);

    Credit toEntity(CreditDto request);

    @Named("stringToUUID")
    static UUID stringToUUID(String orderId) {
        return orderId != null ? UUID.fromString(orderId) : null;
    }
}
