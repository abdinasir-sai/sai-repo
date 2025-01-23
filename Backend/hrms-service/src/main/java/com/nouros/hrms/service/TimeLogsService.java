package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.TimeLogs;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.TimeSheetWrapper;

/**
 * 
 * TimeLogsService interface is a service layer interface which handles all the
 * business logic related to TimeLogs model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext TimeLogs
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface TimeLogsService extends GenericService<Integer,TimeLogs> {


	Double calculateHours(Integer timeLogsId);

	List<TimeLogs> getTimeLogsByEmpIdAndWeekNumber(Integer empId, Integer weeks);

	String getTimeLogsDetails(Integer empId, Integer weeks);

	String getTimeLogsDetailsByTimeSheet(Integer timeSheetId);

	List<TimeLogs> createOrUpdateTimeLogs(TimeSheetWrapper timeSheetWrapper);

}
