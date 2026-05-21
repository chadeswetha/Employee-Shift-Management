package com.example.notification.service;

import com.example.notification.dto.NotificationDTO;
import com.example.notification.entity.Notification;
import com.example.notification.enums.NotificationType;
import com.example.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void sendNotification(String recipientId, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .message(message)
                .type(type)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
        repository.save(notification);
    }

    public List<NotificationDTO> getNotificationsFor(String recipientId) {
        return repository.findByRecipientId(recipientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(String id) {
        repository.findById(id).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }

    private NotificationDTO toDto(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId().toString());
        dto.setRecipientId(notification.getRecipientId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setTimestamp(notification.getTimestamp());
        return dto;
    }
}
