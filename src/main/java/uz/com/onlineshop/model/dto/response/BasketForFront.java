package uz.com.onlineshop.model.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BasketForFront {

    private UUID id;

    private List<ProductForFront> productEntities;
}
