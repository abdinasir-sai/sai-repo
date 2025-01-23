package com.nouros.hrms.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.LeavesDto;
import com.nouros.hrms.wrapper.UnpaidLeaveWrapper;

import jakarta.validation.Valid;

/**
 * 
 * LeavesService interface is a service layer interface which handles all the
 * business logic related to Leaves model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Leaves
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface LeavesService extends GenericService<Integer,Leaves> {

	List<UnpaidLeaveWrapper> getUnpaidLeaveCount(Date fromDate, Date toDate, Integer employeeId);

	String updateLeaveBalanceProcessInstanceId(@Valid String processInstanceId);

	Leaves updateLeavesWorkflowStage(LeavesDto leavesDto);

	   public Integer getCountOfLeave(LocalDate beforeDate , LocalDate afterDate,Integer employeeId,Integer code,String flow);

	   Integer findLeaveTakenByEmployeeIdAndWorkflowStage(Integer employeeId,Integer leaveTypeId,String workflowStage);

		 Integer getLeaveByPayrollDateAndEmployeeId(Date fromDate, Date toDate, Integer employeeId,
				Integer leveTypeId );
		 
		 Leaves getLeavesByProcessInstanceId(String processInstanceId);
		 public String cancelLeaves(String processInstanceId);
		 public String deleteLeaveByLeaveId(Integer leaveId);
}
