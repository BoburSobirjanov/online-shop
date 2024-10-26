package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.basket.Basket;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID> {

    @Query("select u from basket as u where u.id=?1")
    Optional<Basket> findBasketById(UUID id);
}
