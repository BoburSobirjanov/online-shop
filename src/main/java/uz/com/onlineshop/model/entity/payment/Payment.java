package uz.com.onlineshop.model.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime createdTime;

    private PaymentStatus paymentStatus;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private OrderEntity order;

    private Double amount;
}
