package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.mapper.CategoryMapper;
import uz.com.onlineshop.model.dto.request.CategoryDto;
import uz.com.onlineshop.model.dto.response.CategoryForFrontDto;
import uz.com.onlineshop.model.entity.categories.Category;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.repository.CategoryRepository;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;


    public StandardResponse<CategoryForFrontDto> save(CategoryDto categoryDto) {
        checkHasCategory(categoryDto.getName());
        Category category = categoryMapper.toEntity(categoryDto);
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        CategoryForFrontDto categoryForFrontDto = categoryMapper.toDto(category);
        return StandardResponse.ok("Category added successfully!", categoryForFrontDto);
    }


    private void checkHasCategory(String name) {
        Category category = categoryRepository.findCategoryByName(name);
        if (category != null) {
            throw new NotAcceptableException("Category has already added!");
        }
    }


    public StandardResponse<String> delete(UUID id, Principal principal) {
        Category category = categoryRepository.findCategoryById(id);
        if (category == null) {
            throw new DataNotFoundException("Category not found!");
        }
        List<ProductEntity> productEntityList = productRepository.getProductEntityByCategoryId(id);
        for (ProductEntity product : productEntityList) {
            if (product.getCategoryId().equals(category.getId())) {
                throw new NotAcceptableException("Can not delete this category. Because it has product.");
            }
        }
        category.setDeleted(true);
        category.setDeletedTime(LocalDateTime.now());
        category.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        categoryRepository.save(category);

        return StandardResponse.ok("Category deleted", "DELETED");
    }


    public StandardResponse<CategoryForFrontDto> getById(UUID id) {
        Category category = categoryRepository.findCategoryById(id);
        if (category == null) {
            throw new DataNotFoundException("Category not found!");
        }
        CategoryForFrontDto categoryForFrontDto =categoryMapper.toDto(category);
        return StandardResponse.ok("This is category!", categoryForFrontDto);
    }


    public Page<CategoryForFrontDto> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllCategories(pageable);
        return categories.map(categoryMapper::toDto);
    }


    public StandardResponse<CategoryForFrontDto> update(UUID id, CategoryDto categoryDto, Principal principal) {
        Category category = categoryRepository.findCategoryById(id);
        if (category == null) {
            throw new DataNotFoundException("Category not found!");
        }
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        category.setUpdatedTime(LocalDateTime.now());
        category.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        Category save = categoryRepository.save(category);
        CategoryForFrontDto categoryForFrontDto = categoryMapper.toDto(save);

        return StandardResponse.ok("Category updated!", categoryForFrontDto);
    }


    public StandardResponse<String> multiDeleteById(List<String> id, Principal principal) {
        List<Category> categoryList = categoryRepository.findAllById(id
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()));

        if (categoryList.isEmpty()) {
            throw new DataNotFoundException("Category not found!");
        }

        for (Category category : categoryList) {
            category.setDeletedTime(LocalDateTime.now());
            category.setDeleted(true);
            category.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
            categoryRepository.save(category);
        }

        return StandardResponse.ok("Categories deleted", "DELETED");
    }
}
