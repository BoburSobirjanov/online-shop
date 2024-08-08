package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.CategoryDto;
import uz.com.onlineshop.model.dto.response.CategoryForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.CategoryService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<CategoryForFront> save(
            @RequestBody CategoryDto categoryDto
            ){
        return categoryService.save(categoryDto);
    }

    @DeleteMapping("/{id}/delete-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
            ){
        return categoryService.delete(id, principal);
    }
}
