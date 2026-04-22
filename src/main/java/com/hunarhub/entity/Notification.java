package com.hunarhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "related_dispute_id")
    private Long relatedDisputeId;

    /** e.g. COMPLAINT_RAISED, COMPLAINT_RESOLVED, ORDER_PLACED, NEW_PRODUCT, ENTREPRENEUR_PENDING, ENTREPRENEUR_APPROVED */
    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "related_order_id")
    private Long relatedOrderId;

    @Column(name = "related_product_id")
    private Long relatedProductId;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

