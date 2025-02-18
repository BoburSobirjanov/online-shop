package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFrontDto;
import uz.com.onlineshop.model.entity.product.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductForFrontDto toDto(ProductEntity product);

    ProductEntity toEntity(ProductDto request);
}
