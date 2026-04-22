package com.hunarhub.dto;

import com.hunarhub.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImageUrl;
    private String categoryName;
    private Long entrepreneurId;
    private String entrepreneurName;
    private Integer quantity;
    private Double totalPrice;
    private Order.OrderStatus status;
    private LocalDateTime orderDate;
}
