package uz.com.onlineshop.model.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BasketDto {

    private List<String> productEntities;
}
