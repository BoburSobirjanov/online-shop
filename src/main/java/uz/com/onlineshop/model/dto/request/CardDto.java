package uz.com.onlineshop.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {

    private String cardNumber;

    private String cardType;

    private Double cardBalance;
}
