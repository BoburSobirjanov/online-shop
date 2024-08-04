package uz.com.onlineshop.model.review;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.com.onlineshop.model.BaseEntity;
import uz.com.onlineshop.model.user.UserEntity;
import uz.com.onlineshop.model.product.ProductEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "reviews")
public class ReviewsEntity extends BaseEntity {

    @ManyToOne
    private ProductEntity productId;

    @ManyToOne
    private UserEntity userId;

    private Integer rating;

    private String comment;

}
