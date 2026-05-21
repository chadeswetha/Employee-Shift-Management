package com.example.shiftservice;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * ShiftServiceApplication: Entry point for the Shift Service Spring Boot application.
 * 
 * - **@SpringBootApplication**: Marks this class as a Spring Boot application,
 *   enabling component scanning, auto-configuration, and property support.
 * - **@EnableScheduling**: Enables support for scheduled tasks in the application.
 * - **@EnableFeignClients**: Enables Feign client support for declarative REST client calls.
 * - **@EnableDiscoveryClient**: Enables service discovery for this application in a 
 *   distributed environment, commonly with Eureka or Consul.
 */

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
public class ShiftServiceApplication {
	/**
     * Main method to start the Spring Boot application.
     * 
     * @param args Command-line arguments passed during the application startup.
     */

	public static void main(String[] args) {
		SpringApplication.run(ShiftServiceApplication.class, args);
	}

}
