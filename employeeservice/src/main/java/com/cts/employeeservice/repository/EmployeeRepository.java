package com.cts.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.enums.Role;

import java.util.List;
import java.util.Optional;

/**
 * EmployeeRepository interface provides the data access layer for the Employee entity.
 * It extends JpaRepository to enable standard CRUD operations and includes additional
 * custom query methods for specific use cases.
 */
public interface EmployeeRepository extends JpaRepository<Employee, String> {
	
	 boolean existsByEmail(String email);
	 boolean existsByContactNumber(String contactNumber);

	    boolean existsByEmailAndIdNot(String email, String id); // Add this method
	    boolean existsByContactNumberAndIdNot(String contactNumber, String id); 

    /**
     * Finds an employee by their email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the employee, if found.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Retrieves a list of employees by their role.
     *
     * @param role The role to filter employees by.
     * @return A list of employees matching the specified role.
     */
    List<Employee> findByRole(Role role);

    /**
     * Checks if a manager exists in a specific department.
     *
     * @param department The name of the department.
     * @param manager The role to check for (expected to be Role.MANAGER).
     * @return True if a manager exists in the department, false otherwise.
     */
    boolean existsByDepartmentAndRole(String department, Role manager);

    /**
     * Retrieves all employees with a specific role.
     *
     * @param role The role to filter employees by.
     * @return A list of employees with the specified role.
     */
    List<Employee> findAllByRole(Role role);

    /**
     * Retrieves employees by their department name.
     *
     * @param departmentName The name of the department to search for.
     * @return A list of employees belonging to the specified department.
     */
    List<Employee> findByDepartment(String departmentName);

    /**
     * Finds an employee by their role and department.
     *
     * @param role The role of the employee.
     * @param department The department name.
     * @return An Optional containing the employee, if found.
     */
    Optional<Employee> findByRoleAndDepartment(Role role, String department);
    List<Employee> findByDepartmentAndRole(String department,Role role);

boolean existsByRole(Role admin);
@Query("SELECT e.department FROM Employee e WHERE e.id = :employeeId")
String getDepartmentNameByEmployeeId(@Param("employeeId") String employeeId);



  
   


}
