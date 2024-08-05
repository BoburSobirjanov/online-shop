package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.orderItems.OrderItems;

import java.util.UUID;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,UUID> {
}
