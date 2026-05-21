package com.example.notification.entity;

import com.example.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	 @Id
	 @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String recipientId; // employeeId or managerId
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean isRead;
    private LocalDateTime timestamp;
    
    
    @PrePersist
    public void generateUUID() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
