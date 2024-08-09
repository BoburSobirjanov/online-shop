package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<ProductForFront> save(
            @RequestBody ProductDto productDto
            ){
        return productService.save(productDto);
    }
}
