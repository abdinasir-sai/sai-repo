package com.nouros.hrms.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Optional;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.integration.service.impl.NotificationIntegrationImpl;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeDailyAttendance;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.repository.EmployeeLeaveTypeRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.LeaveTypeRepository;
import com.nouros.hrms.repository.LeavesRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeDailyAttendanceService;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.LeavesService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.LeavesDto;
import com.nouros.hrms.wrapper.UnpaidLeaveWrapper;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

import jakarta.validation.Valid;

/**
 * This is a class named "LeavesServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "LeavesService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Leaves and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Leaves Leaves)
 * for exporting the Leaves data into excel file by reading the template and
 * mapping the Leaves details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class LeavesServiceImpl extends AbstractService<Integer, Leaves> implements LeavesService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Leaves entities.
	 */

	private static final String CANNOT_DEDUCTED_LEAVE = "Cannot Deducted Leave";
	
	private static final Logger log = LogManager.getLogger(LeavesServiceImpl.class);

	public LeavesServiceImpl(GenericRepository<Leaves> repository) {
		super(repository, Leaves.class);
	}

	@Autowired
	private LeavesRepository leavesRepository;

	@Autowired
	private LeaveTypeRepository leavesTypeResository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeLeaveTypeRepository employeeLeaveTypeRepository;

	@Autowired
	IWorkorderController workorderController;

	@Autowired
	WorkflowActionsController workflowActionsController;

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param leaves The leaves object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Leaves create(Leaves leaves) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		leaves.setWorkspaceId(workspaceId); // done done
		return leavesRepository.save(leaves);
	}

	@Override
	public List<UnpaidLeaveWrapper> getUnpaidLeaveCount(Date fromDate, Date toDate, Integer employeeId) {
		log.debug("Inside LeavesServiceImpl method getUnpaidLeaveCount  fromDate {}  toDate :{}  employeeId :{} ",
				fromDate, toDate, employeeId);

		String leaveType = "Unpaid leave";

		LeaveType leaveTypeObj = leavesTypeResository.findByNameAndCustomerId(leaveType, commonUtils.getCustomerId());

		if (employeeId != null) {

			return getLeaveCountWithEmployeeId(fromDate, toDate, employeeId, leaveTypeObj.getId());

		} else {

			return getUnpaidLeaveCountForAllEmployee(fromDate, toDate, leaveTypeObj.getId());

		}
	}

	private List<UnpaidLeaveWrapper> getUnpaidLeaveCountForAllEmployee(Date fromDate, Date toDate, Integer leveTypeId) {

		List<UnpaidLeaveWrapper> unpaidLeaveWrapperList = new ArrayList<>();
		try {
			log.debug(" Inside @getUnpaidLeaveCountForAllEmployee  customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> listOfEmployeeId = leavesRepository.getAllEmployeeLeaveCount(fromDate, toDate, leveTypeId,
					commonUtils.getCustomerId());

			for (Object[] empId : listOfEmployeeId) {
				int id = (int) empId[0];

				List<UnpaidLeaveWrapper> list = getLeaveCountWithEmployeeId(fromDate, toDate, id, leveTypeId);
				unpaidLeaveWrapperList.addAll(list);
			}

		} catch (Exception e) {
			log.error("Exception occured :{}", e.getMessage());
			log.error(Utils.getStackTrace(e));

		}
		return unpaidLeaveWrapperList;
	}

	private List<UnpaidLeaveWrapper> getLeaveCountWithEmployeeId(Date fromDate, Date toDate, Integer employeeId,
			Integer leveTypeId) {
		log.debug(
				"Inside LeavesServiceImpl method getLeaveCountWithEmployeeId  fromDate {}  toDate :{} employeeId :{} leveTypeId :{} ",
				fromDate, toDate, employeeId, leveTypeId);
		UnpaidLeaveWrapper unpaidLeaveWrapper = new UnpaidLeaveWrapper();
		List<UnpaidLeaveWrapper> unpaidLeaveWrapperList = new ArrayList<>();

		Date employeeDateOfExit = employeeRepository.fetchDateOfExit(employeeId);
		if (null != employeeDateOfExit && employeeDateOfExit.before(toDate) && employeeDateOfExit.after(fromDate)) {
			log.debug("Employee Date of Exit is in current month :{} ", employeeDateOfExit);
			toDate = employeeDateOfExit;
		}
		Long total = 0l;
		Long totalDay = 0l;
		try {
			totalDay = leavesRepository.getTotaldayCount(fromDate, toDate);
			log.debug("Inside LeavesServiceImpl method getLeaveCountWithEmployeeId  totalDay {}  leveTypeId :{} ",
					totalDay, leveTypeId);

			totalDay = totalDay + 1;

			// total = leavesRepository.getUnpaidLeaveCount(fromDate, toDate, employeeId,
			// leveTypeId);
			// get list of leave which intersect with payroll dates
			log.debug("The EmployeeId :{} and startDate :{} endDate :{} ", employeeId, fromDate, toDate);
			EmployeeDailyAttendanceService employeeDailyAttendence = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeDailyAttendanceService.class);
			Integer count = employeeDailyAttendence.getListOfLeavesBetweenDates(employeeId, fromDate, toDate,
					leveTypeId);
			log.debug("The count of unpaid leave is :{} ", count);
			total = (long) count;
			log.debug("Inside LeavesServiceImpl method getLeaveCountWithEmployeeId  total {}  ", total);

			log.debug(" Inside @getLeaveCountWithEmployeeId  customerId is : {}", commonUtils.getCustomerId());
			Date endDate = leavesRepository.getMaxEndDate(fromDate, toDate, employeeId, leveTypeId,
					commonUtils.getCustomerId());
			log.debug(" Max End Date in Leave Service Impl :{} ", endDate);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (endDate != null && fromDate != null) {
				LocalDate localFromDate = LocalDate.parse(sdf.format(fromDate));
				LocalDate localEndDate = LocalDate.parse(sdf.format(endDate));
				if (localEndDate.getMonth() == localFromDate.getMonth() && total != null) {
					Long dd = ChronoUnit.DAYS.between(localEndDate, localFromDate);
					log.info("Difrance between localEndDate ,fromDate {}", dd);
					if (dd > 0)
						total = total + dd;
				}
			}
			if (total == null) {
				log.debug(
						"Inside LeavesServiceImpl method getLeaveCountWithEmployeeId  no leaves totalDay {}  employeeid :{} ",
						totalDay, employeeId);
				log.debug("Inside LeavesServiceImpl method getLeaveCountWithEmployeeId employeeId :{} totalDay {}  ",
						employeeId, totalDay);
				unpaidLeaveWrapper.setCountOfWorkingDays(totalDay);
			} else {
				log.debug(
						"Inside LeavesServiceImpl method getLeaveCountWithEmployeeId leaves found totalDay {}  employeeid :{} totalDay:{} total:{} ",
						(totalDay - total), employeeId, totalDay, total);
				log.debug("Inside LeavesServiceImpl method getLeaveCountWithEmployeeId employeeId :{} total {}  ",
						employeeId, totalDay - total);
				unpaidLeaveWrapper.setCountOfWorkingDays(totalDay - total);
			}
			unpaidLeaveWrapper.setEmployeeId(employeeId);
			unpaidLeaveWrapperList.add(unpaidLeaveWrapper);

		} catch (Exception e) {

			log.error("Exception occured  inside getLeaveCountWithEmployeeId :{}", e.getMessage());
			log.error(Utils.getStackTrace(e));

		}
		return unpaidLeaveWrapperList;
	}

	public boolean isDateBetween(LocalDate startDate, LocalDate endDate, LocalDate dateToCheck) {
		return (dateToCheck.isEqual(startDate) || dateToCheck.isAfter(startDate))
				&& (dateToCheck.isEqual(endDate) || dateToCheck.isBefore(endDate));
	}

	@Override
	public String updateLeaveBalanceProcessInstanceId(@Valid String processInstanceId) {
		log.info("Inside method updateLeaveBalanceProcessInstanceId processInstanceId {}", processInstanceId);
		try {
			Leaves leave = leavesRepository.findByProcessInstanceId(processInstanceId);
			
			 if (leave != null && null != leave.getLeaveType() && null != leave.getEmployee()) {
				Integer leaveTypeId = leave.getLeaveType().getId();
				Integer employeeId = leave.getEmployee().getId();
				log.info("Inside method updateLeaveBalanceProcessInstanceId leaveTypeId ,employeeId {},{}", leaveTypeId,
						employeeId);
				EmployeeLeaveType employeeLeaveType = employeeLeaveTypeRepository
						.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId);
				if(leave.getLeaveType().getName().equalsIgnoreCase(PRConstant.REMOTE_WORKING))
				{
					String response = updateLeavesForRemoteWorking(processInstanceId,leave);
					log.debug("Reponse for updateLeaveForRemoteWorking response:{} ",response);
				}
				if (null != employeeLeaveType && null != employeeLeaveType.getTotalBalance()) {
					log.info("Inside method updateLeaveBalanceProcessInstanceId employeeLeaveType TotalBalance {}",
							employeeLeaveType.getTotalBalance());
					if (employeeLeaveType.getLeaveType().getName().equalsIgnoreCase(PRConstant.SICK_LEAVE)
							&& employeeLeaveType.getBalance() < leave.getLeaveTaken()) {
						employeeLeaveType.setBalance(0.0);
					} else if(employeeLeaveType.getBalance()<leave.getLeaveBalance()) {
						 
						log.error("Error Inside updateLeaveBalanceProcessInstanceeId Employee Leave Type Balance is less Than LeaveBalance ");
                          return CANNOT_DEDUCTED_LEAVE;						 
 
						}
						
						else
						{
							double restBalance = employeeLeaveType.getBalance() - leave.getLeaveTaken();
						  employeeLeaveType.setBalance(restBalance);
						}
					}
					employeeLeaveTypeRepository.saveAndFlush(employeeLeaveType);
					EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider
							.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
					Boolean flag = employeeDailyAttendanceService.addLeaveforEmployee(processInstanceId);
					log.debug("Inside LeavesServiceImpl @method updateLeaveBalanceProcessInstanceId value of flag :{} ",
							flag);
					leave.setIsDeducted(flag);
					leavesRepository.save(leave);
					log.debug(
							"Inside @class LeavesServiceImpl @method updateLeaveBalanceProcessInstanceId Leave Is Dudected :{} ",
							leave.getIsDeducted());
					return APIConstants.SUCCESS_JSON;
				}
			
		}
		
		catch(Exception e)
	{
			log.error(Utils.getStackTrace(e));
		}

	return APIConstants.FAILURE_JSON;
	}

