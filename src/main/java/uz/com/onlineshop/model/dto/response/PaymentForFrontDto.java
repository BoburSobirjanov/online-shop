package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.enums.PaymentMethod;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentForFrontDto {

    private UUID id;

    private OrderForFrontDto order;

    private UserForFrontDto user;

    private PaymentMethod paymentMethod;

    private Double amount;
}
