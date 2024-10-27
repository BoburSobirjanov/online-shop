package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.entity.payment.PaymentMethod;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentForFront {

    private UUID id;

    private OrderForFront order;

    private UserForFront user;

    private PaymentMethod paymentMethod;

    private Double amount;
}
