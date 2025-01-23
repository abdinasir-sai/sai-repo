package com.nouros.hrms.service.impl;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.time.DayOfWeek;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.repository.EmployeeLeaveTypeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeDailyAttendanceService;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.HolidayService;
import com.nouros.hrms.service.LeaveTypeService;
import com.nouros.hrms.service.LeavesService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.payrollmanagement.model.HrmsSystemConfig;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

import java.util.Map;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * This is a class named "EmployeeLeaveTypeServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "EmployeeLeaveTypeService" interface and it extends the
 * "AbstractService" class, which seems to be a generic class for handling CRUD
 * operations for entities. This class is annotated with @Service, indicating
 * that it is a Spring Service bean. This class is using Lombok's @Slf4j
 * annotation which will automatically generate an Slf4j based logger instance,
 * so it is using the Slf4j API for logging. The class has a constructor which
 * takes a single parameter of GenericRepository EmployeeLeaveType and is used
 * to call the superclass's constructor. This class have one public method
 * public byte[] export(List of EmployeeLeaveType EmployeeLeaveType) for
 * exporting the EmployeeLeaveType data into excel file by reading the template
 * and mapping the EmployeeLeaveType details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeLeaveTypeServiceImpl extends AbstractService<Integer,EmployeeLeaveType>
		implements EmployeeLeaveTypeService {

	private static final String THE_DATES_IN_HAJJ_LEAVES_IS = "The dates in HAJJ leaves is :{} :{} ";

	private static final String YOU_CAN_TAKE_ONLY_2_REMOTE_LEAVE_IN_WEEK = "You can take a maximum of 2 remote leaves per week";

	private static final String ONLY_FEMALE_EMPLOYEE_IS_ELIGIBLE = ". Only Female Employee is eligible";

	private static final String ISLAM = "Islam";

	private static final String MUSLIM = "Muslim";

	private static final String ERROR_INSIDE_CLASS_EMPLOYEE_LEAVE_TYPE_SERVICE_IMPL_METHOD_CREDIT_LEAVE_BY_EMP_ID = "Error inside @Class EmployeeLeaveTypeServiceImpl @Method creditLeaveByEmpId";

	private static final Logger log = LogManager.getLogger(EmployeeLeaveTypeServiceImpl.class);
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String ERROR_MSG = "errorMsg";
	private static final String APPLIED = "applied";
	private static final String BALANCE = "balance";
	private static final String EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTHS = ". Employment duration is less than 3 months";
	private static final String MARRIED = "Married";
	private static final String ONLY_MARRIED_EMPLOYEE_IS_ELIGIBLE = ". Only Married Employee is eligible";
	private static final String EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH = ". Employment duration is less than 3 month";
	private static final String DEAR = "Dear ";
	private static final String YOU_ARE_NOT_ELIGIBLE_FOR = " you are not eligible for ";
	private static final Integer annualDefinateContract = 20;
	private static final Integer inDefinateContract = 60;
	//private static final String ;

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeLeaveType entities.
	 */

	public EmployeeLeaveTypeServiceImpl(GenericRepository<EmployeeLeaveType> repository) {
		super(repository, EmployeeLeaveType.class);
	}

	@Autowired
	private EmployeeLeaveTypeRepository employeeLeaveTypeRepository;
	
	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	private CommonUtils commonUtils;


	/**
	 * Creates a new vendor.
	 *
	 * @param employeeLeaveType The employeeLeaveType object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeLeaveType create(EmployeeLeaveType employeeLeaveType) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeLeaveType.setWorkspaceId(workspaceId); // done done
		return employeeLeaveTypeRepository.save(employeeLeaveType);
	}

	private EmployeeLeaveType findByEmployeeIdAndLeaveType(Integer empId, Integer leaveType) {
		EmployeeLeaveType employeeLeaveType = employeeLeaveTypeRepository.findByEmployeeIdAndLeaveTypeId(empId,
				leaveType);
		if (employeeLeaveType == null) {
			throw new BusinessException("There is no leave balance found for give leave type");
		}
		return employeeLeaveType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String leaveValidate(Integer id, Integer leaveType, Date fromDate, Date toDate, Integer duration,
			LeaveType leaveTypeObj) {
		log.debug(" Inside @leaveValidate  customerId is : {}", commonUtils.getCustomerId());
		JSONObject obj = new JSONObject();
		try {
			if (null != fromDate && null == toDate && null != duration) {
				toDate = calculateEndDate(fromDate, duration);
				log.info("Inside method leaveValidate toDate {}", toDate);
			} else if (null != fromDate && null == toDate && null == duration) {
				toDate = calculateEndDate(fromDate, 1);
				log.info("Inside method leaveValidate toDate {}", toDate);
			}
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			log.debug("Inside @leaveValidate Leave Type Id:{} ",leaveTypeObj.getId());
			Employee employee = employeeService.getEmployeeByUserId(id);
			int leaveDuration = getDiffDayBetweenDates(fromDate, toDate);
			int employeeMentMonths = getDiffMonthsBetweenDates(employee.getDateOfJoining(), new Date());
			EmployeeLeaveType employeeLeaveType = findByEmployeeIdAndLeaveType(employee.getId(), leaveType);
			String leaveTypeLabel = employeeLeaveType.getLeaveType().getName();
			Boolean  serverValue = checkServerValue(); 
			log.debug("Value of ServerValue :{} ",serverValue);
			if (leaveTypeObj.getName().equalsIgnoreCase(PRConstant.REMOTE_WORKING) && serverValue) {
				log.info("inside method leaveValidate it is Remote Leave ");
            log.debug("Current Server is SAI :{} ",serverValue);
				int businessDayLeaveDuration = getBusinessDayLeave(fromDate, toDate);
				log.debug("The value of BusinessLeaveDuration is :{} ", businessDayLeaveDuration);
				 isEmployeeElegibleForRemoteLeave(employee, fromDate, toDate, leaveType,leaveTypeLabel );
				obj.put(BALANCE, "");
				obj.put(APPLIED, businessDayLeaveDuration);
				obj.put(ERROR_MSG, "");
				return obj.toJSONString();

			}
			else if (leaveTypeObj.getName().equalsIgnoreCase(PRConstant.REMOTE_WORKING) && !serverValue ) {
			    log.debug("Current Server is SIT :{} ",serverValue);
			    log.info("inside method leaveValidate it is Remote Leave ");

				int businessDayLeaveDuration = getBusinessDayLeave(fromDate, toDate);
				log.debug("The value of BusinessLeaveDuration is :{} ", businessDayLeaveDuration);
			 				obj.put(BALANCE, "");
				obj.put(APPLIED, businessDayLeaveDuration);
				obj.put(ERROR_MSG, "");
				return obj.toJSONString();
			}else {
				checkLeaveEligiblity(leaveTypeLabel, employeeMentMonths, employee, leaveType,fromDate,toDate);
				return checkLeaveBalance(employeeLeaveType, leaveDuration, fromDate, toDate, obj);
			}

		} catch (BusinessException e) {
			log.error("Error occured inside EmployeeLeaveTypeService @Method leaveValidate {},{}",
					Utils.getStackTrace(e), e);
			obj.put(BALANCE, "");
			obj.put(APPLIED, "");
			obj.put(ERROR_MSG, e.getMessage());
			return obj.toJSONString();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private void checkLeaveEligiblity(String leaveTypeLabel, int employeeMentMonths, Employee employee,Integer leaveType,Date fromDate,Date toDate) {
		checkLeave(employee, employeeMentMonths, leaveTypeLabel,leaveType,fromDate,toDate);
	}

	@SuppressWarnings("unchecked")
	private String checkLeaveBalance(EmployeeLeaveType employeeLeaveType, int leaveDuration, Date fromDate, Date toDate,
			JSONObject obj) {
		if(employeeLeaveType.getLeaveType()!=null && employeeLeaveType.getLeaveType().getLeaveTypeDay() != null
				&& employeeLeaveType.getLeaveType().getName().equalsIgnoreCase(PRConstant.SICK_LEAVE))
		{
			obj.put(BALANCE, employeeLeaveType.getBalance());
			obj.put(APPLIED, leaveDuration);
			obj.put(ERROR_MSG, "");
			return obj.toJSONString();
		}
		
		if (employeeLeaveType.getLeaveType() != null && employeeLeaveType.getLeaveType().getLeaveTypeDay() != null
				&& employeeLeaveType.getLeaveType().getLeaveTypeDay().equalsIgnoreCase("Business days")) {
			int businessDayLeaveDuration = getBusinessDayLeave(fromDate, toDate);
			if (employeeLeaveType.getBalance() < businessDayLeaveDuration) {
				obj.put(BALANCE, employeeLeaveType.getBalance());
				obj.put(APPLIED, leaveDuration);
				obj.put(ERROR_MSG, "You cannot take leave more than your leave balance");
				return obj.toJSONString();
			} else {
				obj.put(BALANCE, employeeLeaveType.getBalance());
				obj.put(APPLIED, businessDayLeaveDuration);
				obj.put(ERROR_MSG, "");
				return obj.toJSONString();
			}
		} else {
			if (employeeLeaveType.getBalance() < leaveDuration) {
				obj.put(BALANCE, employeeLeaveType.getBalance());
				obj.put(APPLIED, leaveDuration);
				obj.put(ERROR_MSG, "You cannot take leave more than your leave balance");
				return obj.toJSONString();
			} else {
				obj.put(BALANCE, employeeLeaveType.getBalance());
				obj.put(APPLIED, leaveDuration);
				obj.put(ERROR_MSG, "");
				return obj.toJSONString();
			}
		}
	}

	@Override
	public int getBusinessDayLeave(Date fromDate, Date toDate) {
		HolidayService holidayService = ApplicationContextProvider.getApplicationContext()
				.getBean(HolidayService.class);
		List<Holiday> holidayList = holidayService.findAll();
		return calculateBusinessDays(fromDate, toDate, holidayList);
	}

	private void checkLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel, Integer leaveType,Date fromDate,Date toDate) {

		switch (leaveTypeLabel) {
		case "Annual leave":
			isEmployeeElegibleForcheckAnnualLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType,fromDate);
			break;
		case "Sick leave":
			isEmployeeElegibleForcheckSickLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Hajj leave":
			isEmployeeElegibleForHajjLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType,fromDate);
			break;
		case "Paternity leave":
			isEmployeeElegibleForPaternityLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Maternity leave":
			isEmployeeElegibleForMaternityLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Marriage leave":
			isEmployeeElegibleForMarriageLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Bereavement leave":
			isEmployeeElegibleForBereavementLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Iddah leave for non-Muslims":
			isEmployeeElegibleForEddaLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Emergency leave":
			isEmployeeElegibleForEmergencyLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Exam leave":
			isEmployeeElegibleForExamLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType,fromDate);
			break;
		case "Iddah leave for Muslims":
			isEmployeeElegibleForEddaLeaveForMuslim(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;
		case "Unpaid leave":
			isEmployeeElegibleForUnpaidLeave(employee, employeeMentMonths, leaveTypeLabel, leaveType);
			break;

		case "Remote working":
			isEmployeeElegibleForRemoteLeave(employee, fromDate, toDate, leaveType, leaveTypeLabel);
			break;
		default:
			// Code for default case
			break;
		}
	}

	private void isEmployeeElegibleForcheckAnnualLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType,Date fromDate) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTHS);
		}
		LocalDate date = LocalDate.now();
		LocalDate fromLocalDate = convertDateToLocalDate(fromDate);
		log.debug(THE_DATES_IN_HAJJ_LEAVES_IS,date,fromLocalDate);
		 

	}

	private void isEmployeeElegibleForcheckSickLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTHS);
		}
	}

	private void isEmployeeElegibleForHajjLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType,Date fromDate) {
		EmployeeLeaveType employeeAnnualLeaveType = employeeLeaveTypeRepository.findByEmployeeIdAndLeaveTypeId(employee.getId(),leaveType );
	 		if (employeeMentMonths <= 24) {
	 			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
						+ ". Employment duration is less than 2 year");
	 			} else if (employee != null && employee.getReligion() != null
					&& ((!employee.getReligion().equalsIgnoreCase(MUSLIM))
							&& (!employee.getReligion().equalsIgnoreCase(ISLAM)))) {
				throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
						+ ". Only Muslim Employee is eligible");
			}
			if(employeeAnnualLeaveType.getBalance()<14)
			{
				throw new BusinessException("You can take Hajj leave only once ");
			}
			LocalDate date = LocalDate.now();
			LocalDate fromLocalDate = convertDateToLocalDate(fromDate);
			log.debug(THE_DATES_IN_HAJJ_LEAVES_IS,date,fromLocalDate);
			long daysBetween = ChronoUnit.DAYS.between(date,fromLocalDate);
			log.debug("difference between the date and Date Entered is :{} ",daysBetween);
			if(daysBetween <= 30)
			{
				throw new BusinessException("Leave request must be submitted at least 30 days before the leave commencement date");
			}

		 	}

	private void isEmployeeElegibleForPaternityLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTHS);
		} else if (employee != null && employee.getGender() != null && !employee.getGender().equalsIgnoreCase("Male")) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ". Only Male Employee is eligible");
		} else if (employee != null && employee.getMaritalStatus() != null
				&& !employee.getMaritalStatus().equalsIgnoreCase(MARRIED)) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_MARRIED_EMPLOYEE_IS_ELIGIBLE);
		}
	}

	private void isEmployeeElegibleForMaternityLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
//		if (employeeMentMonths <= 12) {
//			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
//					+ ". Employment duration is less than 1 year");
//		} 
		if (employee != null && employee.getGender() != null && employee.getGender().equalsIgnoreCase("Male")) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_FEMALE_EMPLOYEE_IS_ELIGIBLE);
		} else if (employee != null && employee.getMaritalStatus() != null
				&& !employee.getMaritalStatus().equalsIgnoreCase(MARRIED)) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_MARRIED_EMPLOYEE_IS_ELIGIBLE);
		}
	}

	private void isEmployeeElegibleForMarriageLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		}

	}

	private void isEmployeeElegibleForBereavementLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		}
	}

	private void isEmployeeElegibleForEddaLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		} else if (employee != null && employee.getGender() != null
				&& !employee.getGender().equalsIgnoreCase("Female")) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_FEMALE_EMPLOYEE_IS_ELIGIBLE);
		} else if (employee != null && employee.getMaritalStatus() != null
				&& !employee.getMaritalStatus().equalsIgnoreCase(MARRIED)) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_MARRIED_EMPLOYEE_IS_ELIGIBLE);
		} else if (employee != null && employee.getReligion() != null
				&& ((employee.getReligion().equalsIgnoreCase(MUSLIM)
						|| employee.getReligion().equalsIgnoreCase(ISLAM)))) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ " Only Non - Muslims Employee are eligible ");
		}
	}

	private void isEmployeeElegibleForEddaLeaveForMuslim(Employee employee, int employeeMentMonths,
			String leaveTypeLabel, Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		} else if (employee != null && employee.getGender() != null
				&& !employee.getGender().equalsIgnoreCase("Female")) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_FEMALE_EMPLOYEE_IS_ELIGIBLE);
		} else if (employee != null && employee.getMaritalStatus() != null
				&& !employee.getMaritalStatus().equalsIgnoreCase(MARRIED)) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ONLY_MARRIED_EMPLOYEE_IS_ELIGIBLE);
		} else if (employee != null && employee.getReligion() != null
				&& ((!employee.getReligion().equalsIgnoreCase(MUSLIM))
						&& (!employee.getReligion().equalsIgnoreCase(ISLAM)))) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ". Only Muslim Employee is eligible");
		}
	}

	private void isEmployeeElegibleForEmergencyLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		}

	}

	private void isEmployeeElegibleForExamLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType,Date fromDate) {
		if (employeeMentMonths <= 3) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ EMPLOYMENT_DURATION_IS_LESS_THAN_3_MONTH);
		}
		LocalDate date = LocalDate.now();
		LocalDate fromLocalDate = convertDateToLocalDate(fromDate);
		log.debug("The Dates for Exam Leave :{} :{}",date,fromLocalDate);
		long daysBetween = ChronoUnit.DAYS.between(date,fromLocalDate);
		log.debug("difference between the date and Date Entered is :{} ",daysBetween);
        if(daysBetween<=14)
        {
        	throw new BusinessException("Leave request must be submitted at least 14 days before the leave commencement date");
        }
	}

	private void isEmployeeElegibleForRemoteLeave(Employee employee, Date fromDate, Date toDate,
			Integer id, String leaveTypeLabel) {
		EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
		log.debug("Inside @method isEmployeeElegibleForRemoteLeave  :{} :{} ", fromDate, toDate);
		Integer diff = getDiffDayBetweenDates(fromDate,toDate);
		log.debug(" Difference Between from Date and to Date :{} ",diff);
		if(diff>3)
		{
			throw new BusinessException("You cannot take Remote Leave more than 3 Days");
		}
	    LocalDate localFromDate = convertDateToLocalDate(fromDate);
	    LocalDate localToDate = convertDateToLocalDate(toDate);
	    log.debug(" in Remote Working localFromDate :{} localToDate :{} ",fromDate,toDate);
         String fromDateMonth = localFromDate.getMonth().toString();
         String toDateMonth = localToDate.getMonth().toString();
         log.debug("Value of fromDateMonth :{} toDateMonth :{} ",fromDateMonth,toDateMonth);
         if(fromDateMonth.equalsIgnoreCase(toDateMonth))
         {
        	 LocalDate monthLocalStartDate = localFromDate.withDayOfMonth(1);
             LocalDate monthLocalEndDate = localFromDate.with(TemporalAdjusters.lastDayOfMonth());
            Date monthStartDate = Date.from(monthLocalStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date monthEndDate = Date.from(monthLocalEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
             log.debug(" Start Date and endDate of the month :{} :{}",monthStartDate,monthEndDate);
             Integer count = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(), monthStartDate, monthEndDate, id);
            log.debug("Inside isEmployeeElegibleForRemoteLeave The Count of list of EmployeeDailyAttendance :{} ",count);
             if(count>=3 || (diff + count)>3)
            	 throw new BusinessException("You cannot take Remote Leave more than 3 in month");
            	 
         }
         else
         {
        	 LocalDate monthLocalStartDate = localFromDate.withDayOfMonth(1);
             LocalDate monthLocalEndDate = localFromDate.with(TemporalAdjusters.lastDayOfMonth());
            Date monthStartDate = Date.from(monthLocalStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date monthEndDate = Date.from(monthLocalEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
             log.debug(" Start Date and endDate of the month :{} :{} in Date start and end :{} :{}",monthLocalStartDate,monthLocalEndDate,
            		 monthStartDate,monthEndDate );
             LocalDate anotherMonthLocalStartDate = localToDate.withDayOfMonth(1);
             LocalDate anotherMonthLocalEndDate = localToDate.with(TemporalAdjusters.lastDayOfMonth());
             Date anotherMonthStartDate = Date.from(anotherMonthLocalStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
             Date anotherMonthEndDate = Date.from(anotherMonthLocalEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
             log.debug(" Start Date and endDate of the another month :{} :{} inDate:{} :{}",anotherMonthLocalStartDate,anotherMonthLocalEndDate
            		 ,anotherMonthStartDate,anotherMonthEndDate);
             Integer count = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(), monthStartDate, monthEndDate, id);
            log.debug("Inside else The Count of list of EmployeeDailyAttendance :{} ",count);
            Integer count1 = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(), anotherMonthStartDate, anotherMonthEndDate, id);
           log.debug(" The Count of list of EmployeeDailyAttendance :{} ",count1);
           if(count>=3 || count1 >=3 || (count1+count+diff)>3 || (count+diff)>=3 || (count1+diff)>=3  )
        	   throw new BusinessException("You cannot take Remote Leave more than 3 in month");
             
         }
	    
	}

	//checks for Unpaid leave
	private void isEmployeeElegibleForUnpaidLeave(Employee employee, int employeeMentMonths, String leaveTypeLabel,
			Integer leaveType) {
		EmployeeLeaveType employeeLeaveType = employeeLeaveTypeRepository
				.findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveType);//finding employeeLeave Type of Unpaid leave
		log.debug("The Employment Type of Employee is :{} ",employee.getEmploymentType());
		if (!employee.getEmploymentType().equalsIgnoreCase(PRConstant.DIRECT_HIRE)
				&& !employee.getEmploymentType().equalsIgnoreCase(PRConstant.FULL_TIME)) {
			throw new BusinessException("Only Direct Hire or Full Time Employee are eligible");
		}
		if (employeeMentMonths <= 12) {
			throw new BusinessException(DEAR + employee.getFirstName() + YOU_ARE_NOT_ELIGIBLE_FOR + leaveTypeLabel
					+ ". Employment duration is less than 1 year");
		}
		 
		LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext().getBean(LeaveTypeService.class);
		LeaveType leaveTypeAnnualLeave = leaveTypeService.findByName(PRConstant.ANNUAL_LEAVE);
		EmployeeLeaveType employeeAnnualLeaveType = employeeLeaveTypeRepository.findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveTypeAnnualLeave.getId());
		if(employeeAnnualLeaveType.getBalance()!=0)
		{
			throw new BusinessException("Your Annual Leave Balance is not 0");
		}
	}

	public static int getDiffDayBetweenDates(Date dateBefore, Date dateAfter) {
		log.info("inside getDiffDayBetweenDates:::  dateBefore {} dateAfter {}", dateBefore, dateAfter);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			LocalDate startDate = LocalDate.parse(sdf.format(dateBefore));
			LocalDate endtDate = LocalDate.parse(sdf.format(dateAfter));
			Long range = ChronoUnit.DAYS.between(startDate, endtDate);
			log.info("date diff in java8 {}", range);
			int modifiedRange = range.intValue() + 1;
			log.info("modifiedRange {}", modifiedRange);
			return modifiedRange;
		} catch (Exception e) {
			log.warn("Error in side getDiffBetweenDates ", e);
			return 0;
		}
	}

	public static int getDiffMonthsBetweenDates(Date dateBefore, Date dateAfter) {
		log.info("inside getDiffMonthsBetweenDates:::  dateBefore {} dateAfter {}", dateBefore, dateAfter);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			LocalDate startDate = LocalDate.parse(sdf.format(dateBefore));
			LocalDate endtDate = LocalDate.parse(sdf.format(dateAfter));
			Long range = ChronoUnit.MONTHS.between(startDate, endtDate);
			log.info("date diff in java8 {}", range);
			return range.intValue();
		} catch (Exception e) {
			log.warn("Error in side getDiffBetweenDates ", e);
			return 0;
		}
	}

	public int calculateBusinessDays(Date fromDate, Date toDate, List<Holiday> holidays) {
		int businessDays = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		LocalDate startDate = LocalDate.parse(sdf.format(fromDate));
		LocalDate endtDate = LocalDate.parse(sdf.format(toDate));

		for (LocalDate currentDate = startDate; !currentDate.isAfter(endtDate); currentDate = currentDate.plusDays(1)) {
			if (!isHoliday(currentDate, holidays)) {
				businessDays++;
			}
		}

		return businessDays;
	}

	private boolean isHoliday(LocalDate date, List<Holiday> holidays) {

		for (Holiday holiday : holidays) {

			LocalDate dateAsLocalDate = holiday.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (dateAsLocalDate.equals(date)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String creaditLeaveByEmpId(Integer empId) {
		try {
			List<EmployeeLeaveType> employeeLeaveTypes = employeeLeaveTypeRepository.findByEmployeeId(empId);
			if (employeeLeaveTypes.isEmpty()) {
				EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
						.getBean(EmployeeService.class);
				Employee employee = employeeService.findById(empId);
				if (employee != null) {
					Employee emp = employee;
					LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext()
							.getBean(LeaveTypeService.class);
					List<LeaveType> leaveTypes = leaveTypeService.findAll();
					if (!leaveTypes.isEmpty()) {

						List<EmployeeLeaveType> employeeLeaveTypeList = new ArrayList<>();

						for (LeaveType leaveType : leaveTypes) {
							EmployeeLeaveType empleavetype = new EmployeeLeaveType();
							empleavetype.setLeaveType(leaveType);
							empleavetype.setEmployeeId(emp);
							empleavetype.setBalance(leaveType.getOpeningBalance());
							empleavetype.setTotalBalance(leaveType.getOpeningBalance());
							Date curruntDay = new Date();
							LocalDate localDate = curruntDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							LocalDate yearEnd = localDate.plusMonths(12);
							empleavetype.setYearStartDate(curruntDay);
							empleavetype.setYearEndDate(
									Date.from(yearEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()));
							employeeLeaveTypeList.add(empleavetype);
						}
						employeeLeaveTypeRepository.saveAll(employeeLeaveTypeList);
					}
				}

			}
		} catch (Exception e) {
			String errorMessage = ERROR_INSIDE_CLASS_EMPLOYEE_LEAVE_TYPE_SERVICE_IMPL_METHOD_CREDIT_LEAVE_BY_EMP_ID;
			throw new BusinessException(errorMessage, e);
		}
		return APIConstants.SUCCESS_JSON;
	}

	@Override
	public void creaditLeave() {
		try {

			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			List<Employee> employee = employeeService.findEmployeesWhoCompletedThreeMonths();
			log.info("employee list {}", employee.size());
			LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext()
					.getBean(LeaveTypeService.class);
			List<LeaveType> leaveTypes = leaveTypeService.findAll();
			log.info("leaveTypes {}", leaveTypes.size());

			for (Employee emp : employee) {

				List<EmployeeLeaveType> employeeLeaveTypes = employeeLeaveTypeRepository.findByEmployeeId(emp.getId());
				log.info("employeeLeaveTypes list size {}", employeeLeaveTypes.size());

				if (employeeLeaveTypes.isEmpty()) {
					log.info("Inside if employeeLeaveTypes list is empty ");

					if (!leaveTypes.isEmpty()) {
						log.info("Inside if leaveTypes list is not empty ");

						List<EmployeeLeaveType> employeeLeaveTypeList = new ArrayList<>();
						for (LeaveType leaveType : leaveTypes) {
							EmployeeLeaveType empleavetype = new EmployeeLeaveType();
							empleavetype.setLeaveType(leaveType);
							empleavetype.setEmployeeId(emp);
							empleavetype.setBalance(leaveType.getOpeningBalance());
							empleavetype.setTotalBalance(leaveType.getOpeningBalance());
							Date curruntDay = new Date();
							LocalDate localDate = curruntDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							LocalDate yearEnd = localDate.plusMonths(12);
							empleavetype.setYearStartDate(curruntDay);
							empleavetype.setYearEndDate(
									Date.from(yearEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()));
							employeeLeaveTypeList.add(empleavetype);
						}
						employeeLeaveTypeRepository.saveAll(employeeLeaveTypeList);
					}
				}
			}

		} catch (Exception e) {
			String errorMessage = ERROR_INSIDE_CLASS_EMPLOYEE_LEAVE_TYPE_SERVICE_IMPL_METHOD_CREDIT_LEAVE_BY_EMP_ID;
			throw new BusinessException(errorMessage, e);
		}

	}

	@Override
	public void updateLeaveBalanceWhenYearComplete() {
		try {
			Boolean  serverValue = checkServerValue(); 
			log.debug("Server Value Method updateLeaveBalanceWhenYearComplete :{} ",serverValue);
			if(serverValue)
			{
				log.debug("The Server is SAI :{}",serverValue);
				saiUpdateBalanceValue();
			}
			else {
				sitUpdateBalanceValue();
			}
		} catch (Exception e) {
 
			 log.error("Error Inside @class EmployeeLeaveType @method updateLeaveBalanceWhenYearComplete :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			 throw new BusinessException();
		}

	}

	public Date calculateEndDate(Date startDate, int duration) {
		log.info(String.format("Inside calculateEndDate::: startDate %s duration %d", startDate, duration));
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			LocalDate localStartDate = LocalDate.parse(sdf.format(startDate));
			LocalDate localEndDate = localStartDate.plusDays(duration);
			Date endDate = java.sql.Date.valueOf(localEndDate);
			log.info(String.format("Calculated end date: %s", endDate));
			return endDate;
		} catch (Exception e) {
			log.error(String.format("Error inside calculateEndDate: %s", e));
			return null;
		}
	}

//	public void checkForRemoteWorking(Date fromDate, Date toDate, Employee employee, Integer id,
//			String leaveTypeLabel) {
//
//		log.info("Inside class EmployeeLeaveType @method checkForRemoteWorking ");
//		log.debug("Inside class EmployeeLeaveType @method checkForRemoteWorking :{} :{} ", fromDate, toDate);
//		LocalDate startDate = convertDateToLocalDate(fromDate);
//		LocalDate endDate = convertDateToLocalDate(toDate);
//
//		LocalDate sunday1 = startDate.with(DayOfWeek.SUNDAY);
//		if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//			sunday1 = sunday1.minusWeeks(1);
//		}
//
//		LocalDate thursday1 = sunday1.plusDays(4);
//
//		int week1 = getWeekNumber(startDate);
//		int week2 = getWeekNumber(endDate);
//
//		LocalDate sunday2 = endDate.with(DayOfWeek.SUNDAY);
//		if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//			sunday2 = sunday2.minusWeeks(1);
//		}
//
//		LocalDate thursday2 = sunday2.plusDays(4);
//
//		log.debug("the value of week1 :{} week2 :{} ", week1, week2);
//		log.debug(" sunday1:{}  thursday1:{}  sunday2:{} thursday2 :{} ", sunday1, thursday1, sunday2, thursday2);
//		if (week1 == week2) {
//			Integer daysBetween = getDiffDayBetweenDates(fromDate, toDate);
//			Integer sum = isEmployeeElegibleForRemoteLeave(employee, sunday1, thursday1, id, leaveTypeLabel);
//			log.debug("inside checkForRemoteWorking value of daysBetween , count is :{} :{}", daysBetween, sum);
//			if (sum == null) {
//				sum = 0;
//			}
//			if (daysBetween > 2 || sum >= 2 || (sum + daysBetween) > 2) {
//				throw new BusinessException(YOU_CAN_TAKE_ONLY_2_REMOTE_LEAVE_IN_WEEK);
//			}
//		} else {
//
//			Long diffBetweenFirstWeek = ChronoUnit.DAYS.between(startDate, thursday1);
//			Long diffBetweenSecondWeek = ChronoUnit.DAYS.between(sunday2, endDate);
//			Integer count = isEmployeeElegibleForRemoteLeave(employee, sunday2, thursday2, id, leaveTypeLabel);
//			log.debug("The value of diffBetweenFirstWeek :{} diffBetweenSecondWeek :{} count :{}", diffBetweenFirstWeek,
//					diffBetweenSecondWeek, count);
//			if (count == null) {
//				count = 0;
//			}
//			if (count >= 2 || diffBetweenFirstWeek > 2 || (diffBetweenFirstWeek + count) > 2) {
//				throw new BusinessException(YOU_CAN_TAKE_ONLY_2_REMOTE_LEAVE_IN_WEEK);
//			}
//
//			else if (diffBetweenSecondWeek > 2) {
//				throw new BusinessException(YOU_CAN_TAKE_ONLY_2_REMOTE_LEAVE_IN_WEEK);
//			}
//		}
//
//	}

	public static int getWeekNumber(LocalDate date) {

		WeekFields weekFields = WeekFields.of(Locale.getDefault());

		return date.get(weekFields.weekOfWeekBasedYear());

	}

	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	public EmployeeLeaveType getEmployeeLeaveTypeByEmployeeIdAndLeaveType(Integer employeeId,Integer leaveTypeId)
	{
		try
		{
			log.debug("inside @class EmployeeLeaveTypeServiceImpl @method getEmployeeLeaveTypeByEmployeeIdAndLeaveType EmployeeID :{} leaveTypeId :{} ",
					employeeId,leaveTypeId);
			EmployeeLeaveType employeeLeaveType = employeeLeaveTypeRepository.getEmployeeLeaveTypeByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId,commonUtils.getCustomerId());
			return employeeLeaveType;
		}
		catch(Exception e)
		{
			log.error("Error inside @class EmployeeLeaveTypeServiceImpl @method getEmployeeLeaveTypeByEmployeeIdAndLeaveType :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();  
		}
	}
	public Boolean checkServerValue()
	{
		try
		{
			log.info("Inside @class  EmployeeLeaveTypeServiceImpl @method checkServerValue  ");
			String value =  hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME);
			if(value.equalsIgnoreCase("SAI"))
			 {
				 log.info("Current Server is SAI");
				 return true;
			 }
			 return false;
		}
		catch(Exception e)
		{
			log.error("Error inside @class EmployeeLeaveTypeServiceImpl @method checkServerValue :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();  
		}
	}
	public void saiUpdateBalanceValue()
	{
		try {
			List<EmployeeLeaveType> employeeLeaveTypes = employeeLeaveTypeRepository
					.findAll();
			log.debug("Inside @class EmployeeLeaveType @method saiUpdateBalanceValue size of list :{}",employeeLeaveTypes.size());
			List<EmployeeLeaveType> employeeLeaveTypeList = new ArrayList<>();
			for (EmployeeLeaveType employeeLeaveType : employeeLeaveTypes) {
				double restBalance=0.0;
				log.debug("Changing EmployeeLeaveType :{} Employee :{}",employeeLeaveType.getId(),employeeLeaveType.getEmployeeId().getId());
		  Map<String ,String> hrmsSystemMap = hrmsSystemConfigService.getHrmsKeyValue();   
		   String carryForwards = hrmsSystemMap.get(PRConstant.CARRY_FORWARD_LIST);
		   String[] leaveList = carryForwards.split(",");
		   log.debug("Inside saiUpdateBalanceValue Carry forward list :{} ",leaveList);
		   if(leaveList==null)
		   {
			   log.error(" Leave List for carry forward is Null :{} ",leaveList);
		   }
		   for(String leaveName :leaveList)
		   {
			   log.debug(" Name of leave :{} ",leaveName);
			   if(employeeLeaveType.getLeaveType().getName().equalsIgnoreCase(leaveName))
			   {
				if(leaveName.equalsIgnoreCase(PRConstant.ANNUAL_LEAVE_SML))
				{
                  restBalance = employeeLeaveType.getBalance();
				  log.debug(" Balance for leave :{} :{} ",restBalance,PRConstant.ANNUAL_LEAVE_SML);
				  if(restBalance>=10) {restBalance=10;}
                   Double totalBalance = employeeLeaveType.getLeaveType().getOpeningBalance() +restBalance;
				   log.debug("The total Balance is :{} ",restBalance);
				   employeeLeaveType.setTotalBalance(totalBalance);  
				   restBalance = totalBalance;
				  log.debug("The rest Balance is :{} ",restBalance);
				}
				else
				   restBalance = employeeLeaveType.getLeaveType().getOpeningBalance();
			   }
			   else
			   {
		 		     restBalance = employeeLeaveType.getLeaveType().getOpeningBalance();				   
			   }
			   log.debug(" Rest Balance :{} for Leave :{} ",restBalance,employeeLeaveType.getLeaveType().getName());
					employeeLeaveType.setBalance(restBalance);
					employeeLeaveTypeList.add(employeeLeaveType);			   
		   }

			}
			employeeLeaveTypeRepository.saveAll(employeeLeaveTypeList);
		} catch (Exception e) {
 
			 log.error("Error Inside @class EmployeeLeaveType @method saiUpdateBalanceValue :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			 throw new BusinessException();
		}

	}
	public void sitUpdateBalanceValue()
	{
		try {
			List<EmployeeLeaveType> employeeLeaveTypes = employeeLeaveTypeRepository
					.findAll();
			log.debug("Inside @class EmployeeLeaveType @method updateLeaveBalanceWhenYearComplete size of list :{}",employeeLeaveTypes.size());
			List<EmployeeLeaveType> employeeLeaveTypeList = new ArrayList<>();
			for (EmployeeLeaveType employeeLeaveType : employeeLeaveTypes) {
				double restBalance=0.0;
				log.debug("Changing EmployeeLeaveType :{} Employee :{}",employeeLeaveType.getId(),employeeLeaveType.getEmployeeId().getId());
		  Map<String ,String> hrmsSystemMap = hrmsSystemConfigService.getHrmsKeyValue();   
		   String carryForwards = hrmsSystemMap.get(PRConstant.CARRY_FORWARD_LIST);
		   String[] leaveList = carryForwards.split(",");
		   log.debug("Inside updateLeaveBalanceWhenYearCompelete Carry forward list :{} ",leaveList);
		   if(leaveList==null)
		   {
			   log.error(" Leave List for carry forward is Null :{} ",leaveList);
		   }
		   for(String leaveName :leaveList)
		   {
			   log.debug(" Name of leave :{} ",leaveName);
			   if(employeeLeaveType.getLeaveType().getName().equalsIgnoreCase(leaveName))
			   {
				   restBalance = employeeLeaveType.getTotalBalance()+employeeLeaveType.getBalance();
			   }
			   else
			   {
		 		     restBalance = employeeLeaveType.getTotalBalance();				   
			   }
			   log.debug(" Rest Balance :{} for Leave :{} ",restBalance,employeeLeaveType.getLeaveType().getName());
					employeeLeaveType.setBalance(restBalance);
					employeeLeaveTypeList.add(employeeLeaveType);			   
		   }

			}
			employeeLeaveTypeRepository.saveAll(employeeLeaveTypeList);
		} catch (Exception e) {
 
			 log.error("Error Inside @class EmployeeLeaveType @method updateLeaveBalanceWhenYearComplete :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			 throw new BusinessException();
		}

	}
}
