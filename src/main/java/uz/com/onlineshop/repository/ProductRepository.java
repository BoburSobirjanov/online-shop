package uz.com.onlineshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.product.ProductEntity;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,UUID> {
    @Query("select u from products as u where u.isDeleted=false and u.id=?1")
    ProductEntity findProductEntityById(UUID id);
    @Query("select u from products as u where u.isDeleted=false")
    Page<ProductEntity> findAllProducts(Pageable pageable);
    @Query("select u from products as u where u.isDeleted=false and u.categoryId=?1")
    List<ProductEntity> findProductEntityByCategoryId(UUID id);
}
