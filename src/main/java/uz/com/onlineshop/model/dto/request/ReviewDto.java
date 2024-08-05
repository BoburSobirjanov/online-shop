package uz.com.onlineshop.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDto {

    private Integer rating;

    private String comment;
}
