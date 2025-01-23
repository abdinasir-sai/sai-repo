package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.TimeSheet;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.TimeLogsWrapper;

/**
 * 
 * TimeSheetService interface is a service layer interface which handles all the
 * business logic related to TimeSheet model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves TimeSheet
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface TimeSheetService extends GenericService<Integer,TimeSheet> {


	TimeSheet findTimeSheetByEmployeeIdAndWeekNumber(Integer id, Integer weekNumber);

	TimeSheet updateTimeSheetDetails(TimeSheet timesheet, List<TimeLogsWrapper> weekLogs, Employee employee,
			String timeSheetStatus, String timeSheetDescription);

	TimeSheet updateWithBpmn(TimeSheet timesheet);

	String deleteTimeLogsAndTimeSheetById(Integer id);

	TimeSheet createTimeSheet(Employee employee, List<TimeLogsWrapper> weekLogs, Integer weekNumber,
			String timeSheetStatus, String timeSheetDescription, Integer year);

}
