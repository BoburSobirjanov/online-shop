package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.model.dto.request.CategoryDto;
import uz.com.onlineshop.model.dto.response.CategoryForFront;
import uz.com.onlineshop.model.entity.categories.Category;
import uz.com.onlineshop.repository.CategoryRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;






    public StandardResponse<CategoryForFront> save(CategoryDto categoryDto){
        checkHasCategory(categoryDto.getName());
        Category category =  modelMapper.map(categoryDto, Category.class);
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        CategoryForFront categoryForFront = modelMapper.map(category, CategoryForFront.class);
        return StandardResponse.<CategoryForFront>builder()
                .data(categoryForFront)
                .status(Status.SUCCESS)
                .message("Category added successfully!")
                .build();
    }







    private void checkHasCategory(String name) {
        Category category = categoryRepository.findCategoryByName(name);
        if (category!=null){
            throw new NotAcceptableException("Category has already added!");
        }
    }








    public StandardResponse<String> delete(UUID id, Principal principal){
        Category category = categoryRepository.findCategoryById(id);
        if (category==null){
            throw new DataNotFoundException("Category not found!");
        }
        category.setDeleted(true);
        category.setDeletedTime(LocalDateTime.now());
        category.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        categoryRepository.save(category);

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("Category deleted")
                .build();
    }








    public StandardResponse<CategoryForFront> getById(UUID id){
        Category category = categoryRepository.findCategoryById(id);
        if (category==null){
            throw new DataNotFoundException("Category not found!");
        }
        CategoryForFront categoryForFront = modelMapper.map(category, CategoryForFront.class);
        return StandardResponse.<CategoryForFront>builder()
                .data(categoryForFront)
                .status(Status.SUCCESS)
                .message("This is category!")
                .build();
    }







    public Page<CategoryForFront> getAllCategories(Pageable pageable){
        Page<Category> categories = categoryRepository.findAllCategories(pageable);
        return categories.map(category -> new CategoryForFront(category.getId(), category.getName(), category.getDescription()));
    }




    public StandardResponse<CategoryForFront> update(UUID id, CategoryDto categoryDto, Principal principal){
        Category category = categoryRepository.findCategoryById(id);
        if (category==null){
            throw new DataNotFoundException("Category not found!");
        }
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        category.setUpdatedTime(LocalDateTime.now());
        category.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        Category save = categoryRepository.save(category);
        CategoryForFront categoryForFront = modelMapper.map(save, CategoryForFront.class);

        return StandardResponse.<CategoryForFront>builder()
                .data(categoryForFront)
                .status(Status.SUCCESS)
                .message("Category updated!")
                .build();
    }
}
