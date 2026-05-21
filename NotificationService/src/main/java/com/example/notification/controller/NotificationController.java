package com.example.notification.controller;

import com.example.notification.dto.NotificationDTO;
import com.example.notification.enums.NotificationType;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody Map<String, String> payload) {
        service.sendNotification(
                payload.get("recipientId"),
                payload.get("message"),
                NotificationType.valueOf(payload.get("type"))
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{recipientId}")
    public ResponseEntity<List<NotificationDTO>> getAll(@PathVariable String recipientId) {
        return ResponseEntity.ok(service.getNotificationsFor(recipientId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
