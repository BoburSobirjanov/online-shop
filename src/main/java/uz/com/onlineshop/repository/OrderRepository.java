package uz.com.onlineshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,UUID> {


    @Query("select u from orders as u where u.isDeleted=false and u.id=?1")
    OrderEntity findOrderEntityById(UUID id);




    @Query("select u from orders as u where u.isDeleted=false and  u.orderStatus='CANCELLED'")
    Page<OrderEntity> findOrderEntityByOrderStatus(Pageable pageable);




    @Query("select u from orders as u where u.isDeleted=false and u.userId=?1")
    Page<OrderEntity> findOrderEntityByUserId(Pageable pageable, UserEntity user);
}
