package com.hunarhub.service;

import com.hunarhub.dto.NotificationDto;
import com.hunarhub.entity.Notification;
import com.hunarhub.entity.User;
import com.hunarhub.exception.ResourceNotFoundException;
import com.hunarhub.repository.NotificationRepository;
import com.hunarhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public void createNotification(User recipient, String title, String message, Long relatedDisputeId) {
        createNotification(recipient, title, message, "GENERAL", relatedDisputeId, null, null);
    }

    @Transactional
    public void createNotification(User recipient, String title, String message, String notificationType,
                                   Long relatedDisputeId, Long relatedOrderId, Long relatedProductId) {
        Notification n = new Notification();
        n.setRecipient(recipient);
        n.setTitle(title);
        n.setMessage(message);
        n.setNotificationType(notificationType != null ? notificationType : "GENERAL");
        n.setRelatedDisputeId(relatedDisputeId);
        n.setRelatedOrderId(relatedOrderId);
        n.setRelatedProductId(relatedProductId);
        n.setRead(false);
        notificationRepository.save(n);
    }

    public List<NotificationDto> getMyNotifications() {
        User me = getCurrentUser();
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(me.getId())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public long getMyUnreadCount() {
        User me = getCurrentUser();
        return notificationRepository.countByRecipientIdAndReadFalse(me.getId());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        User me = getCurrentUser();
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!n.getRecipient().getId().equals(me.getId())) {
            throw new ResourceNotFoundException("Notification not found");
        }
        n.setRead(true);
        notificationRepository.save(n);
    }

    private NotificationDto toDto(Notification n) {
        return new NotificationDto(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getNotificationType(),
                n.getRelatedDisputeId(),
                n.getRelatedOrderId(),
                n.getRelatedProductId(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}

