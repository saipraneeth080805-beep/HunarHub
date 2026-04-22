package com.hunarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private String notificationType;
    private Long relatedDisputeId;
    private Long relatedOrderId;
    private Long relatedProductId;
    private boolean read;
    private LocalDateTime createdAt;
}

