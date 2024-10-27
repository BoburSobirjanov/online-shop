package uz.com.onlineshop.model.entity.order;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    private UserEntity userId;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String shippingAddress;

    private String billingAddress;

    @ManyToMany
    private List<ProductEntity> productEntities;

}