//	@Override
//	public Leaves updateLeavesWorkflowStage(LeavesDto leavesDto) {
//		log.debug("Inside method UpdateLeavesWorkflowStage leaveId : {}", leavesDto.getLeaveId());
//		try {
//			if (leavesDto.getLeaveId() != null) {
//				java.util.Leaves optionalLeave = leavesRepository.findById(leavesDto.getLeaveId());
//				if (optionalLeave.isPresent()) {
//					Leaves leaves = optionalLeave.get();
//					leaves.setWorkflowStage(leavesDto.getWorkflowStage());
//					return leavesRepository.save(leaves);
//				} else {
//					throw new BusinessException("Leaves with ID " + leavesDto.getLeaveId() + " not found");
//				}
//			}
//		} catch (Exception e) {
//			throw new BusinessException("error while updating leaves work flow stage", e.getMessage());
//		}
//		return null;
//	}

	@Override
	public Leaves updateLeavesWorkflowStage(LeavesDto leavesDto) {
		log.debug("Inside method UpdateLeavesWorkflowStage leaveId : {}", leavesDto.getLeaveId());
		try {

			if (leavesDto.getLeaveId() != null) {
				Leaves optionalLeave = super.findById(leavesDto.getLeaveId());
				if (optionalLeave != null) {
					Leaves leaves = optionalLeave;
					LocalDate localDate = LocalDate.now();
					LocalDate leaveFromDate = convertDateToLocalDate(leaves.getFromDate());

					if (!localDate.isBefore(leaveFromDate)) {
						log.debug("LocalDate: {}, leave from Date: {}", localDate, leaveFromDate);
						throw new BusinessException("Leave is expired");
					}
					if (leavesDto.getWorkflowStage().equalsIgnoreCase("CANCELLED") && leaves != null
							&& leaves.getProcessInstanceId() != null) {
						log.debug("Inside method UpdateLeavesWorkflowStage ProcessInstanceId found is  : {}",
								leaves.getProcessInstanceId());
						leaves.setWorkflowStage(leavesDto.getWorkflowStage());
						String response = cancelHrApproveLeaves(leaves);
						log.debug("Reponse of Canceling leave in :{} ", response);
						NotificationIntegration notificationIntegration = ApplicationContextProvider
								.getApplicationContext().getBean(NotificationIntegration.class);
						Map<String, String> hrmsSystemConfig = hrmsSystemConfigService.getHrmsKeyValue();
						log.debug(" Value of PR Constant CancelNotificationTemplate :{} ",PRConstant.CANCEL_NOTIFICATION_TEMPLATE);
						String cancelNotificationTemplateName = hrmsSystemConfigService
								.getValue(PRConstant.CANCEL_NOTIFICATION_TEMPLATE);
						log.debug("Cancel Notification Template Name  :{} ", cancelNotificationTemplateName);
						NotificationTemplate template = notificationIntegration
								.getTemplte(cancelNotificationTemplateName);
						JSONObject jsonObject = getJsonObjectForLeave(leaves);
						String managerName = leaves.getEmployee().getReportingManager().getWorkEmailAddress();
						log.debug("Name of Manager of Employee Email :{}  is :{} ",leaves.getEmployee().getWorkEmailAddress(),managerName);
						notificationIntegration.sendNotification(template, jsonObject, managerName);
					}
					return leavesRepository.save(leaves);
				}

			} else {
				throw new BusinessException("Leaves with ID " + leavesDto.getLeaveId() + " not found");
			}

		} catch (BusinessException be) {
			throw new BusinessException("Leave Expired");
		} catch (Exception e) {
			log.error(" Error Inside @class LeaveServiceImpl @method updateLeavesWorkflowStage :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("error while updating leaves work flow stage");
		}
		return null;
	}

	private String cancelHrApproveLeaves(Leaves leaves) {
		try {
			log.debug("Inside @method cancelHrApproveLeaves leaves:{}", leaves.getId());
			LeaveType leaveType = leaves.getLeaveType();
			log.debug("Leave Type is :{} and Id :{} ", leaveType.getName(), leaveType.getId());

			if (leaves.getIsDeducted() != null && leaves.getIsDeducted()) {
				log.info("Leave is approved ");
				String response = cancelLeaves(leaves.getProcessInstanceId());
				return response;
			}
			return APIConstants.FAILURE_JSON;
		} catch (Exception e) {
			log.error(" Error Inside @class LeaveServiceImpl @method cancelHrApproveLeaves :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public Integer getCountOfLeave(LocalDate beforeDate, LocalDate afterDate, Integer employeeId, Integer code,
			String flow) {
		log.info("Inside @class LeaveService @method getCountOfLeave ");
		try {
			log.debug("Inside @class LeaveService @method getCountOfLeave  date1 :{}  date2 :{}", beforeDate,
					afterDate);
			log.debug(" Inside @getCountOfLeave  customerId is : {}", commonUtils.getCustomerId());
			Integer count = leavesRepository.getLeaveSumByDate(beforeDate, afterDate, employeeId, code, flow,commonUtils.getCustomerId());
			log.debug("the leave count is :{} ", count);
			return count;

		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method getCountOfLeave :{}  :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	@Override
	public Integer findLeaveTakenByEmployeeIdAndWorkflowStage(Integer employeeId, Integer leaveTypeId,
			String workflowStage) {
		log.debug(
				"Inside @class LeaveService @method findLeaveTakenByEmployeeIdAndWorkflowStage employeeId :{} leaveTypeId :{} WorkflowStage :{}",
				employeeId, leaveTypeId, workflowStage);
		try {
			log.debug(" Inside @findLeaveTakenByEmployeeIdAndWorkflowStage  customerId is : {}", commonUtils.getCustomerId());
			Integer leaveTaken = leavesRepository.getLeaveTakenByEmployeeIdAndLeaveIdAndWorkflowStage(employeeId,
					leaveTypeId, workflowStage,commonUtils.getCustomerId());
			log.debug("Value of Leave Taken is :{} ", leaveTaken);
			if (leaveTaken != null) {
				return leaveTaken;
			} else {
				return 0;
			}
		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method findLeaveTakenByEmployeeIdAndWorkflowStage :{}  :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();

		}
	}

	@Override
	public Integer getLeaveByPayrollDateAndEmployeeId(Date fromDate, Date toDate, Integer employeeId,
			Integer leveTypeId) {
		try {
			log.debug(
					"Inside @class LeaveService @method getLeaveByPayrollDateAndEmployeeId fromDate :{} toDate :{} employeeId :{} leaveTypeId :{} ",
					fromDate, toDate, employeeId, leveTypeId);
			log.debug(" Inside @getLeaveByPayrollDateAndEmployeeId  customerId is : {}", commonUtils.getCustomerId());
			List<Leaves> leaveList = leavesRepository.getLeavesByPayrollRunDates(PRConstant.HRAPPROVED, leveTypeId,
					employeeId, fromDate, toDate,commonUtils.getCustomerId());
			log.debug("List of Leaves :{} ", leaveList);

			EmployeeDailyAttendanceService employeeDailyAttendence = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeDailyAttendanceService.class);
			Integer count = employeeDailyAttendence.getListOfLeavesBetweenDates(employeeId, fromDate, toDate,
					leveTypeId);

			return count;
		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method getLeaveByPayrollDateAndEmployeeId :{}  :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public Leaves getLeavesByProcessInstanceId(String processIntanceId) {
		try {
			log.debug("Inside @class LeaveService @method getLeavesByProcessInstanceId processInstanceId :{} ",
					processIntanceId);
			log.debug(" Inside @getLeavesByProcessInstanceId  customerId is : {}", commonUtils.getCustomerId());
			Leaves leave = leavesRepository.getLeaveByProcessInstanceId(processIntanceId,commonUtils.getCustomerId());
			return leave;

		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method getLeavesByProcessInstanceId :{}  :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	@Override
	public String cancelLeaves(String processInstanceId) {
		try {
			log.debug("Inside @class LeavesServiceImpl @method cancelLeaves processInstanceId :{} ", processInstanceId);
			Leaves leave = getLeavesByProcessInstanceId(processInstanceId);
			Double count = leave.getLeaveTaken();
			log.debug("Count of Leave taken :{} ", count);
			Employee employee = leave.getEmployee();
			LeaveType leaveType = leave.getLeaveType();
			log.debug("Id of LeaveType :{} ", leaveType.getId());
			EmployeeLeaveTypeService employeeLeaveTypeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeLeaveTypeService.class);
			EmployeeLeaveType employeeLeaveType = employeeLeaveTypeService
					.getEmployeeLeaveTypeByEmployeeIdAndLeaveType(employee.getId(), leaveType.getId());
			log.debug("Employee Leave Type Id :{} for Employee :{} ", employeeLeaveType.getId(), employee.getId());
			Double balance = employeeLeaveType.getBalance();
			balance = balance + count;
			log.debug("Balance After adding leave :{} ", balance);
			employeeLeaveType.setBalance(balance);
			employeeLeaveTypeService.update(employeeLeaveType);
			leave.setWorkflowStage(PRConstant.CANCELLED);
			leavesRepository.save(leave);
			EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
			employeeDailyAttendanceService.changeEmployeeDailyAttendanceStatusByLeaveId(leave);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method cancelLeaves :{}  :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			return APIConstants.FAILURE_JSON;
		}
	}

	private JSONObject getJsonObjectForLeave(Leaves leave) {
		try {
			log.info("Inside @class LeavesServiceImpl @method getJsonObjectForLeave ");
			JSONObject jsonObject = new JSONObject();
			if (leave.getEmployee() != null && leave.getEmployee().getFullName() != null) {
				jsonObject.put("fullName", leave.getEmployee().getFullName());
			} else {
				throw new BusinessException("Employee Full Name Not Present");
			}
			return jsonObject;
		} catch (Exception e) {
			log.error("Error inside @class LeaveService @method getJsonObjectForLeave :{}  :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	 public String deleteLeaveByLeaveId(Integer leaveId)
	 {
	   	try
	   	{
	   		log.debug("Inside @class LeaveServiceImpl @method deleteLeaveAndEmployeeDailyAttendance leave Id :{}",leaveId);
	   		Leaves leave = super.findById(leaveId);
	   		if(leave!=null) {
	   	    EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
	        Integer count = employeeDailyAttendanceService.deleteEmployeeDailyAttendanceByLeaveId(leaveId);
	        log.debug(" Count of Deleted Employee Daily Attendance :{} ",count);
	          try
	         {
	        	leavesRepository.deleteById(leaveId);
	        log.info("Leave Deleted");
	          }
	         catch(Exception e)
	           {
	        	log.error("Cannot Delete the Leave ");
	           }
	        return APIConstants.SUCCESS_JSON;
	   		}
	   		  else
	   		  {
	   			log.error("Leave does not exist for Leave Id :{} ",leaveId);
	   			throw new BusinessException("Cannot Delete the Leave");
	   		}
	   	}
	   	catch(Exception e) {
			log.error("Error inside @class LeaveServiceImpl @method deleteLeaveAndEmployeeDailyAttendance :{}  :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
	   	}
	 }

	private String updateLeavesForRemoteWorking(String processInstanceId,Leaves leave)
	{
		try
		{
			EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
			Boolean flag = employeeDailyAttendanceService.addLeaveforEmployee(processInstanceId);
			log.debug("Inside LeavesServiceImpl @method updateLeaveBalanceProcessInstanceId value of flag :{} ",
					flag);
			leave.setIsDeducted(flag);
			leavesRepository.save(leave);
			log.debug(
					"Inside @class LeavesServiceImpl @method updateLeaveBalanceProcessInstanceId Leave Is Dudected :{} ",
					leave.getIsDeducted());
			return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e)
		{
		  log.error("error inside @class LeaveServiceImpl @method updateLeaveForRemote :{} :{}",e.getMessage(),Utils.getStackTrace(e));
		  throw new BusinessException();
		}
	}
}
