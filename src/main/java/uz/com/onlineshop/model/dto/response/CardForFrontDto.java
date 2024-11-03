package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.enums.CardType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardForFrontDto {

    private UUID id;

    private String cardNumber;

    private CardType cardType;

    private String expireDate;
}
