package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.model.dto.request.PaymentDto;
import uz.com.onlineshop.model.dto.response.PaymentForFront;
import uz.com.onlineshop.model.entity.card.CardEntity;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.order.OrderStatus;
import uz.com.onlineshop.model.entity.payment.Payment;
import uz.com.onlineshop.model.entity.payment.PaymentMethod;
import uz.com.onlineshop.model.entity.payment.PaymentStatus;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.CardRepository;
import uz.com.onlineshop.repository.OrderRepository;
import uz.com.onlineshop.repository.PaymentRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final CardRepository cardRepository;



    public StandardResponse<PaymentForFront> payForOrder(PaymentDto paymentDto, Principal principal){
        Payment payment = modelMapper.map(paymentDto, Payment.class);
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        OrderEntity order = orderRepository.findOrderEntityById(UUID.fromString(paymentDto.getOrderId()));
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        CardEntity card =  cardRepository.findCardEntityById(UUID.fromString(paymentDto.getCardId()));
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        if (card.getCardBalance()<order.getTotalAmount()){
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new UserBadRequestException("You have no enough balance for this order! Please, fill your balance!");
        }
        card.setCardBalance(card.getCardBalance()- order.getTotalAmount());
        cardRepository.save(card);
        payment.setOrder(order);
        payment.setUser(user);
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.COMPLETED);
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
        Payment save = paymentRepository.save(payment);
        PaymentForFront paymentForFront = modelMapper.map(save, PaymentForFront.class);

        return StandardResponse.<PaymentForFront>builder()
                .status(Status.SUCCESS)
                .message("Payment added")
                .data(paymentForFront)
                .build();
    }



    public StandardResponse<PaymentForFront> getById(UUID id){
        Payment payment = paymentRepository.findPaymentById(id);
        if (payment==null){
            throw new DataNotFoundException("Payment not found!");
        }
        PaymentForFront paymentForFront = modelMapper.map(payment, PaymentForFront.class);

        return StandardResponse.<PaymentForFront>builder()
                .status(Status.SUCCESS)
                .message("This is payment")
                .data(paymentForFront)
                .build();
    }




    public StandardResponse<String> deleteById(UUID id, Principal principal){
        Payment payment = paymentRepository.findPaymentById(id);
        if (payment==null){
            throw new DataNotFoundException("Payment not found!");
        }
        payment.setDeleted(true);
        payment.setDeletedTime(LocalDateTime.now());
        payment.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        paymentRepository.save(payment);

        return StandardResponse.<String>builder()
                .status(Status.SUCCESS)
                .message("Payment deleted!")
                .data("DELETED")
                .build();
    }





    public StandardResponse<String> multiDeleteById(List<String> id, Principal principal){
        List<Payment> paymentList = paymentRepository.findAllById(id
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()));

        for (Payment payment: paymentList) {
            payment.setDeletedTime(LocalDateTime.now());
            payment.setDeleted(true);
            payment.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
            paymentRepository.save(payment);
        }

        return StandardResponse.<String>builder()
                .status(Status.SUCCESS)
                .message("Payments deleted!")
                .data("DELETED")
                .build();
    }
}