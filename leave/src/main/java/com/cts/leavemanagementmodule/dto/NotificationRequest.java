package com.cts.leavemanagementmodule.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private String recipientId;
    private String title;
   private String message;
    private NotificationType type;
    public enum NotificationType {
        SHIFT_REMINDER,
        LEAVE_REQUEST,
        LEAVE_RESPONSE
    }

}