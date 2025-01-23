package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeLeaveTypeRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeLeaveType} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeLeaveTypeRepository extends GenericRepository<EmployeeLeaveType> {

	@Query("SELECT e FROM EmployeeLeaveType e WHERE e.employeeId.id =:empId and e.leaveType.id=:leaveType")
	EmployeeLeaveType findByEmployeeIdAndLeaveTypeId(@Param("empId") Integer empId, @Param("leaveType") Integer leaveType);

	
	@Query("SELECT e FROM EmployeeLeaveType e WHERE e.employeeId.id =:empId")
	List<EmployeeLeaveType> findByEmployeeId(@Param("empId") Integer empId);

	@Query(value="select * from EMPLOYEE_LEAVE_TYPE where  EMPLOYEE_ID_FK in ( SELECT ID  FROM EMPLOYEE  WHERE DATE_OF_JOINING <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)    AND DELETED = 0 AND CUSTOMER_ID = :customerId)",nativeQuery=true)
	List<EmployeeLeaveType> findEmployeeLeaveTypeWhoCompletedThreeMonths( @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM EMPLOYEE_LEAVE_TYPE WHERE YEAR_END_DATE = DATE_ADD(YEAR_START_DATE, INTERVAL 1 YEAR) AND CUSTOMER_ID = :customerId",nativeQuery=true)
	List<EmployeeLeaveType> findEmployeeLeaveTypeWhoCompletedYear( @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT * FROM EMPLOYEE_LEAVE_TYPE WHERE  EMPLOYEE_ID_FK =:employeeId AND LEAVE_TYPE_FK =:leaveTypeId AND CUSTOMER_ID = :customerId ",nativeQuery = true )
	EmployeeLeaveType getEmployeeLeaveTypeByEmployeeIdAndLeaveTypeId(@Param("employeeId")Integer employeeId,@Param("leaveTypeId")Integer leaveTypeId, @Param("customerId") Integer customerId);
} 
