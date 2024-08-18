package uz.com.onlineshop.model.entity.order;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING,

    PAID,

    DELIVERED,

    CANCELLED
}
