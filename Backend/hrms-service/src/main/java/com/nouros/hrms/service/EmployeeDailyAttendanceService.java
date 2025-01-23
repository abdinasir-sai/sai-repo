package com.nouros.hrms.service;

import java.util.Date;

import com.nouros.hrms.model.EmployeeDailyAttendance;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * EmployeeDailyAttendanceService interface is a service layer interface which
 * handles all the business logic related to EmployeeDailyAttendance model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext EmployeeDailyAttendance
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeDailyAttendanceService extends GenericService<Integer, EmployeeDailyAttendance> {

     Boolean addLeaveforEmployee(String processInstanceId);
     Integer getListOfLeavesBetweenDates(Integer employeeId ,Date startDate ,Date endDate,Integer leaveTypeId);
 	public Integer getListOfLeavesByLeaveTypeId(Integer employeeId ,Integer leaveTypeId,Date endDate);
//	public Integer getCountOfLeavesBeforeDate(Date startDate ,Integer leaveTypeId,Integer employeeId);
 	public String changeEmployeeDailyAttendanceStatusByLeaveId(Leaves leave);
 	public Integer deleteEmployeeDailyAttendanceByLeaveId(Integer leaveId);
}
