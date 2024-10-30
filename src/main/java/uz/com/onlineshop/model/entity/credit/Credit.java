package uz.com.onlineshop.model.entity.credit;

import jakarta.persistence.*;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.time.LocalDateTime;

@Entity(name = "credit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Credit extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Double creditAmount;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Integer creditMonths;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(nullable = false)
    private Double hasBeenPaid;

    @Column(nullable = false)
    private Double mustBePaid;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @OneToOne
    private OrderEntity order;

    private Double monthlyPayment;
}
