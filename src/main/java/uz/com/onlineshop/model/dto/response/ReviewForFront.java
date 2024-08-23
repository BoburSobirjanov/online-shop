package uz.com.onlineshop.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewForFront {

    private ProductForFront productId;

    private UserForFront userId;

    private Integer rating;

    private String comment;
}
