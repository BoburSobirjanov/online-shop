package uz.com.onlineshop.model.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.com.onlineshop.model.entity.order.OrderStatus;
import uz.com.onlineshop.model.entity.order.PaymentMethod;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {

    private Double totalAmount;

    private String paymentMethod;

    private String shippingAddress;

    private String billingAddress;
}
