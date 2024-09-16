package uz.com.onlineshop.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.filter.ImageUtils;
import uz.com.onlineshop.filter.IpAddressUtil;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFront;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.CategoryRepository;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageUtils imageUtils;
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





    public StandardResponse<ProductForFront> save(MultipartFile imageProduct, ProductDto productDto) {
        ProductEntity product = modelMapper.map(productDto, ProductEntity.class);
        try {

            if (imageProduct == null) {
                product.setProductImage(null);
            } else {
                if (imageUtils.uploadImage(imageProduct)) {
                    System.out.println("Upload successfully");
                }
                product.setProductImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setDescription(productDto.getDescription());
            product.setBrand(productDto.getBrand());
            product.setColor(productDto.getColor());
            product.setName(productDto.getName());
            product.setBarcode(productDto.getBarcode());
            product.setModel(productDto.getModel());
            product.setSku(productDto.getSku());
            product.setPrice(Double.valueOf(productDto.getPrice()));
            product.setDimensions(productDto.getDimensions());
            product.setMaterial(productDto.getMaterial());
            if (categoryRepository.findCategoryById(UUID.fromString(productDto.getCategoryId())) == null) {
                throw new NotAcceptableException("Category not found! Try again!");
            }
            product.setCategoryId(UUID.fromString(productDto.getCategoryId()));
            product.setCountryOfOrigin(productDto.getCountryOfOrigin());
            product.setManufacturer(productDto.getManufacturer());
            product.setStock(Integer.valueOf(productDto.getStock()));
            product.setWarranty(productDto.getWarranty());
            product.setWeight(Double.valueOf(productDto.getWeight()));
            product.setIsSale(Integer.valueOf(productDto.getSale()));
            product.setViewCount(0);
            ProductEntity save = productRepository.save(product);
            ProductForFront productForFront = modelMapper.map(save, ProductForFront.class);
            return StandardResponse.<ProductForFront>builder()
                    .data(productForFront)
                    .status(Status.SUCCESS)
                    .message("Product added!")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("Product has deleted successfully!")
                .build();
    }






    public Page<ProductForFront> getAllProducts(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllProducts(pageable);
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }








    public Page<ProductForFront> getAllByView(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllByViewCount(pageable);
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }









    public Page<ProductForFront> getAllByPriceAsc(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllProductsByPriceAsc(pageable);
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }








    public Page<ProductForFront> getAllByPriceDesc(Pageable pageable){
        Page<ProductEntity> productEntities = productRepository.findAllByPriceDesc(pageable);
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }









    public Page<ProductForFront> getByCategory(Pageable pageable,UUID id){
        Page<ProductEntity> productEntities = productRepository.findProductEntityByCategoryId(pageable, id);
        if (productEntities==null){
            throw new DataNotFoundException("Product not found same this category!");
        }
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }





    public StandardResponse<ProductForFront> getById(UUID id){
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        ProductForFront product = modelMapper.map(productEntity, ProductForFront.class);
        return StandardResponse.<ProductForFront>builder()
                .data(product)
                .status(Status.SUCCESS)
                .message("this is product")
                .build();
    }





    public StandardResponse<ProductForFront> update(UUID id, ProductDto productDto, Principal principal){
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
        productEntity.setPrice(Double.valueOf(productDto.getPrice()));
        productEntity.setDimensions(productDto.getDimensions());
        productEntity.setMaterial(productDto.getMaterial());
        productEntity.setCategoryId(UUID.fromString(productDto.getCategoryId()));
        productEntity.setCountryOfOrigin(productDto.getCountryOfOrigin());
        productEntity.setManufacturer(productDto.getManufacturer());
        productEntity.setStock(Integer.valueOf(productDto.getStock()));
        productEntity.setIsSale(Integer.valueOf(productDto.getSale()));
        productEntity.setWarranty(productDto.getWarranty());
        productEntity.setWeight(Double.valueOf(productDto.getWeight()));
        productEntity.setUpdatedTime(LocalDateTime.now());
        productEntity.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        ProductEntity save = productRepository.save(productEntity);
        ProductForFront productForFront = modelMapper.map(save, ProductForFront.class);

        return StandardResponse.<ProductForFront>builder()
                .data(productForFront)
                .status(Status.SUCCESS)
                .message("Product updated")
                .build();
    }






    public Page<ProductForFront> getProductsInSale(Pageable pageable){
       Page<ProductEntity> productEntities = productRepository.getAllProductsInSale(pageable);
       if (productEntities==null){
           throw new DataNotFoundException("Products not found in sale❌");
       }
        return productEntities.map(product -> new ProductForFront(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(),
                product.getStock(), product.getBrand(),
                product.getModel(), product.getCategoryId(),
                product.getWeight(), product.getDimensions(),
                product.getColor(), product.getMaterial(),
                product.getWarranty(), product.getSku(),
                product.getBarcode(), product.getManufacturer(),
                product.getCountryOfOrigin(),
                product.getIsSale(), product.getProductImage()));
    }




    public StandardResponse<ProductForFront> setSaleToProduct(UUID id, Integer sale,Principal principal){
        ProductEntity productEntity = productRepository.findProductEntityById(id);
        if (productEntity==null){
            throw new DataNotFoundException("Product not found!");
        }
        productEntity.setIsSale(sale);
        productEntity.setPrice(productEntity.getPrice()-(sale* productEntity.getPrice())/100);
        productEntity.setUpdatedTime(LocalDateTime.now());
        productEntity.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        ProductEntity save = productRepository.save(productEntity);
        ProductForFront productForFront = modelMapper.map(save, ProductForFront.class);

        return StandardResponse.<ProductForFront>builder()
                .status(Status.SUCCESS)
                .data(productForFront)
                .message("Sale added in this product!")
                .build();

    }
}
