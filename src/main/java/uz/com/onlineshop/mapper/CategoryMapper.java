package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.CategoryDto;
import uz.com.onlineshop.model.dto.response.CategoryForFrontDto;
import uz.com.onlineshop.model.entity.categories.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryForFrontDto toDto(Category category);

    Category toEntity(CategoryDto request);
}
