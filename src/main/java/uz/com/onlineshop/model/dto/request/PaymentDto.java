package uz.com.onlineshop.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto {

    private String orderId;

    private String cardId;
}
