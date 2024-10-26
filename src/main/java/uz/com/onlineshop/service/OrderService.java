package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.model.dto.request.OrderDto;
import uz.com.onlineshop.model.dto.response.OrderForFront;
import uz.com.onlineshop.model.entity.basket.Basket;
import uz.com.onlineshop.model.entity.card.CardEntity;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.order.OrderStatus;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.BasketRepository;
import uz.com.onlineshop.repository.CardRepository;
import uz.com.onlineshop.repository.OrderRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BasketRepository basketRepository;
    private final CardRepository cardRepository;



    public StandardResponse<OrderForFront> save(OrderDto orderDto, Principal principal){
        Optional<Basket> basket = basketRepository.findBasketById(UUID.fromString(orderDto.getBasketId()));
        OrderEntity order = modelMapper.map(orderDto, OrderEntity.class);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setBillingAddress(orderDto.getBillingAddress());
        order.setShippingAddress(orderDto.getShippingAddress());
        if (basket.isEmpty()){
            throw new DataNotFoundException("Basket not found!");
        }
        List<ProductEntity> productEntities = basket.get().getProduct();
        Double amount = productEntities.stream().mapToDouble(ProductEntity::getPrice).sum();
        order.setTotalAmount(amount);
        order.setProductEntities(productEntities.stream().toList());
        order.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        OrderEntity save = orderRepository.save(order);
        basketRepository.delete(basket.get());
        OrderForFront orderForFront = modelMapper.map(save, OrderForFront.class);

        return StandardResponse.<OrderForFront>builder()
                .data(orderForFront)
                .status(Status.SUCCESS)
                .message("Order saved!")
                .build();
    }



    public StandardResponse<OrderForFront> getById(UUID id){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        OrderForFront orderForFront = modelMapper.map(order, OrderForFront.class);
        return StandardResponse.<OrderForFront>builder()
                .data(orderForFront)
                .status(Status.SUCCESS)
                .message("This is order!")
                .build();
    }





    public StandardResponse<String> delete(UUID id, Principal principal){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        order.setDeleted(true);
        order.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        order.setDeletedTime(LocalDateTime.now());
        orderRepository.save(order);

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("Order deleted!")
                .build();
    }



    public StandardResponse<OrderForFront> cancelOrder(UUID id){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        OrderEntity save = orderRepository.save(order);
        OrderForFront orderForFront = modelMapper.map(save, OrderForFront.class);

        return StandardResponse.<OrderForFront>builder()
                .data(orderForFront)
                .status(Status.SUCCESS)
                .message("Order cancelled!")
                .build();
    }




    public StandardResponse<OrderForFront> updateOrder(UUID id, OrderDto orderDto, Principal principal){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        order.setTotalAmount(order.getTotalAmount());
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setBillingAddress(orderDto.getBillingAddress());
        order.setUpdatedTime(LocalDateTime.now());
        order.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        order.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        OrderEntity save = orderRepository.save(order);
        OrderForFront orderForFront = modelMapper.map(save, OrderForFront.class);

        return StandardResponse.<OrderForFront>builder()
                .data(orderForFront)
                .status(Status.SUCCESS)
                .message("Order updated!")
                .build();
    }



    public Page<OrderForFront> getCancelledOrders(Pageable pageable){
        Page<OrderEntity> orderEntities = orderRepository.findOrderEntityByOrderStatus(pageable);
       return  orderEntities.map(order -> new OrderForFront(order.getId(),order.getTotalAmount(),
               order.getOrderStatus(),
               order.getShippingAddress(), order.getBillingAddress()));
    }



    public Page<OrderForFront> getMyOrders(Pageable pageable, Principal principal){
        Page<OrderEntity> orderEntities = orderRepository.findOrderEntityByUserId(pageable,userRepository.findUserEntityByEmail(principal.getName()));
        return orderEntities.map(order -> new OrderForFront(order.getId(),order.getTotalAmount(),order.getOrderStatus(),
                order.getShippingAddress(), order.getBillingAddress()));
    }







    public StandardResponse<String> changeOrderToDelivered(UUID id){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return StandardResponse.<String>builder()
                .status(Status.SUCCESS)
                .message("Order has delivered!")
                .data("DELIVERED")
                .build();
    }





    public StandardResponse<OrderForFront> payForOrder(UUID id, String cardId){
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order==null){
            throw new DataNotFoundException("Order not found!");
        }
        CardEntity card =  cardRepository.findCardEntityById(UUID.fromString(cardId));
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        if (card.getCardBalance()<order.getTotalAmount()){
            throw new UserBadRequestException("You have no enough balance for this order! Please, fill your balance!");
        }
        card.setCardBalance(card.getCardBalance()- order.getTotalAmount());
        cardRepository.save(card);
        order.setOrderStatus(OrderStatus.PAID);
        OrderEntity save = orderRepository.save(order);
        OrderForFront orderForFront = modelMapper.map(save, OrderForFront.class);

        return StandardResponse.<OrderForFront>builder()
                .status(Status.SUCCESS)
                .message("Order amount paid!")
                .data(orderForFront)
                .build();

    }

}
