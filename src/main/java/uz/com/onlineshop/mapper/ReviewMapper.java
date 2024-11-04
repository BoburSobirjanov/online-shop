package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.ReviewForFrontDto;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewsEntity, ReviewForFrontDto>{

    ReviewForFrontDto toDto(ReviewsEntity reviews);

}
