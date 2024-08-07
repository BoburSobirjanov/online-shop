package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.categories.Category;

import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category,UUID> {
    @Query("select u from categories as u where u.isDeleted=false and u.name=?1")
    Category findCategoryByName(String name);
}
