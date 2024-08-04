package uz.com.onlineshop.model.order;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING,

    PAID,

    SHIPPED,

    DELIVERED,

    CANCELLED
}
