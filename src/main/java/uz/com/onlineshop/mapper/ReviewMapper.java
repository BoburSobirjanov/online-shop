package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.com.onlineshop.model.dto.request.ReviewDto;
import uz.com.onlineshop.model.dto.response.ReviewForFrontDto;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewForFrontDto toDto(ReviewsEntity reviews);

    ReviewsEntity toEntity(ReviewDto request);

    @Named("stringToUUID")
    static UUID stringToUUID(String productId) {
        return productId != null ? UUID.fromString(productId) : null;
    }

}
