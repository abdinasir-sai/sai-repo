package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The LeavesRepository interface is a extension of {@link GenericRepository}.
 * It's purpose is to provide additional methods that are specific to the
 * {@link Leaves} entity. see GenericRepository see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface LeavesRepository extends GenericRepository<Leaves> {


	
	Leaves findByProcessInstanceId(String processInstanceId);
	
	@Query(nativeQuery = true, value = "SELECT  sum(LEAVE_TAKEN) FROM LEAVES WHERE  LEAVE_TAKEN is not NULL and  WORKFLOW_STAGE= 'HR-Approved' and     FROM_DATE <=:toDate          AND TO_DATE >=:fromDate          AND EMPLOYEE_ID =:employeeId          AND LEAVE_TYPE_ID =:leaveType AND CUSTOMER_ID = :customerId ;")
	Long getUnpaidLeaveCount(@Param("fromDate") Date fromDate, @Param("toDate")Date toDate, @Param("employeeId") Integer employeeId,@Param("leaveType") Integer leaveType, @Param("customerId") Integer customerId);

	@Query(nativeQuery = true, value = "SELECT  sum(LEAVE_TAKEN) FROM LEAVES WHERE  LEAVE_TAKEN is not NULL  and      FROM_DATE <=:toDate          AND TO_DATE >=:fromDate          AND EMPLOYEE_ID =:employeeId          AND LEAVE_TYPE_ID =:leaveType AND CUSTOMER_ID = :customerId ;")
	Long getLeaveCount(@Param("fromDate") Date fromDate, @Param("toDate")Date toDate, @Param("employeeId") Integer employeeId,@Param("leaveType") Integer leaveType, @Param("customerId") Integer customerId);

	
	
	@Query(nativeQuery = true, value = "SELECT MAX(TO_DATE) AS end_date FROM LEAVES WHERE LEAVE_TAKEN is not NULL and APPROVAL_STATUS='Approved' and FROM_DATE >= :fromDate AND TO_DATE <= :toDate AND EMPLOYEE_ID = :employeeId AND LEAVE_TYPE_ID=:leaveType AND CUSTOMER_ID = :customerId")
	Date getMaxEndDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("employeeId") Integer employeeId, @Param("leaveType") Integer leaveType, @Param("customerId") Integer customerId);

	
	@Query(nativeQuery = true, value = "select EMPLOYEE_ID,sum(LEAVE_TAKEN) from LEAVES where LEAVE_TAKEN is not NULL and  APPROVAL_STATUS='Approved' AND LEAVE_TYPE_ID=:leaveType and FROM_DATE <= :toDate          AND TO_DATE >= :fromDate AND CUSTOMER_ID = :customerId   group by EMPLOYEE_ID ;")
	List<Object[]> getAllEmployeeLeaveCount(@Param("fromDate")  Date fromDate,@Param("toDate")  Date toDate,@Param("leaveType") Integer leaveType, @Param("customerId") Integer customerId);

	@Query(nativeQuery = true, value = "SELECT DATEDIFF( :toDate , :fromDate) AS days_difference")
	Long getTotaldayCount(@Param("fromDate") Date fromDate, @Param("toDate")  Date toDate);
	
	@Query(value = "SELECT COUNT(*) FROM LEAVES l INNER JOIN EMPLOYEE e ON l.EMPLOYEE_ID=e.ID WHERE l.CREATED_TIME >:beforeDate AND l.CREATED_TIME<:afterDate AND l.EMPLOYEE_ID=:employeeId AND l.LEAVE_TYPE_ID=:type AND l.WORKFLOW_STAGE=:stage AND l.CUSTOMER_ID = :customerId",nativeQuery =true)
    Integer getLeaveCountByDate(@Param("beforeDate")LocalDate beforeDate,@Param("afterDate")LocalDate afterDate,@Param("employeeId")Integer employeeId,@Param("type")Integer type,@Param("stage")String flow, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT sum(LEAVE_TAKEN) FROM LEAVES l INNER JOIN EMPLOYEE e ON l.EMPLOYEE_ID=e.ID WHERE l.FROM_DATE>=:beforeDate AND l.TO_DATE<=:afterDate AND l.EMPLOYEE_ID=:employeeId AND l.LEAVE_TYPE_ID=:type AND l.WORKFLOW_STAGE=:stage AND l.CUSTOMER_ID = :customerId",nativeQuery =true)
    Integer getLeaveSumByDate(@Param("beforeDate")LocalDate beforeDate,@Param("afterDate")LocalDate afterDate,@Param("employeeId")Integer employeeId,@Param("type")Integer type,@Param("stage")String stage, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM LEAVES l WHERE  l.WORKFLOW_STAGE =:workflowStage AND l.LEAVE_TYPE_ID =:leaveType AND l.EMPLOYEE_ID =:employeeId AND AND l.CUSTOMER_ID = :customerId  AND ( (l.from_date BETWEEN (SELECT MIN(:fromDate) FROM PAYROLL_RUN) AND (SELECT MAX(:toDate) FROM PAYROLL_RUN)) OR  l.to_date BETWEEN (SELECT MIN(:fromDate) FROM PAYROLL_RUN) AND (SELECT MAX(:toDate) FROM PAYROLL_RUN) ) ",nativeQuery = true)
	List<Leaves> getLeavesByPayrollRunDates(@Param("workflowStage")String workflowStage ,@Param("leaveType") Integer leaveType, @Param("employeeId") Integer employeeId,@Param("fromDate") Date fromDate, @Param("toDate")Date toDate, @Param("customerId") Integer customerId);

	@Query(value = "SELECT SUM(LEAVE_TAKEN) FROM LEAVES WHERE EMPLOYEE_ID =:employeeId AND LEAVE_TYPE_ID =:leaveTypeId AND WORKFLOW_STAGE =:workflowStage AND CUSTOMER_ID = :customerId" ,nativeQuery = true)
	Integer getLeaveTakenByEmployeeIdAndLeaveIdAndWorkflowStage(@Param("employeeId")Integer employeeId ,@Param("leaveTypeId")Integer leaveTypeId , @Param("workflowStage")String workflowStage, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM LEAVES WHERE PROCESS_INSTANCE_ID =:processInstanceId AND CUSTOMER_ID = :customerId",nativeQuery = true)
	Leaves getLeaveByProcessInstanceId(@Param("processInstanceId")String processInstanceId, @Param("customerId") Integer customerId);
	
}
