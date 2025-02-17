package uz.com.onlineshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.product.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {


    @Query("select p from products as p where p.isDeleted=false and p.id=?1")
    ProductEntity findProductEntityById(UUID id);


    @Query("select p from products as p where p.isDeleted=false and p.categoryId=?1")
    Page<ProductEntity> findProductEntityByCategoryId(Pageable pageable, UUID id);


    @Query("select p from products as p where p.isDeleted=false and p.categoryId=?1")
    List<ProductEntity> getProductEntityByCategoryId(UUID id);


    @Query("select p from products as p where p.isDeleted=false")
    Page<ProductEntity> findAllProducts(Pageable pageable);


    @Query("select p from products as p where p.isDeleted=false order by  p.viewCount desc")
    Page<ProductEntity> findAllByViewCount(Pageable pageable);


    @Query("select p from products as p where p.isDeleted=false order by p.price desc")
    Page<ProductEntity> findAllByPriceDesc(Pageable pageable);


    @Query("select p from products as p where p.isDeleted=false order by p.price asc ")
    Page<ProductEntity> findAllProductsByPriceAsc(Pageable pageable);


    @Query("select p from products as p where p.isSale>0 order by p.isSale asc")
    Page<ProductEntity> getAllProductsInSale(Pageable pageable);


    @Query("select p from products as p where p.isDeleted=false and p.id in ?1")
    Optional<List<ProductEntity>> findAllById(List<UUID> id);

}
