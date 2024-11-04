package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.mapper.CreditMapper;
import uz.com.onlineshop.model.dto.request.CreditDto;
import uz.com.onlineshop.model.dto.response.CreditForFrontDto;
import uz.com.onlineshop.model.entity.card.CardEntity;
import uz.com.onlineshop.model.entity.credit.Credit;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.enums.CreditStatus;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.enums.OrderStatus;
import uz.com.onlineshop.repository.CardRepository;
import uz.com.onlineshop.repository.CreditRepository;
import uz.com.onlineshop.repository.OrderRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

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
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final CreditMapper creditMapper;



    public StandardResponse<CreditForFrontDto> save(CreditDto creditDto, Principal principal){
        OrderEntity order = orderRepository.findOrderEntityById(UUID.fromString(creditDto.getOrder()));
        if (order.getOrderStatus()== OrderStatus.PAID || order.getOrderStatus()==OrderStatus.CANCELLED){
            throw new UserBadRequestException("Can not create credit for this order. Because order has already paid or canceled!");
        }
        Double creditAmount = order.getTotalAmount() + (((order.getTotalAmount() * (100 + creditDto.getInterestRate())/100) - order.getTotalAmount()) * creditDto.getCreditMonths()/12);
        Credit credit =  modelMapper.map(creditDto, Credit.class);
        credit.setUser(userRepository.findUserEntityByEmail(principal.getName()));
        credit.setCreditMonths(creditDto.getCreditMonths());
        credit.setInterestRate(creditDto.getInterestRate());
        credit.setCreditStatus(CreditStatus.PROCESS);
        credit.setOrder(orderRepository.findOrderEntityById(UUID.fromString(creditDto.getOrder())));
        credit.setStartTime(LocalDateTime.now());
        credit.setMonthlyPayment(creditAmount/creditDto.getCreditMonths());
        credit.setEndTime(LocalDateTime.now().plusMonths(creditDto.getCreditMonths()));
        credit.setCreditAmount(creditAmount);
        credit.setHasBeenPaid(0.0);
        credit.setMustBePaid(creditAmount);
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
        Credit save = creditRepository.save(credit);

        CreditForFrontDto creditForFrontDto = modelMapper.map(save, CreditForFrontDto.class);

        return StandardResponse.ok("Credit saved!",creditForFrontDto);
    }





    public StandardResponse<CreditForFrontDto> getById(UUID id){
        Optional<Credit> credit = creditRepository.findCreditById(id);
        if (credit.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        CreditForFrontDto creditForFrontDto = modelMapper.map(credit, CreditForFrontDto.class);

        return StandardResponse.ok("This is credit!",creditForFrontDto);
    }





    public StandardResponse<String> deleteById(UUID id, Principal principal){
        Optional<Credit> credit = creditRepository.findCreditById(id);
        if (credit.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        if (credit.get().getCreditStatus()==CreditStatus.PROCESS){
            throw new NotAcceptableException("Can not delete this credit. Because credit has no completed yet!");
        }
        credit.get().setDeleted(true);
        credit.get().setDeletedTime(LocalDateTime.now());
        credit.get().setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        creditRepository.save(credit.get());

        return StandardResponse.ok("Credit deleted","DELETED");
    }



    public StandardResponse<CreditForFrontDto> payForCredit(UUID id, Double amount, String cardId){
        Optional<Credit> credit = creditRepository.findCreditById(id);
        if (credit.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        if (credit.get().getCreditStatus()==CreditStatus.COMPLETED){
            throw new UserBadRequestException("Can not pay for this order. Because order has already completed!");
        }
        CardEntity card = cardRepository.findCardEntityById(UUID.fromString(cardId));
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        if (card.getCardBalance()<amount){
            throw new NotAcceptableException("Not enough money! Try again");
        }
        if (amount>credit.get().getCreditAmount()){
            throw new UserBadRequestException("You must be pay only " + credit.get().getCreditAmount()+" UZS");
        }
        Credit creditEntity = credit.get();
        creditEntity.setCreditAmount(creditEntity.getCreditAmount()-amount);
        creditEntity.setHasBeenPaid(creditEntity.getHasBeenPaid() + amount);
        creditEntity.setMustBePaid(creditEntity.getCreditAmount() - creditEntity.getHasBeenPaid());
        Credit save = creditRepository.save(creditEntity);
        if (save.getCreditAmount()==0){
            save.setCreditStatus(CreditStatus.COMPLETED);
            creditRepository.save(save);
        }
        CreditForFrontDto creditForFrontDto = modelMapper.map(save, CreditForFrontDto.class);

        return StandardResponse.ok(amount + " UZS paid for credit!",creditForFrontDto);
    }





    public Page<CreditForFrontDto> findAll(Pageable pageable){
        Page<Credit> credits = creditRepository.findAllCredits(pageable);

        if (credits.isEmpty()){
            throw new DataNotFoundException("Credits not found!");
        }

        return credits.map(creditMapper::toDto);
    }






    public Page<CreditForFrontDto> getMyCredits(Principal principal,Pageable pageable){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        Page<Credit> credits = creditRepository.findAllByUser(user,pageable);
        if (credits.isEmpty()){
            throw new DataNotFoundException("Credits not found!");
        }
        return credits.map(creditMapper::toDto);
    }





    public Page<CreditForFrontDto> getUsersCredit(UUID id, Pageable pageable){
        UserEntity user = userRepository.findUserEntityById(id);
        Page<Credit> credits = creditRepository.findAllByUser(user,pageable);
        if (credits.isEmpty()){
            throw new DataNotFoundException("Credit not found!");
        }
        return credits.map(creditMapper::toDto);
    }
}
