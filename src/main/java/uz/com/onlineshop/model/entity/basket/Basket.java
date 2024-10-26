package uz.com.onlineshop.model.entity.basket;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.util.List;
import java.util.UUID;

@Entity(name = "basket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany
    private List<ProductEntity> product;

    @OneToOne
    private UserEntity user;
}
