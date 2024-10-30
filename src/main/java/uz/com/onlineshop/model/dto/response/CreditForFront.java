package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.entity.credit.CreditStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreditForFront {

    private UUID id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double creditAmount;

    private Double interestRate;

    private Integer creditMonths;

    private UserForFront user;

    private Double hasBeenPaid;

    private Double mustBePaid;

    private CreditStatus creditStatus;

    private OrderForFront order;

    private Double monthlyPayment;

}
