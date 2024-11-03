package uz.com.onlineshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.exception.RequestValidationException;
import uz.com.onlineshop.model.dto.request.CategoryDto;
import uz.com.onlineshop.model.dto.response.CategoryForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.CategoryService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;




    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public ResponseEntity<StandardResponse<CategoryForFrontDto>> save(
            @Valid
            @RequestBody CategoryDto categoryDto,
            BindingResult bindingResult
            )throws RequestValidationException {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }







    @DeleteMapping("/{id}/delete-by-id")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
            ){
        return categoryService.delete(id, principal);
    }







    @GetMapping("/get-by-id/{id}")
    public StandardResponse<CategoryForFrontDto> getById(
            @PathVariable UUID id
    ){
        return categoryService.getById(id);
    }






    @GetMapping("/get-all-categories")
    public Page<CategoryForFrontDto> getCategories(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getAllCategories(pageable);
    }





    @PutMapping("/update-category/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<CategoryForFrontDto> update(
            @PathVariable UUID id,
            @RequestBody CategoryDto categoryDto,
            Principal principal
    ){
        return categoryService.update(id, categoryDto, principal);
    }




    @DeleteMapping("/multi-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> multiDelete(
            @RequestBody List<String> id,
            Principal principal
    ){
        return categoryService.multiDeleteById(id, principal);
    }
}
