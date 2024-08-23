package uz.com.onlineshop.model.dto.response;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemForFront {

    private List<ProductForFront> productId;

    private Integer quantity;

    private Double price;
}
