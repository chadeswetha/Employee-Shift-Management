package com.cts.employeeservice;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The EmployeeserviceApplication class serves as the entry point 
 * for the Employee Service application. It is annotated with 
 * @SpringBootApplication to mark it as a Spring Boot application 
 * and @EnableDiscoveryClient to enable service registration and discovery.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeeserviceApplication {

    /**
     * The main method is the entry point of the application. 
     * It uses SpringApplication.run to bootstrap and launch the application.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EmployeeserviceApplication.class, args);
    }
}
