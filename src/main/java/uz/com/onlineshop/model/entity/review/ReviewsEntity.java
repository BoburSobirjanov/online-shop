package uz.com.onlineshop.model.entity.review;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.entity.product.ProductEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "reviews")
public class ReviewsEntity extends BaseEntity {

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    private UserEntity user;

    private Integer rating;

    private String comment;

}
