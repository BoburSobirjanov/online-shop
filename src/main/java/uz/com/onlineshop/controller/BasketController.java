package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.onlineshop.model.dto.request.BasketDto;
import uz.com.onlineshop.model.dto.response.BasketForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.BasketService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/basket")
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/save")
    public StandardResponse<BasketForFrontDto> save(
            @RequestBody BasketDto basketDto,
            Principal principal){
        return basketService.save(basketDto, principal);
    }
}
