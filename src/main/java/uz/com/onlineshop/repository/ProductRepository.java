package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.product.ProductEntity;

import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,UUID> {
}
