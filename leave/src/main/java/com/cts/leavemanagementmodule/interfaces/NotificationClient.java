package com.cts.leavemanagementmodule.interfaces;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.leavemanagementmodule.dto.NotificationRequest;
 
@FeignClient(name = "NOTIFICATION-SERVICE",url = "http://localhost:8766/api/notifications")
public interface NotificationClient {
 
    @PostMapping("/send")
    void sendNotification(@RequestBody NotificationRequest request);
}
 