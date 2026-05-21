package com.cts.employeeservice.test;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.enums.Role;
import com.cts.employeeservice.repository.EmployeeRepository;
import com.cts.employeeservice.dto.EmployeeResponseDto;
import com.cts.employeeservice.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees_ReturnsEmployeeList() {
        Employee employee1 = Employee.builder()
                .employeeName("Alice")
                .email("alice@example.com")
                .department("IT")
                .role(Role.EMPLOYEE)
                .contactNumber("1111111111")
                .build();

        Employee employee2 = Employee.builder()
                .employeeName("Bob")
                .email("bob@example.com")
                .department("HR")
                .role(Role.MANAGER)
                .contactNumber("2222222222")
                .build();

        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<EmployeeResponseDto> responseDtoList = employeeService.getAllEmployees();

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(2, responseDtoList.size());
        Assertions.assertEquals("Alice", responseDtoList.get(0).getEmployeeName());
        Assertions.assertEquals("Bob", responseDtoList.get(1).getEmployeeName());
    }
}
