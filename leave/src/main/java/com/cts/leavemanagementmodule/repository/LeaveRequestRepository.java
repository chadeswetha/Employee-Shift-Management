package com.cts.leavemanagementmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.leavemanagementmodule.model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;
/**
 * LeaveRequestRepository: Repository interface for LeaveRequest entity.
 *
 * Author: K.Ankitha Reddy
 
 */

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String> {
	/**
     * Custom query to find leave requests by employee ID using @Query.
     *
     * @param employeeId The ID of the employee.
     * @return List of leave requests for the given employee ID.
     */

    // Custom query to find leave requests by employee ID using @Query
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employeeId = :employeeId")
    List<LeaveRequest> findLeaveRequestsByEmployeeId(@Param("employeeId") String employeeId);
    
    @Query("SELECT l FROM LeaveRequest l WHERE l.managerId = :managerId AND l.status = 'PENDING'")
    List<LeaveRequest> findPendingLeaveRequestsByManagerId(@Param("managerId") String managerId);
//    @Query("SELECT COUNT(l) > 0 FROM Leave l " +
//            "WHERE l.employeeId = :employeeId AND l.status != 'REJECTED' " +
//            "AND l.startDate <= :endDate AND l.endDate >= :startDate")
    // boolean existsOverlappingLeave(String employeeId, LocalDate startDate, LocalDate endDate);
   // @Query("SELECT e.department FROM Employee e WHERE e.id = :employeeId")
   // String getDepartmentNameByEmployeeId(@Param("employeeId") String employeeId);
   Boolean existsByEmployeeIdAndStartDate(String employeeId,LocalDate startDate);
}
