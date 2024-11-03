package uz.com.onlineshop.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.filter.IpAddressUtil;
import uz.com.onlineshop.mapper.ProductMapper;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFrontDto;
import uz.com.onlineshop.model.entity.categories.Category;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.CategoryRepository;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final Map<String, Long> ipProductViewMap = new HashMap<>();






    public void trackView(UUID productId, HttpServletRequest request) {
        String clientIp = IpAddressUtil.getClientIp(request);
        String key = clientIp + "_" + productId.toString();
        Long currentTime = System.currentTimeMillis();
        Long lastViewed = ipProductViewMap.get(key);
        if (lastViewed == null || (currentTime - lastViewed) > 3600000) {
            ipProductViewMap.put(key, currentTime);
            incrementViewCount(productId);
        }
    }





    public void incrementViewCount(UUID productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id " + productId));

        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }





    public StandardResponse<ProductForFrontDto> save(ProductDto productDto) {
        Category category = categoryRepository.findCategoryById(UUID.fromString(productDto.getCategoryId()));
        ProductEntity product = modelMapper.map(productDto, ProductEntity.class);
            product.setDescription(productDto.getDescription());
            product.setBrand(productDto.getBrand());
            product.setColor(productDto.getColor());
            product.setName(productDto.getName());
            product.setBarcode(productDto.getBarcode());
            product.setModel(productDto.getModel());
            product.setSku(productDto.getSku());
            product.setPrice(productDto.getPrice());
            product.setDimensions(productDto.getDimensions());
            product.setMaterial(productDto.getMaterial());
            if (category == null) {
                throw new NotAcceptableException("Category not found! Try again!");
            }
            product.setCategoryId(category.getId());
            product.setCountryOfOrigin(productDto.getCountryOfOrigin());
            product.setManufacturer(productDto.getManufacturer());
            product.setStock(productDto.getStock());
            product.setWarranty(productDto.getWarranty());
            product.setWeight(productDto.getWeight());
            product.setIsSale(0);
            product.setViewCount(0);
            ProductEntity save = productRepository.save(product);
            ProductForFrontDto productForFrontDto = modelMapper.map(save, ProductForFrontDto.class);
            return StandardResponse.ok("Product added!",productForFrontDto);
        }

        public StandardResponse<String> delete(UUID id, Principal principal){
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName());
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        productEntity.setDeleted(true);
        productEntity.setDeletedTime(LocalDateTime.now());
        productEntity.setDeletedBy(userEntity.getId());
        productRepository.save(productEntity);

        return StandardResponse.ok("Product has deleted successfully!","DELETED");
    }






    public Page<ProductForFrontDto> getAllProducts(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllProducts(pageable);
        return productEntities.map(productMapper::toDto);
    }








    public Page<ProductForFrontDto> getAllByView(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllByViewCount(pageable);
        return productEntities.map(productMapper::toDto);
    }









    public Page<ProductForFrontDto> getAllByPriceAsc(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllProductsByPriceAsc(pageable);
        return productEntities.map(productMapper::toDto);
    }








    public Page<ProductForFrontDto> getAllByPriceDesc(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllByPriceDesc(pageable);
        return productEntities.map(productMapper::toDto);
    }









    public Page<ProductForFrontDto> getByCategory(Pageable pageable, UUID id){
        Page<ProductEntity> productEntities = productRepository.findProductEntityByCategoryId(pageable, id);
        if (productEntities==null){
            throw new DataNotFoundException("Product not found same this category!");
        }
        return productEntities.map(productMapper::toDto);
    }





    public StandardResponse<ProductForFrontDto> getById(UUID id){
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        ProductForFrontDto product = modelMapper.map(productEntity, ProductForFrontDto.class);
        return StandardResponse.ok("This is product",product);
    }





    public StandardResponse<ProductForFrontDto> update(UUID id, ProductDto productDto, Principal principal){
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("product not found!");
        }
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
        productEntity.setIsSale(productEntity.getIsSale());
        productEntity.setWarranty(productDto.getWarranty());
        productEntity.setWeight(productDto.getWeight());
        productEntity.setUpdatedTime(LocalDateTime.now());
        productEntity.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        ProductEntity save = productRepository.save(productEntity);
        ProductForFrontDto productForFrontDto = modelMapper.map(save, ProductForFrontDto.class);

        return StandardResponse.ok("Product updated!",productForFrontDto);
    }






    public Page<ProductForFrontDto> getProductsInSale(Pageable pageable){
       Page<ProductEntity> productEntities = productRepository.getAllProductsInSale(pageable);
       if (productEntities==null){
           throw new DataNotFoundException("Products not found in sale❌");
       }
        return productEntities.map(productMapper::toDto);
    }




    public StandardResponse<ProductForFrontDto> setSaleToProduct(UUID id, Integer sale, Principal principal){
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        productEntity.setIsSale(sale);
        productEntity.setPrice(productEntity.getPrice()-(sale* productEntity.getPrice())/100);
        productEntity.setUpdatedTime(LocalDateTime.now());
        productEntity.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        ProductEntity save = productRepository.save(productEntity);
        ProductForFrontDto productForFrontDto = modelMapper.map(save, ProductForFrontDto.class);

        return StandardResponse.ok("Sale added in this product!",productForFrontDto);

    }



    public StandardResponse<ProductForFrontDto> removeSale(UUID id, Principal principal){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        productEntity.setPrice(100* productEntity.getPrice()/(100- productEntity.getIsSale()));
        productEntity.setIsSale(0);
        productEntity.setUpdatedTime(LocalDateTime.now());
        productEntity.setUpdatedBy(user.getId());
        ProductEntity save = productRepository.save(productEntity);
        ProductForFrontDto productForFrontDto = modelMapper.map(save, ProductForFrontDto.class);

        return StandardResponse.ok("Sale removed from product!",productForFrontDto);
    }



    public StandardResponse<String> multiDeleteById(List<String> id, Principal principal){
        Optional<List<ProductEntity>> productList = productRepository.findAllById(id
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()));

        if (productList.isEmpty()){
            throw new DataNotFoundException("Products not found!");
        }

        for (ProductEntity product: productList.get()) {
            product.setDeletedTime(LocalDateTime.now());
            product.setDeleted(true);
            product.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
            productRepository.save(product);
        }

        return StandardResponse.ok("Products has deleted successfully!","DELETED");
    }
}
