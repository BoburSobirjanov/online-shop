package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFront;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.ProductService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


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





    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
            ){
        return productService.delete(id, principal);
    }





    @GetMapping("/get-all-products")
    public Page<ProductEntity> getAll(
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size
    ){
        return productService.getAll(page, size);
    }





    @GetMapping("/get-by-category")
    public List<ProductEntity> getByCategory(
            @RequestParam UUID id
    ){
        return productService.getByCategory(id);
    }




    @GetMapping("/{id}/get-by-id")
    public StandardResponse<ProductForFront> getById(
            @PathVariable UUID id
    ){
        return productService.getById(id);
    }




    @PutMapping("/update-product/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<ProductForFront> update(
            @PathVariable UUID id,
            @RequestBody ProductDto productDto,
            Principal principal
    ){
        return productService.update(id, productDto, principal);
    }
}
