package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.enums.CreditStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreditForFrontDto {

    private UUID id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double creditAmount;

    private Double interestRate;

    private Integer creditMonths;

    private UserForFrontDto user;

    private Double hasBeenPaid;

    private Double mustBePaid;

    private CreditStatus creditStatus;

    private OrderForFrontDto order;

    private Double monthlyPayment;

}
