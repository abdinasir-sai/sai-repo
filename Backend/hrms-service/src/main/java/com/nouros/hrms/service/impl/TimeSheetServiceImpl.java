package com.nouros.hrms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;

import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.TimeSheet;
import com.nouros.hrms.repository.TimeLogsRepository;
import com.nouros.hrms.repository.TimeSheetRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TimeSheetService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.TimeLogsWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * This is a class named "TimeSheetServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "TimeSheetService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository TimeSheet and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of TimeSheet
 * TimeSheet) for exporting the TimeSheet data into excel file by reading the
 * template and mapping the TimeSheet details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class TimeSheetServiceImpl extends AbstractService<Integer,TimeSheet> implements TimeSheetService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   TimeSheet entities.
	 */

	private static final Logger log = LogManager.getLogger(TimeSheetServiceImpl.class);

	public TimeSheetServiceImpl(GenericRepository<TimeSheet> repository) {
		super(repository, TimeSheet.class);
	}

	@Autowired
	private TimeSheetRepository timeSheetRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	 @Autowired
	  private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param timeSheet The timeSheet object to create.
	 * @return The created vendor object.
	 */
	@Override
	public TimeSheet create(TimeSheet timeSheet) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		timeSheet.setWorkspaceId(workspaceId); // done done
		return timeSheetRepository.save(timeSheet);
	}

	@Override
	public TimeSheet findTimeSheetByEmployeeIdAndWeekNumber(Integer id, Integer weekNumber) {
		TimeSheet timesheet = null;
		try {
			
//			Session session = entityManager.unwrap(Session.class);
//			log.debug(" Inside @findTimeSheetByEmployeeIdAndWeekNumber UserId fetched for login customerId is : {}", commonUtils.getCustomerId());
//			session.enableFilter("CUSTOMER_ID_FILTER").setParameter("customerId", commonUtils.getCustomerId());
//			log.info(" Inside @findTimeSheetByEmployeeIdAndWeekNumber after enabling CUSTOMER_ID_FILTER ");
			log.debug(" Inside @findTimeSheetByEmployeeIdAndWeekNumber  customerId is : {}", commonUtils.getCustomerId());
			 
			timesheet = timeSheetRepository.findTimeSheetByEmployeeIdAndWeekNumber(id, weekNumber);
			return timesheet;
		} catch (BusinessException be) {
			log.debug("No timesheet Found for employee with id : {} ", id);
		} catch (Exception be) {
			log.debug("SomeThing Went Wrong for timesheet Fetching");
		}
		return timesheet;
	}

	@Override
	@TriggerBPMN(entityName = "TimeSheet", appName = "HRMS_APP_NAME")
	public TimeSheet createTimeSheet(Employee employee, List<TimeLogsWrapper> weekLogs, Integer weekNumber,
			String timeSheetStatus, String timeSheetDescription, Integer year) {
		log.info("Inside method createTimeSheet");
		TimeSheet timesheetForCreation = new TimeSheet();
		timesheetForCreation.setEmployee(employee);
		if (employee != null && employee.getReportingManager() != null) {
			timesheetForCreation.setReportingManagerId(employee.getReportingManager());
		}
		if (timeSheetStatus != null && timeSheetStatus.equalsIgnoreCase("SAVE")) {
			timesheetForCreation.setWorkflowStage("draft");
		} else {
			timesheetForCreation.setApprovalStatus("Pending");
		}
		timesheetForCreation.setWeekNumber(weekNumber);
		timesheetForCreation.setYear("2024");
		timesheetForCreation.setMonth(getMonthFromWeekAndYear(weekNumber));
		Integer totalMinutes = calculateTotalMinutes(weekLogs);
		timesheetForCreation.setSubmittedTotalMinutes(totalMinutes);
		Double totalHours = calculateTotalHours(weekLogs);
		timesheetForCreation.setSubmittedTotalHours(totalHours);
		timesheetForCreation.setDescription(timeSheetDescription);
		setWeekDates(timesheetForCreation, weekNumber, year);
		return create(timesheetForCreation);
	}

	private void setWeekDates(TimeSheet timesheet, Integer weekNumber, Integer year) {
		log.info("Inside method setWeekDates");
		timesheet.setWeekStartDate(getStartDateOfWeek(year, weekNumber));
		timesheet.setWeekEndDate(getEndDateOfWeek(year, weekNumber));
		log.debug("TimeSheet Object to be Created is : {}", timesheet);
	}

	public static Date getStartDateOfWeek(int year, int weekNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return calendar.getTime();
	}

	public static Date getEndDateOfWeek(int year, int weekNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		return calendar.getTime();
	}

	public static int getMonthFromWeekAndYear(int weekNumber) {
		Calendar calendar = Calendar.getInstance();
		int year = 2024;
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Assuming the week starts on Monday
		return calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based, so add 1
	}

	@Override
	@TriggerBPMN(entityName = "TimeSheet", appName = "HRMS_APP_NAME")
	public TimeSheet updateTimeSheetDetails(TimeSheet timesheet, List<TimeLogsWrapper> weekLogs, Employee employee,
			String timeSheetStatus, String timeSheetDescription) {
		log.info("Inside method updateTimeSheetDetails");
		log.debug("ProcessInstanceId found for TimeSheet is : ",timesheet.getProcessInstanceId());
		if (timesheet.getProcessInstanceId() != null) {
			if (timeSheetStatus != null && timeSheetStatus.equalsIgnoreCase("SUBMIT")) {
				timesheet.setApprovalStatus("Closed");
			} else if (timeSheetStatus != null && timeSheetStatus.equalsIgnoreCase("RESUBMIT")) {
				timesheet.setApprovalStatus("On Hold");
			}
		}else if(timesheet.getProcessInstanceId() == null && timeSheetStatus.equalsIgnoreCase("SAVE")) {
			log.info("Inside method updateTimeSheetDetails save in update");
			timesheet.setWorkflowStage("draft");
		}else {
			timesheet.setApprovalStatus("Pending");
		}
		if (timeSheetDescription != null) {
			timesheet.setDescription(timeSheetDescription);
		}
		Integer totalMinutes = calculateTotalMinutes(weekLogs);
		timesheet.setSubmittedTotalMinutes(totalMinutes);

		Double totalHours = calculateTotalHours(weekLogs);
		timesheet.setSubmittedTotalHours(totalHours);

		if (null == timesheet.getProcessInstanceId()) {
			return updateWithBpmn(timesheet);
		}
		return timeSheetRepository.save(timesheet);
	}

	@Override
	@TriggerBPMN(entityName = "TimeSheet", appName = "HRMS_APP_NAME")
	public TimeSheet updateWithBpmn(TimeSheet timesheet) {
		log.info("Inside method updateWithBpmn");
		return timeSheetRepository.save(timesheet);
	}

	public Integer calculateTotalMinutes(List<TimeLogsWrapper> weekLogs) {
		Integer totalMinutes = 0;
		for (TimeLogsWrapper weekLog : weekLogs) {
			totalMinutes += calculateTotalMinutesForEachTimeLogs(weekLog);
		}
		log.debug("total minutes after complete iteration : {} ", totalMinutes);
		return totalMinutes;
	}

	private Integer calculateTotalMinutesForEachTimeLogs(TimeLogsWrapper logsWrapper) {
		log.info("Inside method calculateTotalMinutes");
		Integer sum = 0;
		sum += convertToInt(logsWrapper.getSunday());
		sum += convertToInt(logsWrapper.getMonday());
		sum += convertToInt(logsWrapper.getTuesday());
		sum += convertToInt(logsWrapper.getWednesday());
		sum += convertToInt(logsWrapper.getThursday());
		sum += convertToInt(logsWrapper.getFriday());
		sum += convertToInt(logsWrapper.getSaturday());
		log.debug("total minutes for each timelogs is : {} ", sum);
		return sum;
	}

	private Integer convertToInt(String str) {
		if (str == null) {
			return 0;
		} else {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	public Double calculateTotalHours(List<TimeLogsWrapper> weekLogs) {
		Double totalMinutes = 0.0;
		for (TimeLogsWrapper weekLog : weekLogs) {
			totalMinutes += calculateTotalMinutesForWeekLogs(weekLog);
		}
		Double totalHours = totalMinutes / 60;
		log.debug("total hours after complete iteration : {} ", totalHours);
		return totalHours;
	}

	private Double calculateTotalMinutesForWeekLogs(TimeLogsWrapper logsWrapper) {
		log.info("Inside method calculateTotalMinutesForWeekLogs");
		Double sum = 0.0;
		sum += convertToDouble(logsWrapper.getSunday());
		sum += convertToDouble(logsWrapper.getMonday());
		sum += convertToDouble(logsWrapper.getTuesday());
		sum += convertToDouble(logsWrapper.getWednesday());
		sum += convertToDouble(logsWrapper.getThursday());
		sum += convertToDouble(logsWrapper.getFriday());
		sum += convertToDouble(logsWrapper.getSaturday());
		log.debug("total minutes for each week logs is : {} ", sum);
		return sum;
	}

	private Double convertToDouble(String str) {
		if (str == null) {
			return 0.0;
		} else {
			try {
				return Double.parseDouble(str);
			} catch (NumberFormatException e) {
				return 0.0;
			}
		}
	}

	@Override
	public String deleteTimeLogsAndTimeSheetById(Integer id) {
		log.debug("Inside method deleteTimeLogsAndTimeSheetById With Id : {} ", id);
		try {
			TimeLogsRepository timeLogsRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(TimeLogsRepository.class);
			log.debug("Inside method deleteTimeLogsAndTimeSheetById customerId is : {}", commonUtils.getCustomerId());
			timeLogsRepository.deleteAllTimeLogsAssociatedWithTimeSheet(id, commonUtils.getCustomerId());
			log.debug("All TimeLogs Deleted for Associated TimeSheet with Id : {} ", id);
			timeSheetRepository.deleteById(id);
			log.debug("TimeSheet Deleted with Id : {} ", id);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.debug("SomeThing Went Wrong While Deleting timesheet And TimeLogs ");
			return APIConstants.FAILURE_JSON;
		}
	}

}
