package com.example.shiftservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.shiftservice.dto.NotificationRequest;


 
@FeignClient(name = "NOTIFICATION-SERVICE",url = "http://localhost:8769/api/notifications")
public interface NotificationClient {
 
    @PostMapping("/send")
    void sendNotification(@RequestBody NotificationRequest request);
}
 