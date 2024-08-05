package uz.com.onlineshop.model.entity.orderItems;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.product.ProductEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "order_items")
public class OrderItems extends BaseEntity {

    @ManyToOne
    private OrderEntity order;

    @ManyToMany
    private List<ProductEntity> productId;

    private Integer quantity;

    private Double price;
}
