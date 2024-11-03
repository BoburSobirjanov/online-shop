package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.enums.OrderStatus;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderForFrontDto {

    private UUID id;

    private Double totalAmount;

    private OrderStatus orderStatus;

    private String shippingAddress;

    private String billingAddress;
}
