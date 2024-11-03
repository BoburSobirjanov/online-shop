package uz.com.onlineshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.ProductDto;
import uz.com.onlineshop.model.dto.response.ProductForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
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
    public ResponseEntity<StandardResponse<ProductForFrontDto>> save(
            @RequestBody ProductDto productDto
    ){
        return ResponseEntity.ok(productService.save(productDto));
    }


    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal)
    {
        return productService.delete(id, principal);
    }


    @GetMapping("/get-all-products")
    public Page<ProductForFrontDto> findAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }


    @GetMapping("/get-all-products-popular")
    public Page<ProductForFrontDto> findAllByViews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllByView(pageable);
    }


    @GetMapping("/get-all-products-asc")
    public Page<ProductForFrontDto> findAllByPriceAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllByPriceAsc(pageable);
    }


    @GetMapping("/get-all-products-desc")
    public Page<ProductForFrontDto> findAllByPriceDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllByPriceDesc(pageable);
    }


    @GetMapping("/get-by-category")
    public Page<ProductForFrontDto> getByCategory(
            @RequestParam UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return productService.getByCategory(pageable,id);
    }


    @GetMapping("/{id}/get-by-id")
    public StandardResponse<ProductForFrontDto> getById(
            @PathVariable UUID id,
            HttpServletRequest request
    ){
        productService.trackView(id, request);
        return productService.getById(id);
    }


    @PutMapping("/update-product/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<ProductForFrontDto> update(
            @PathVariable UUID id,
            @RequestBody ProductDto productDto,
            Principal principal
    ){
        return productService.update(id, productDto, principal);
    }


    @GetMapping("/get-products-in-sale")
    public Page<ProductForFrontDto> getAllProductsInSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsInSale(pageable);
    }


    @PutMapping("/{id}/set-sale")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<ProductForFrontDto> setSaleToProduct(
            @PathVariable UUID id,
            @RequestParam Integer sale,
            Principal principal
    ){
        return productService.setSaleToProduct(id, sale, principal);
    }




    @PutMapping("/{id}/remove-sale")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<ProductForFrontDto> removeSale(
            @PathVariable  UUID id,
            Principal principal
    ){
        return productService.removeSale(id, principal);
    }


    @DeleteMapping("/multi-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> multiDelete(
            @RequestBody List<String> id,
            Principal principal
    ){
        return productService.multiDeleteById(id, principal);
    }


}
