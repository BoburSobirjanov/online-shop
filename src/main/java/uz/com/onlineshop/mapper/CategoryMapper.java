package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.CategoryForFrontDto;
import uz.com.onlineshop.model.entity.categories.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryForFrontDto>{

    CategoryForFrontDto toDto(Category category);
}
