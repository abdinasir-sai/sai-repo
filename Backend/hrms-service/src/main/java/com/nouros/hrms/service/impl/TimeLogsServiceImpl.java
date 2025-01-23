package com.nouros.hrms.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.TimeLogs;
import com.nouros.hrms.model.TimeSheet;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.TimeLogsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.TimeLogsService;
import com.nouros.hrms.service.TimeSheetService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.TimeLogsUtils;
import com.nouros.hrms.wrapper.TimeLogsWrapper;
import com.nouros.hrms.wrapper.TimeSheetWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "TimeLogsServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "TimeLogsService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository TimeLogs and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of TimeLogs
 * TimeLogs) for exporting the TimeLogs data into excel file by reading the
 * template and mapping the TimeLogs details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class TimeLogsServiceImpl extends AbstractService<Integer,TimeLogs> implements TimeLogsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   TimeLogs entities.
	 */

	private static final Logger log = LogManager.getLogger(TimeLogsServiceImpl.class);

	@Autowired
	CustomerInfo customerInfo;
 
	@Autowired
	UserRest userRest;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CommonUtils commonUtils;

 
	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	public TimeLogsServiceImpl(GenericRepository<TimeLogs> repository) {
		super(repository, TimeLogs.class);
	}

	@Autowired
	private TimeLogsRepository timeLogsRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param timeLogs The timeLogs object to create.
	 * @return The created vendor object.
	 */
	@Override
	public TimeLogs create(TimeLogs timeLogs) {
		CustomerInfo customerInfo1 = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo1.getActiveUserSpaceId();
		timeLogs.setWorkspaceId(workspaceId); // done done
		return timeLogsRepository.save(timeLogs);
	}
	
	@Override
    public Double calculateHours(Integer timeLogsId) {
        log.info("Inside method calculateHours");
        try {
            TimeLogs timeLogs = timeLogsRepository.findById(timeLogsId).orElse(null);
            if (timeLogs == null || timeLogs.getFromTime() == null || timeLogs.getToTime() == null) {
                throw new IllegalArgumentException("Invalid timeLogsId or missing timestamps.");
            }
            log.debug("TimeLogs object fetched is : {} ", timeLogs);

            Instant fromInstant = convertDateToInstant(timeLogs.getFromTime(), timeLogsId, "fromTime");
            Instant toInstant = convertDateToInstant(timeLogs.getToTime(), timeLogsId, "toTime");

            long durationSeconds = Duration.between(fromInstant, toInstant).getSeconds();
            double hours = durationSeconds / 3600.0;
            return Math.round(hours * 100.0) / 100.0;
        } catch (Exception e) {
            String errorMessage = "Error calculating hours for timeLogsId inside class TimeLogsServiceImpl: " + timeLogsId;
            throw new BusinessException(errorMessage, e);
        }
    }

    private Instant convertDateToInstant(Date date, Integer timeLogsId, String fieldName) {
        try {
            return date.toInstant();
        } catch (Exception e) {
            String errorMessage = "Failed to convert Date to Instant for " + fieldName + " inside class TimeLogsServiceImpl: " + timeLogsId;
            throw new BusinessException(errorMessage, e);
        }
    }


	@Override
	public List<TimeLogs> getTimeLogsByEmpIdAndWeekNumber(Integer empId, Integer weekNumber) {
		log.info("Inside method getTimeLogsByEmpIdAndWeekNumber");
		Employee employee = employeeService.findById(empId);
		String userName = "";
		if (employee != null) {
			log.debug(" Employee Details first name : {} , and lastName : {}", employee.getFirstName(),
					employee.getLastName());
			userName = employee.getFirstName() + " " + employee.getLastName();
			log.debug(" UserName formed is  : {}", userName);
		} else {
			throw new BusinessException("Employee not present for Id : " + empId);
		}
		Date fromDate = TimeLogsUtils.getStartDateOfWeek(2024, weekNumber);
		Date toDate = TimeLogsUtils.getEndDateOfWeek(2024, weekNumber);
		log.debug(" fromDate according to weekNumber is  : {} ", fromDate);
		log.debug(" toDate according to weekNumber is  : {} ", toDate);
		log.debug(" Inside @getTimeLogsByEmpIdAndWeekNumber  customerId is : {}", commonUtils.getCustomerId());
		List<TimeLogs> result = timeLogsRepository.getTimeLogsByUserNameAndWeeks(userName, fromDate, toDate,commonUtils.getCustomerId());
		if (result.isEmpty())
			throw new BusinessException(
					"Time Log not present for User - " + userName + " from date " + fromDate + " to date " + toDate);
		return result;
	}

	@Override
	public String getTimeLogsDetails(Integer empId, Integer weekNumber) {
	    try {
	        log.info("Inside method getTimeLogsDetails");
	        List<TimeLogsWrapper> timelogsDetails;
	        List<TimeLogs> result = new ArrayList<>();
	        Employee employee = getEmployeeObject(empId);
	        if (employee != null) {
	            log.debug("Employee Details first name: {}, and lastName: {}", employee.getFirstName(),
	                    employee.getLastName());
	            log.debug(" Inside @getTimeLogsDetails  customerId is : {}", commonUtils.getCustomerId());
	            result = timeLogsRepository.getTimeLogsByEmployeeIdAndWeekNumber(employee.getId(), weekNumber,commonUtils.getCustomerId());
	        }
	        timelogsDetails = setTimeLogsDetails(result);
	        TimeSheet timesheet = getTimeSheetForEmployee(result);
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("week", timelogsDetails);
	        if (timesheet != null) {
	            responseMap.put("timesheet", timesheet);
	        }
	        ObjectMapper objectMapper = customObjectMapper();
	        return objectMapper.writeValueAsString(responseMap);
	    } catch (JsonProcessingException e) {
	        log.error("Error processing JSON response: {}", e.getMessage());
	        return "Error processing JSON response: " + e.getMessage();
	    } catch (BusinessException e) {
	        log.error("Business exception encountered: {}", e.getMessage());
	        return "Business exception encountered: " + e.getMessage();
	    } catch (Exception e) {
	        log.error("An unexpected error occurred: {}", e.getMessage());
	        return "An unexpected error occurred: " + e.getMessage();
	    }
	}

	private TimeSheet getTimeSheetForEmployee(List<TimeLogs> timeLogsList) {
	    try {
	        return (timeLogsList.get(0).getTimesheetFk() != null ? timeLogsList.get(0).getTimesheetFk() : null);
	    } catch (Exception e) {
	        log.error("No TimeSheet Found For Employee ");
	        return null;
	    }
	}

	
	@Override
	public String getTimeLogsDetailsByTimeSheet(Integer timeSheetId) {
		try {
			log.info("Inside method getTimeLogsDetailsByTimeSheet");
			List<TimeLogsWrapper> timelogsDetails;
			List<TimeLogs> result = new ArrayList<>();
			if (timeSheetId != null) {
				log.debug("timeSheetId coming is : {} ", timeSheetId);
				log.debug(" Inside @getTimeLogsDetailsByTimeSheet  customerId is : {}", commonUtils.getCustomerId());
				result = timeLogsRepository.getTimeLogsByTimeSheetId(timeSheetId,commonUtils.getCustomerId());
			}
			Map<String, Object> responseMap = new HashMap<>();
			if (result != null) {
				timelogsDetails = setTimeLogsDetails(result);
				TimeSheet timesheet = getTimesheetForTimeLogs(result);
				responseMap.put("week", timelogsDetails);
				if (timesheet != null) {
					responseMap.put("timesheet", timesheet);
				}
			}
			ObjectMapper objectMapper = customObjectMapper();
			return objectMapper.writeValueAsString(responseMap);
		} catch (JsonProcessingException e) {
			log.error("Error processing JSON response: {}", e.getMessage());
			return "Error processing JSON response: " + e.getMessage();
		} catch (BusinessException e) {
			log.error("Business exception encountered: {}", e.getMessage());
			return "Business exception encountered: " + e.getMessage();
		} catch (Exception e) {
			log.error("An unexpected error occurred: {}", e.getMessage());
			return "An unexpected error occurred: " + e.getMessage();
		}
	}

	private TimeSheet getTimesheetForTimeLogs(List<TimeLogs> result) {
		TimeSheet timesheet = null;
		try {
			timesheet = (result.get(0).getTimesheetFk() != null ? result.get(0).getTimesheetFk() : null);
		} catch (Exception e) {
			log.error("No TimeSheet Found For Employee ");
		}
		return timesheet;
	}

	public ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("propertyFilter", new PropertyFilter());
		objectMapper.setFilterProvider(filterProvider);
		return objectMapper;
	}

	private List<TimeLogsWrapper> setTimeLogsDetails(List<TimeLogs> result) {
		log.info("Inside method setTimeLogsDetails");
		List<TimeLogsWrapper> timelogsDetails = new ArrayList<>();
		for (TimeLogs timelog : result) {
			TimeLogsWrapper timeLogsWrapper = new TimeLogsWrapper();
			timeLogsWrapper.setTask(getValueOrDefault(timelog.getTaskName()));
			timeLogsWrapper.setProject(timelog.getProjects() != null ? timelog.getProjects() : null);
			timeLogsWrapper.setSunday(getValueOrDefault(timelog.getSunday()));
			timeLogsWrapper.setMonday(getValueOrDefault(timelog.getMonday()));
			timeLogsWrapper.setTuesday(getValueOrDefault(timelog.getTuesday()));
			timeLogsWrapper.setWednesday(getValueOrDefault(timelog.getWednesday()));
			timeLogsWrapper.setThursday(getValueOrDefault(timelog.getThursday()));
			timeLogsWrapper.setFriday(getValueOrDefault(timelog.getFriday()));
			timeLogsWrapper.setSaturday(getValueOrDefault(timelog.getSaturday()));
			timelogsDetails.add(timeLogsWrapper);
		}
		return timelogsDetails;
	}

	public static String getValueOrDefault(Object value) {
		return value != null ? value.toString() : "";
	}

	@Override
	public List<TimeLogs> createOrUpdateTimeLogs(TimeSheetWrapper timeSheetWrapper) {
		log.info("Inside method createOrUpdateTimeLogs");
		List<TimeLogs> timeLogsList = new ArrayList<>();
		for (Map.Entry<String, List<TimeLogsWrapper>> entry : timeSheetWrapper.getWeeklyTimeLogs().entrySet()) {
			log.debug("week found: {} ", entry.getKey());
			List<TimeLogsWrapper> weekLogs = entry.getValue();
			log.debug("weekLogs found: {} ", entry.getValue());
			Employee employee = getEmployeeObject(timeSheetWrapper.getEmpId());
			if (employee != null) {
				log.debug("employee found through userId/Id  is : {} ", employee);
			TimeSheet timeSheet = createOrUpdateTimeSheet(employee, weekLogs, timeSheetWrapper.getWeekNumber(),
					timeSheetWrapper.getTimeSheetStatus(), timeSheetWrapper.getTimeSheetDescription(),timeSheetWrapper.getYear());
			log.debug("timeSheet Created/founded : {} ", timeSheet);
			deleteTimeLogsForEmployee(employee.getId(), timeSheetWrapper.getWeekNumber());
			for (TimeLogsWrapper logsWrapper : weekLogs) {
				TimeLogs timeLogs = setTimeLogsDetailsForCreation(timeSheetWrapper, employee, logsWrapper, timeSheet);
				TimeLogs timelogsCreated = timeLogsRepository.save(timeLogs);
				timeLogsList.add(timelogsCreated);
			 }
			}
		}
		return timeLogsList;
	}

	private void deleteTimeLogsForEmployee(Integer empId, Integer weekNumber) {
		log.info("Inside method deleteTimeLogsForEmployee");
		log.debug(" Inside @deleteTimeLogsForEmployee  customerId is : {}", commonUtils.getCustomerId());
		timeLogsRepository.deleteTimeLogsByEmployeeIdAndWeekNumber(empId, weekNumber,commonUtils.getCustomerId());
		log.info("Timelogs deleted For weeknumber : {} and  Employee having id : {} ", weekNumber, empId);
	}

	/**
	 * @param employee
	 */
	private TimeSheet createOrUpdateTimeSheet(Employee employee, List<TimeLogsWrapper> weekLogs, Integer weekNumber,
			String timeSheetStatus, String timeSheetDescription,Integer year) {
		log.info("Inside method createOrUpdateTimeSheet");
		TimeSheetService timeSheetService = ApplicationContextProvider.getApplicationContext()
				.getBean(TimeSheetService.class);
		TimeSheet timesheet = timeSheetService.findTimeSheetByEmployeeIdAndWeekNumber(employee.getId(), weekNumber);
		if (timesheet != null) {
			return timeSheetService.updateTimeSheetDetails(timesheet, weekLogs, employee, timeSheetStatus,
					timeSheetDescription);
		} else {
			return timeSheetService.createTimeSheet(employee, weekLogs, weekNumber, timeSheetStatus,
					timeSheetDescription,year);
		}
	}

	private Employee getEmployeeObject(Integer empId) {
		log.info("Inside method getEmployeeObject");
		if (empId != null) {
			Employee employee = employeeService.findById(empId);
			if (employee != null) {
				log.debug("employee found through primary key is : {} ", employee);
				return employee;
			}
		} else {
			User contextUser = getUserContext();
			log.debug("contextUser user Id is : {}", contextUser.getUserid());
			Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
			return (employee != null ? employee : null);
		}
		return null;
	}

	/**
	 * @param timeSheetWrapper
	 * @param empId
	 * @param logsWrapper
	 * @return
	 */
	private TimeLogs setTimeLogsDetailsForCreation(TimeSheetWrapper timeSheetWrapper, Employee employee,
			TimeLogsWrapper logsWrapper, TimeSheet timeSheet) {
		log.info("Inside method setTimeLogsDetailsForCreation");
		TimeLogs timeLogs = new TimeLogs();
		if (employee != null) {
			timeLogs.setEmployeeId(employee);
		}
		if (timeSheetWrapper.getYear() != null) {
			timeLogs.setYear(timeSheetWrapper.getYear());
		}
		if (timeSheetWrapper.getWeekNumber() != null) {
			timeLogs.setWeekNo(timeSheetWrapper.getWeekNumber());
		}
		if (timeSheetWrapper.getMonth() != null) {
			timeLogs.setMonth(timeSheetWrapper.getMonth());
		}
		timeLogs.setProjects(logsWrapper.getProject());
		timeLogs.setTimesheetFk(timeSheet);
		timeLogs.setTaskName(logsWrapper.getTask());
		if (logsWrapper.getSunday() != null) {
			timeLogs.setSunday(Integer.parseInt(logsWrapper.getSunday()));
		}
		if (logsWrapper.getMonday() != null) {
			timeLogs.setMonday(Integer.parseInt(logsWrapper.getMonday()));
		}
		if (logsWrapper.getTuesday() != null) {
			timeLogs.setTuesday(Integer.parseInt(logsWrapper.getTuesday()));
		}
		if (logsWrapper.getWednesday() != null) {
			timeLogs.setWednesday(Integer.parseInt(logsWrapper.getWednesday()));
		}
		if (logsWrapper.getThursday() != null) {
			timeLogs.setThursday(Integer.parseInt(logsWrapper.getThursday()));
		}
		if (logsWrapper.getFriday() != null) {
			timeLogs.setFriday(Integer.parseInt(logsWrapper.getFriday()));
		}
		if (logsWrapper.getSaturday() != null) {
			timeLogs.setSaturday(Integer.parseInt(logsWrapper.getSaturday()));
		}
		timeLogs.setTotalMinutes(calculateTotalMinutes(logsWrapper));
		return timeLogs;
	}

	private Integer calculateTotalMinutes(TimeLogsWrapper logsWrapper) {
		log.info("Inside method calculateTotalMinutes");
		Integer sum = 0;
		sum += convertToInt(logsWrapper.getSunday());
		sum += convertToInt(logsWrapper.getMonday());
		sum += convertToInt(logsWrapper.getTuesday());
		sum += convertToInt(logsWrapper.getWednesday());
		sum += convertToInt(logsWrapper.getThursday());
		sum += convertToInt(logsWrapper.getFriday());
		sum += convertToInt(logsWrapper.getSaturday());
		log.debug("total minutes is : {} ", sum);
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

}
