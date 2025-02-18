package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.mapper.OrderMapper;
import uz.com.onlineshop.model.dto.request.OrderDto;
import uz.com.onlineshop.model.dto.response.OrderForFrontDto;
import uz.com.onlineshop.model.entity.basket.Basket;
import uz.com.onlineshop.model.entity.credit.Credit;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.enums.OrderStatus;
import uz.com.onlineshop.model.entity.product.ProductEntity;
import uz.com.onlineshop.repository.BasketRepository;
import uz.com.onlineshop.repository.CreditRepository;
import uz.com.onlineshop.repository.OrderRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final CreditRepository creditRepository;
    private final OrderMapper orderMapper;


    public StandardResponse<OrderForFrontDto> save(OrderDto orderDto, Principal principal) {
        Optional<Basket> basket = basketRepository.findBasketById(UUID.fromString(orderDto.getBasketId()));
        OrderEntity order = orderMapper.toEntity(orderDto);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setBillingAddress(orderDto.getBillingAddress());
        order.setShippingAddress(orderDto.getShippingAddress());
        if (basket.isEmpty()) {
            throw new DataNotFoundException("Basket not found!");
        }
        List<ProductEntity> productEntities = basket.get().getProduct();
        Double amount = productEntities.stream().mapToDouble(ProductEntity::getPrice).sum();
        order.setTotalAmount(amount);
        order.setProductEntities(productEntities.stream().toList());
        order.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        OrderEntity save = orderRepository.save(order);
        basketRepository.delete(basket.get());
        OrderForFrontDto orderForFrontDto = orderMapper.toDto(save);

        return StandardResponse.ok("Order saved!", orderForFrontDto);
    }


    public StandardResponse<OrderForFrontDto> getById(UUID id) {
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order == null) {
            throw new DataNotFoundException("Order not found!");
        }
        OrderForFrontDto orderForFrontDto = orderMapper.toDto(order);
        return StandardResponse.ok("This is order", orderForFrontDto);
    }


    public StandardResponse<String> delete(UUID id, Principal principal) {
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order == null) {
            throw new DataNotFoundException("Order not found!");
        }
        if (order.getOrderStatus() == OrderStatus.PAID) {
            Optional<Credit> creditByOrder = creditRepository.findCreditByOrder(order);
            if (creditByOrder.isEmpty()) {
                order.setDeleted(true);
                order.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
                order.setDeletedTime(LocalDateTime.now());
                orderRepository.save(order);

                return StandardResponse.ok("Order deleted!", "DELETED");
            }
            if (creditByOrder.get().getCreditAmount() > 0) {
                throw new NotAcceptableException("Can not delete this order. There is a credit associated with this order. Please, pay for credit firstly!");
            }
        }
        order.setDeleted(true);
        order.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        order.setDeletedTime(LocalDateTime.now());
        orderRepository.save(order);

        return StandardResponse.ok("Order deleted!", "DELETED");

    }


    public StandardResponse<OrderForFrontDto> cancelOrder(UUID id) {
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order == null) {
            throw new DataNotFoundException("Order not found!");
        }
        if (Duration.between(LocalDateTime.now(), order.getCreatedTime()).toMinutes() > 30) {
            throw new NotAcceptableException("Can not cancel this order!");
        }
        if (order.getOrderStatus() == OrderStatus.PAID) {
            throw new UserBadRequestException("Can not cancel this order. Because you have already paid for this!");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        OrderEntity save = orderRepository.save(order);
        OrderForFrontDto orderForFrontDto = orderMapper.toDto(save);

        return StandardResponse.ok("Order canceled!", orderForFrontDto);
    }


    public StandardResponse<OrderForFrontDto> updateOrder(UUID id, OrderDto orderDto, Principal principal) {
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order == null) {
            throw new DataNotFoundException("Order not found!");
        }
        order.setTotalAmount(order.getTotalAmount());
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setBillingAddress(orderDto.getBillingAddress());
        order.setUpdatedTime(LocalDateTime.now());
        order.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        order.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        OrderEntity save = orderRepository.save(order);
        OrderForFrontDto orderForFrontDto = orderMapper.toDto(save);

        return StandardResponse.ok("Order updated!", orderForFrontDto);
    }


    public Page<OrderForFrontDto> getCancelledOrders(Pageable pageable) {
        Page<OrderEntity> orderEntities = orderRepository.findOrderEntityByOrderStatus(pageable);
        return orderEntities.map(orderMapper::toDto);
    }


    public Page<OrderForFrontDto> getMyOrders(Pageable pageable, Principal principal) {
        Page<OrderEntity> orderEntities = orderRepository.findOrderEntityByUserId(pageable, userRepository.findUserEntityByEmail(principal.getName()));
        return orderEntities.map(orderMapper::toDto);
    }


    public StandardResponse<String> changeOrderToDelivered(UUID id) {
        OrderEntity order = orderRepository.findOrderEntityById(id);
        if (order == null) {
            throw new DataNotFoundException("Order not found!");
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return StandardResponse.ok("Order has delivered!", "DELIVERED");
    }


    public StandardResponse<String> multiDeleteById(List<String> id, Principal principal) {
        List<OrderEntity> orderList = orderRepository.findAllById(id
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()));

        if (orderList.isEmpty()) {
            throw new DataNotFoundException("Orders not found!");
        }

        for (OrderEntity order : orderList) {
            order.setDeletedTime(LocalDateTime.now());
            order.setDeleted(true);
            order.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
            orderRepository.save(order);
        }

        return StandardResponse.ok("Orders deleted!", "DELETED");
    }

}
