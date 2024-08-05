package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.entity.order.OrderStatus;
import uz.com.onlineshop.model.entity.order.PaymentMethod;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderForFront {
    private UUID id;

    private Double totalAmount;

    private PaymentMethod paymentMethod;

    private OrderStatus orderStatus;

    private String shippingAddress;

    private String billingAddress;
}
