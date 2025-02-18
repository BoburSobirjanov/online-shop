package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.onlineshop.mapper.BasketMapper;
import uz.com.onlineshop.model.dto.request.BasketDto;
import uz.com.onlineshop.model.dto.response.BasketForFrontDto;
import uz.com.onlineshop.model.entity.basket.Basket;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.repository.BasketRepository;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Transactional
    public StandardResponse<BasketForFrontDto> save(BasketDto basketDto, Principal principal) {
        List<ProductEntity> productEntities = new ArrayList<>();
        List<String> products = basketDto.getProductEntities();
        for (String id : products) {
            ProductEntity product = productRepository.findProductEntityById(UUID.fromString(id));
            productEntities.add(product);
        }
        Basket basket = basketMapper.toEntity(basketDto);
        basket.setProduct(productEntities);
        basket.setUser(userRepository.findUserEntityByEmail(principal.getName()));
        Basket save = basketRepository.save(basket);
        BasketForFrontDto basketForFrontDto =basketMapper.toDto(save);

        return StandardResponse.ok("Basket created", basketForFrontDto);
    }


}
