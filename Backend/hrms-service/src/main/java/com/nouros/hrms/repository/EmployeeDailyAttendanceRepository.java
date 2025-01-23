package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import com.nouros.hrms.model.EmployeeDailyAttendance;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeDailyAttendanceRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeDailyAttendance} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeDailyAttendanceRepository extends GenericRepository<EmployeeDailyAttendance> {

	@Query(value = "SELECT  * FROM EMPLOYEE_DAILY_ATTENDANCE WHERE EMPLOYEEID=:employeeId AND LOGIN_DATE>=:startDate AND LOGIN_DATE<=:endDate AND LEAVE_TYPE_FK=:leaveTypeId AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<EmployeeDailyAttendance> getEmployeeLeaveAttendanceBetweenDates(@Param("employeeId")Integer employeeId,@Param("startDate")Date startDate ,@Param("endDate")Date endDate,@Param("leaveTypeId")Integer leaveTypeId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM EMPLOYEE_DAILY_ATTENDANCE WHERE EMPLOYEEID =:employeeId AND LEAVE_TYPE_FK =:leaveTypeId AND LOGIN_DATE<=:endDate AND CUSTOMER_ID = :customerId ",nativeQuery = true)
	List<EmployeeDailyAttendance> getEmployeeLeaveAttendanceByLeaveTypeId(@Param("employeeId")Integer employeeId, @Param("leaveTypeId")Integer leaveTypeId,@Param("endDate")Date endDate, @Param("customerId") Integer customerId);

 	@Query(value ="SELECT * FROM EMPLOYEE_DAILY_ATTENDANCE WHERE LEAVES_FK=:leaveId AND CUSTOMER_ID = :customerId ",nativeQuery =true)
 	List<EmployeeDailyAttendance> getEmployeeDailyAttendanceByLeaveId(@Param("leaveId")Integer leaveId, @Param("customerId") Integer customerId);
}
