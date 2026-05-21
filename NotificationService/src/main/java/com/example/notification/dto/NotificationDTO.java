package com.example.notification.dto;

import com.example.notification.enums.NotificationType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private String id;
    private String recipientId;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime timestamp;
}
