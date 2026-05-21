package com.cts.leavemanagementmodule.model;

import lombok.AllArgsConstructor;



import lombok.Data;

import java.time.LocalDateTime;
/**
 * Class representing a result response.
 * 
 * This class is used to encapsulate the response of an operation, including success status, message, data, and timestamp.
 * 
 * Author: K.Ankitha Reddy
 */
@Data
@AllArgsConstructor
public class ResultResponse {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
}