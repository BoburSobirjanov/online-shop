package uz.com.onlineshop.model.entity.card;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "cards")
public class CardEntity extends BaseEntity {


    @Column(nullable = false, unique = true)
    private String cardNumber;


    @Enumerated(EnumType.STRING)
    private CardType cardType;


    private Double cardBalance;

    @ManyToOne
    private UserEntity userId;

}
