package uz.com.onlineshop.model.dto.response;


import lombok.*;
import uz.com.onlineshop.model.entity.product.ProductEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {

    private List<ProductEntity> productId;

    private Integer quantity;

    private Double price;
}
