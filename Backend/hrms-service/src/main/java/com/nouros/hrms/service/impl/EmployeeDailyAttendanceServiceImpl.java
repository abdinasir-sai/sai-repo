package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.EmployeeDailyAttendance;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.repository.EmployeeDailyAttendanceRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeDailyAttendanceService;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.HolidayService;
import com.nouros.hrms.service.LeavesService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.payrollmanagement.utils.PRConstant;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeDailyAttendanceServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeDailyAttendanceService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeDailyAttendance and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeDailyAttendance EmployeeDailyAttendance) for exporting the
 * EmployeeDailyAttendance data into excel file by reading the template and
 * mapping the EmployeeDailyAttendance details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeDailyAttendanceServiceImpl extends AbstractService<Integer, EmployeeDailyAttendance>
		implements EmployeeDailyAttendanceService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeDailyAttendance entities.
	 */

	public static final String LEAVE = "Leave";

	public EmployeeDailyAttendanceServiceImpl(GenericRepository<EmployeeDailyAttendance> repository) {
		super(repository, EmployeeDailyAttendance.class);
	}

	private static final Logger log = LogManager.getLogger(EmployeeDailyAttendanceServiceImpl.class);

	@Autowired
	private EmployeeDailyAttendanceRepository employeeDailyAttendanceRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeDailyAttendance The employeeDailyAttendance object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeDailyAttendance create(EmployeeDailyAttendance employeeDailyAttendance) {
		log.info("inside @class EmployeeDailyAttendanceServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeDailyAttendance.setWorkspaceId(workspaceId); // done done
		return employeeDailyAttendanceRepository.save(employeeDailyAttendance);
	}

	@Override
	public Boolean addLeaveforEmployee(String processInstanceId) {
		log.info("Inside @class EmployeeDailyAttendenceServiceImpl @method addLeaveforEmployee ");
		try {
			Boolean flag = true;
			log.debug("Starting of the method addLeaveforEmployee value of Flag :{} ", flag);
			LeavesService leaveService = ApplicationContextProvider.getApplicationContext()
					.getBean(LeavesService.class);
			Leaves leave = leaveService.getLeavesByProcessInstanceId(processInstanceId);
			Integer count = 0;
			EmployeeLeaveTypeService employeeLeaveTypeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeLeaveTypeService.class);
			EmployeeLeaveType employeeLeaveType = employeeLeaveTypeService.getEmployeeLeaveTypeByEmployeeIdAndLeaveType(
					leave.getEmployee().getId(), leave.getLeaveType().getId());
			List<Date> dateList = getDateListByLeave(leave);
			for (Date date : dateList) {
				EmployeeDailyAttendance employeeDailyAttendance = new EmployeeDailyAttendance();
			//	Date time = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
				//log.debug("Inside @class EmployeeDailyAttendenceServiceImpl @method addLeaveforEmployee Date is :{} ",time);
				employeeDailyAttendance.setLoginDate(date);
				employeeDailyAttendance.setStatus(LEAVE);
				employeeDailyAttendance.setLeaves(leave);
				employeeDailyAttendance.setEmployee(leave.getEmployee());
				employeeDailyAttendance.setLeaveType(leave.getLeaveType());
				employeeDailyAttendance.setEmployeeLeaveType(employeeLeaveType);
				create(employeeDailyAttendance);
				count++;
			}
			log.info("No. of Employee Leave Created :{} ", count);
			return flag;
		} catch (Exception e) {
			log.error("Error inside @class EmployeeDailyAttendenceServiceImpl @method addLeaveforEmployee :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	@Override
	public Integer getListOfLeavesBetweenDates(Integer employeeId, Date startDate, Date endDate, Integer leaveTypeId) {
		try {
			Integer count = 0;

			log.debug(
					"Inside @class EmployeeDailyAttendenceServiceImpl @method getListOfLeavesBetweenDates startdate :{} endDate :{} ",
					startDate, endDate);
			log.debug("Inside @class EmployeeDailyAttendenceServiceImpl getListOfLeavesBetweenDates customerId is : {}", commonUtils.getCustomerId());
			List<EmployeeDailyAttendance> listOfEmployeeDailyAttendance = employeeDailyAttendanceRepository
					.getEmployeeLeaveAttendanceBetweenDates(employeeId, startDate, endDate, leaveTypeId, commonUtils.getCustomerId());
			log.debug("THe size of List of employee Daily attendance :{} ", listOfEmployeeDailyAttendance.size());
			for (EmployeeDailyAttendance employeeDailyAttendance : listOfEmployeeDailyAttendance) {
				log.debug("The LoginDate of the EmployeeDailyAttendance :{} ", employeeDailyAttendance.getLoginDate());
				count++;
			}
			return count;
		} catch (Exception e) {
			log.error(
					"Error inside @class EmployeeDailyAttendenceServiceImpl @method getListOfLeavesBetweenDates :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public Integer getListOfLeavesByLeaveTypeId(Integer employeeId, Integer leaveTypeId, Date endDate) {
		try {
			Integer count = 0;

			log.debug(
					"Inside @class EmployeeDailyAttendenceServiceImpl @method getListOfLeavesBetweenDates employeeId :{} leaveTypeId :{} ",
					employeeId, leaveTypeId);
			log.debug("Inside @class EmployeeDailyAttendenceServiceImpl getListOfLeavesByLeaveTypeId customerId is : {}", commonUtils.getCustomerId());
			List<EmployeeDailyAttendance> listOfEmployeeDailyAttendance = employeeDailyAttendanceRepository
					.getEmployeeLeaveAttendanceByLeaveTypeId(employeeId, leaveTypeId, endDate, commonUtils.getCustomerId());
			log.debug("THe size of List of employee Daily attendance :{} ", listOfEmployeeDailyAttendance.size());
			for (EmployeeDailyAttendance employeeDailyAttendance : listOfEmployeeDailyAttendance) {
				log.debug("The LoginDate of the EmployeeDailyAttendance :{} ", employeeDailyAttendance.getLoginDate());
				count++;
			}
			return count;
		} catch (Exception e) {
			log.error(
					"Error inside @class EmployeeDailyAttendenceServiceImpl @method getListOfLeavesBetweenDates :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

//	@Override
//	public Integer getCountOfLeavesBeforeDate(Date startDate ,Integer leaveTypeId,Integer employeeId)
//	{
//		try
//		{
//			log.debug("Inside @class EmployeeDailyAttendenceServiceImpl @method getListOfLeavesBetweenDates startDate :{} employeeId :{} leaveTypeId :{} ",startDate,employeeId,leaveTypeId);
//		 E
//		}
//		catch(Exception e)
//		{
//			log.error("Error inside @class EmployeeDailyAttendenceServiceImpl @method getCountOfLeavesBeforeDate :{} :{} ", e.getMessage(),
//					Utils.getStackTrace(e));
//			throw new BusinessException();  
//		}
//	}

	@Override
	public String changeEmployeeDailyAttendanceStatusByLeaveId(Leaves leave) {
		try {
			log.debug(
					"Inside @class EmployeeDailyAttendanceServiceImpl @method changeEmployeeDailyAttendanceStatusByLeaveId leaveId :{} ",
					leave.getId());
			log.debug("Inside @class EmployeeDailyAttendenceServiceImpl changeEmployeeDailyAttendanceStatusByLeaveId customerId is : {}", commonUtils.getCustomerId());
			List<EmployeeDailyAttendance> employeeDailyAttendances = employeeDailyAttendanceRepository
					.getEmployeeDailyAttendanceByLeaveId(leave.getId(), commonUtils.getCustomerId());
			log.debug(
					"Inside @class EmployeeDailyAttendanceServiceImpl @method changeEmployeeDailyAttendanceStatusByLeaveId size of list :{} ",
					employeeDailyAttendances.size());

			if (!employeeDailyAttendances.isEmpty()) {
				for (EmployeeDailyAttendance employeeDailyAttendance : employeeDailyAttendances) {
					employeeDailyAttendance.setStatus(PRConstant.LEAVE_CANCELLED);
					employeeDailyAttendanceRepository.save(employeeDailyAttendance);
				}

			} else {
				log.debug("Employee Daily Attendance for Leave id not created :{} ", leave.getId());
			}

			return APIConstants.SUCCESS_JSON;

		} catch (Exception e) {
			log.error(
					"Error inside @class EmployeeDailyAttendenceServiceImpl @method changeEmployeeDailyAttendanceStatusByLeaveId :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			return APIConstants.FAILURE_JSON;
		}
	}

	public List<Date> getDateListByLeave(Leaves leave) {
		try {
			log.debug("Inside @class EmployeeDailyAttendanceServiceImpl @method getDateListByLeave  leave Id :{} ",
					leave.getId());
			List<Date> dates = new ArrayList<>();
			if (leave.getLeaveType().getLeaveTypeDay().equalsIgnoreCase(PRConstant.CALENDER_DAYS)) {
				log.info("Leave applied is Calender Days Type ");
				Date fromDate = leave.getFromDate();
				Date toDate = leave.getToDate();
				
				Instant fromInstant = fromDate.toInstant();
		        Instant toInstant = toDate.toInstant();

		        while (!fromInstant.isAfter(toInstant)) {
		            dates.add(Date.from(fromInstant)); // Convert Instant back to Date
		            fromInstant = fromInstant.plus(1, ChronoUnit.DAYS); // Add one day
		        }


			} else if (leave.getLeaveType().getLeaveTypeDay().equalsIgnoreCase(PRConstant.BUSINESS_DAYS)) {
				log.info("Leave applied is Business Days Type ");
				Date fromDate = leave.getFromDate();
				Date toDate = leave.getToDate();
				LocalDate fromLocalDate = convertDateToLocalDate(fromDate);
				LocalDate toLocalDate = convertDateToLocalDate(toDate);
				log.debug("From LocalDate :{} , To LocalDate :{} ", fromLocalDate, toLocalDate);
				HolidayService holidayService = ApplicationContextProvider.getApplicationContext()
						.getBean(HolidayService.class);
				List<Holiday> holidayList = holidayService.findAll();

				Instant fromInstant = fromDate.toInstant();
		        Instant toInstant = toDate.toInstant();

		        while (!fromInstant.isAfter(toInstant)) {
		            dates.add(Date.from(fromInstant)); // Convert Instant back to Date
		            fromInstant = fromInstant.plus(1, ChronoUnit.DAYS); // Add one day
		        }


			}

			return dates;
		} catch (Exception e) {
			log.error(
					"Error inside @class EmployeeDailyAttendenceServiceImpl @method changeEmployeeDailyAttendanceStatusByLeaveId :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private boolean isHoliday(LocalDate date, List<Holiday> holidays) {

		log.debug(" The Date is :{} ", date);
		for (Holiday holiday : holidays) {

			LocalDate dateAsLocalDate = holiday.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (dateAsLocalDate.equals(date)) {
				log.debug("Holiday on :{} ", date);
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer deleteEmployeeDailyAttendanceByLeaveId(Integer leaveId) {
		try {
			log.info(
					"Inside @class EmployeeDailyAttendanceServiceImpl @method deleteEmployeeDailyAttendanceByLeaveId ");
			log.debug("Inside @class EmployeeDailyAttendenceServiceImpl deleteEmployeeDailyAttendanceByLeaveId customerId is : {}", commonUtils.getCustomerId());
			List<EmployeeDailyAttendance> employeeDailyAttendanceList = employeeDailyAttendanceRepository
					.getEmployeeDailyAttendanceByLeaveId(leaveId, commonUtils.getCustomerId());
			Integer count = 0;
			if (!employeeDailyAttendanceList.isEmpty()) {
				for (EmployeeDailyAttendance employeeDailyAttendance : employeeDailyAttendanceList) {
					log.debug(" Login Date of EmployeeDailyAttendance :{} ", employeeDailyAttendance);
					employeeDailyAttendanceRepository.deleteById(employeeDailyAttendance.getId());
					count++;
				}
				log.debug("Employee Daily attendance Deleted by Leave Id ");
			} else {
                   log.debug("Size of Employee Daily Attendance List is Empty :{} ",employeeDailyAttendanceList.size());
			}
			return count;
		} catch (Exception e) {
			log.error(
					"Error Inside @class EmployeeDailyAttendanceServiceImpl @method deleteEmployeeDailyAttendanceByLeaveId :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

}
