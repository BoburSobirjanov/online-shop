package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFront;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public StandardResponse<ProductForFront> save(ProductDto productDto){
        List<ProductEntity> productEntities = productRepository.findAllProductEntity();
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setDescription(productDto.getDescription());
        productEntity.setBrand(productDto.getBrand());
        productEntity.setColor(productDto.getColor());
        productEntity.setName(productDto.getName());
        productEntity.setBarcode(productDto.getBarcode());
        productEntity.setModel(productDto.getModel());
        productEntity.setSku(productDto.getSku());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setDimensions(productDto.getDimensions());
        productEntity.setMaterial(productDto.getMaterial());
        productEntity.setCategoryId(UUID.fromString(productDto.getCategoryId()));
        productEntity.setCountryOfOrigin(productDto.getCountryOfOrigin());
        productEntity.setManufacturer(productDto.getManufacturer());
        productEntity.setStock(productDto.getStock());
        productEntity.setWarranty(productDto.getWarranty());
        productEntity.setWeight(productDto.getWeight());
        for (ProductEntity product:productEntities) {
            if (product.equals(productEntity)){
                throw new NotAcceptableException("Wrong! This product has already been added, you can only change the quantity.");
            }
        }
        ProductEntity save = productRepository.save(productEntity);
        ProductForFront productForFront = modelMapper.map(save, ProductForFront.class);
        return StandardResponse.<ProductForFront>builder()
                .data(productForFront)
                .status(Status.SUCCESS)
                .message("Product added!")
                .build();
    }
}
