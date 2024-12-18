package uz.com.onlineshop.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {

    private String shippingAddress;

    private String billingAddress;

    private String basketId;
}
