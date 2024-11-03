package uz.com.onlineshop.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewForFrontDto {

    private ProductForFrontDto productId;

    private UserForFrontDto userId;

    private Integer rating;

    private String comment;
}
