package uz.com.onlineshop.model.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.enums.PaymentMethod;
import uz.com.onlineshop.model.enums.PaymentStatus;


@Entity(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private OrderEntity order;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
