package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.model.dto.request.CreditDto;
import uz.com.onlineshop.model.dto.response.CreditForFront;
import uz.com.onlineshop.model.entity.credit.Credit;
import uz.com.onlineshop.model.entity.credit.CreditStatus;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.repository.CreditRepository;
import uz.com.onlineshop.repository.OrderRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    public StandardResponse<CreditForFront> save(CreditDto creditDto, Principal principal){
        OrderEntity order = orderRepository.findOrderEntityById(UUID.fromString(creditDto.getOrder()));
        Double creditAmount = order.getTotalAmount() + (((order.getTotalAmount() * (100 + creditDto.getInterestRate())/100) - order.getTotalAmount()) * creditDto.getCreditMonths()/12);
        Credit credit =  modelMapper.map(creditDto, Credit.class);
        credit.setUser(userRepository.findUserEntityByEmail(principal.getName()));
        credit.setCreditMonths(creditDto.getCreditMonths());
        credit.setInterestRate(creditDto.getInterestRate());
        credit.setCreditStatus(CreditStatus.PROCESS);
        credit.setOrder(orderRepository.findOrderEntityById(UUID.fromString(creditDto.getOrder())));
        credit.setStartTime(LocalDateTime.now());
        credit.setEndTime(LocalDateTime.now().plusMonths(creditDto.getCreditMonths()));
        credit.setCreditAmount(creditAmount);
        credit.setHasBeenPaid(0.0);
        credit.setMustBePaid(creditAmount);
        Credit save = creditRepository.save(credit);

        CreditForFront creditForFront = modelMapper.map(save, CreditForFront.class);

        return StandardResponse.<CreditForFront>builder()
                .status(Status.SUCCESS)
                .message("Credit created!")
                .data(creditForFront)
                .build();
    }





    public StandardResponse<CreditForFront> getById(UUID id){
        Optional<Credit> credit = creditRepository.findCreditById(id);
        if (credit.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        CreditForFront creditForFront = modelMapper.map(credit, CreditForFront.class);

        return StandardResponse.<CreditForFront>builder()
                .status(Status.SUCCESS)
                .message("This is credit!")
                .data(creditForFront)
                .build();
    }





    public StandardResponse<String> deleteById(UUID id, Principal principal){
        Optional<Credit> credit = creditRepository.findCreditById(id);
        if (credit.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        credit.get().setDeleted(true);
        credit.get().setDeletedTime(LocalDateTime.now());
        credit.get().setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        creditRepository.save(credit.get());

        return StandardResponse.<String>builder()
                .status(Status.SUCCESS)
                .message("Credit deleted!")
                .data("DELETED")
                .build();
    }
}
