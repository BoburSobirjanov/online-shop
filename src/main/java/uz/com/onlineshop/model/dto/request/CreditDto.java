package uz.com.onlineshop.model.dto.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreditDto {

    private Double interestRate;

    private Integer creditMonths;

    private Double hasBeenPaid;

    private Double mustBePaid;

    private String order;

}
