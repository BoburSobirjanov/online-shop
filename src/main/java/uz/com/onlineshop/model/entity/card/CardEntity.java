package uz.com.onlineshop.model.entity.card;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.enums.CardType;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "cards")
public class CardEntity extends BaseEntity {


    @Column(nullable = false, unique = true, length = 16)
    @Size(max = 16, message = "Field value cannot exceed 16 characters")
    private String cardNumber;


    @Enumerated(EnumType.STRING)
    private CardType cardType;


    @Column(nullable = false)
    private String expireDate;


    @Column(nullable = false)
    private Double cardBalance;

    @ManyToOne
    private UserEntity userId;

}
