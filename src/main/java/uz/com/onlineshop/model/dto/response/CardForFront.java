package uz.com.onlineshop.model.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardForFront {

    private UUID id;

    private String cardNumber;

    private String cardType;
}
