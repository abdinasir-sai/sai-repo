package com.nouros.hrms.service;

import java.util.Date;

import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * EmployeeLeaveTypeService interface is a service layer interface which handles
 * all the business logic related to EmployeeLeaveType model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext EmployeeLeaveType
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeLeaveTypeService extends GenericService<Integer,EmployeeLeaveType> {

	

	String leaveValidate(Integer id, Integer leaveType, Date fromDate, Date toDate, Integer duration,LeaveType leaveTypeObj ); 

	int getBusinessDayLeave(Date fromDate, Date toDate);

	String creaditLeaveByEmpId(Integer empId);
	
	 void creaditLeave();
	  void updateLeaveBalanceWhenYearComplete();
	  public EmployeeLeaveType getEmployeeLeaveTypeByEmployeeIdAndLeaveType(Integer employeeId,Integer leaveTypeId);
	  
}
