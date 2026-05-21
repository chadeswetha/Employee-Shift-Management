package com.example.shiftservice.repository;

import com.example.shiftservice.dto.EmployeeDTO;
import com.example.shiftservice.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * ShiftRepository: JPA repository interface for managing Shift entities.
 * <p>
 * This interface provides methods to perform CRUD operations and custom queries
 * for the `Shift` entity. It extends `JpaRepository`, which offers built-in functionality
 * for database access and management.
 * </p>
 *
 * **Annotations**:
 * - **@Repository**: Marks this interface as a Spring Data repository for dependency injection.
 * - **JpaRepository**: Provides generic CRUD methods for the entity.
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, String> {

    /**
     * Finds all shifts associated with the specified employee ID.
     *
     * @param employeeId The unique ID of the employee.
     * @return A list of shifts assigned to the employee.
     */
    List<Shift> findByEmployeeId(String employeeId);

    /**
     * Finds all shifts within a specified date range for the given employee ID.
     *
     * @param startDate  The start date of the range.
     * @param endDate    The end date of the range.
     * @param employeeId The unique ID of the employee.
     * @return A list of shifts within the date range for the employee.
     */
    List<Shift> findByShiftDateBetweenAndEmployeeId(LocalDate startDate, LocalDate endDate, String employeeId);

    /**
     * Checks if a shift exists on a specified date for the given employee ID.
     *
     * @param shiftDate  The date of the shift.
     * @param employeeId The unique ID of the employee.
     * @return `true` if a shift exists on the specified date; otherwise, `false`.
     */
    boolean existsByShiftDateAndEmployeeId(LocalDate shiftDate, String employeeId);
    boolean existsByEmployeeIdAndShiftDate(String employeeId, LocalDate shiftDate);

    /**
     * Checks if a shift exists for the given employee ID and start time.
     *
     * @param employeeId The unique ID of the employee.
     * @param localTime  The start time of the shift.
     * @return `true` if a shift exists for the employee at the specified start time; otherwise, `false`.
     */
    boolean existsByEmployeeIdAndStartTime(String employeeId, LocalDateTime localTime);

    /**
     * Finds all shifts with start times within a specified range.
     *
     * @param startTime The beginning of the time range.
     * @param endTime   The end of the time range.
     * @return A list of shifts starting within the specified range.
     */
    List<Shift> findBystartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<Shift> findByAssignedBy(String assignedBy);

//	Collection<EmployeeDTO> findByAssignedByAndFilters(String assignedBy, String shiftId, String employeeName,
//			LocalDate shiftDate);
    
    
    
}
