package com.example.shiftservice.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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