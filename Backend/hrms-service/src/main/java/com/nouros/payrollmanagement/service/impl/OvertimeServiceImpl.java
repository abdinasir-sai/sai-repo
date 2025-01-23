package com.nouros.payrollmanagement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.repository.OvertimeLogsRepository;
import com.nouros.payrollmanagement.repository.OvertimeRepository;
import com.nouros.payrollmanagement.service.OvertimeService;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

@Service
public class OvertimeServiceImpl extends AbstractService<Integer,Overtime> implements OvertimeService {

	private static final Logger log = LogManager.getLogger(OvertimeServiceImpl.class);

	public OvertimeServiceImpl(GenericRepository<Overtime> repository) {
		super(repository, Overtime.class);
	}

	@Autowired
	private OvertimeRepository overtimeRepository;

	@Autowired
	private OvertimeLogsRepository overtimeLogsRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	UserRest userRest;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	@Override
	public Overtime create(Overtime overtime) {
		return overtimeRepository.save(overtime);
	}

	@Override
	public void softDelete(int id) {
		log.info("Overtime Id is : {}", id);
		Overtime overtime = super.findById(id);
		if (overtime != null) {

			Overtime overtime1 = overtime;
			overtime1.setDeleted(true);
			overtimeRepository.save(overtime1);
		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {

				softDelete(list.get(i));

			}
		}

	}

	@Override
	public Overtime findById(Integer id) {
		log.info("Finding overtime Id is: {}", id);
		Overtime overtime = super.findById(id);
		if (overtime != null) {
			log.info("Overtime with id:{} found", id);
		} else {
			log.warn("Overtime with id:{} not found", id);
		}
		return overtime;

	}

	public List<Overtime> getListOfOvertimeByEmployeeId(Integer employeeId) {
		try {
			log.info("Inside @class OvertimeServiceImpl @method getListOfOvertimeByEmployeeId ");
			log.debug("Inside @class OvertimeServiceImpl @method getListOfOvertimeByEmployeeId the Employee_Id :{}",
					employeeId);
			List<Overtime> overtimeList = overtimeRepository.findByEmployeeId(employeeId, PRConstant.APPROVED_BY_HR_HEAD);
			log.debug(
					"Inside @class OvertimeServiceImpl @method getListOfOvertimeByEmployeeId the size of Overttime List :{}",
					overtimeList.size());
			return overtimeList;
		} catch (Exception e) {
			log.error("Error inside @class OvertimeServiceImpl @method getListOfOvertimeByEmployeeId :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public OvertimeDto getOvertimeDetails(Integer empId, Integer overtimeId, Integer month, Integer year) {
		log.info("Inside getOvertimeDetails ");
		OvertimeDto overtimeDto = new OvertimeDto();
		try {
			log.debug("Inside method getOvertimeDetails empId: {} overtimeId: {} month :{} year:{} ", empId,overtimeId,month,year);
			Overtime result = new Overtime();
			Employee employee = getEmployeeObject(empId);
			log.debug("Inside getOvertimeDetails employeee :{}", employee);
			if (overtimeId != null) {
				result = overtimeRepository.getById(overtimeId);
			} else if(month !=null && year!=null){
				if (employee != null) {
					log.debug("Employee Details first name: {}, and lastName: {}", employee.getFirstName(),
							employee.getLastName());
					result = overtimeRepository.getOvertimeByEmployeeIdAndMonthNumberAndYear(employee.getId(), month,
							year);
				}
			}else {
				throw new BusinessException("Provide Correct Details of OvertimeId, Month or Year");
			}
			List<OvertimeLogs> overtimeLogs;
			log.debug("Inside getOvertimeDetails result: {} ",result);
			overtimeLogs = overtimeLogsRepository.findByOvertimeId(result.getId());
			log.debug("Inside method getOvertimeDetails overtimeLogs :{}", overtimeLogs);
			overtimeDto.setOvertimeLogs(overtimeLogs);
			return overtimeDto;

		} catch (

		BusinessException e) {
			log.error("Business exception encountered: {}", e.getMessage());
		} catch (Exception e) {
			log.error("An unexpected error occurred: {}", e.getMessage());
		}

		return overtimeDto;
	}

	private Employee getEmployeeObject(Integer empId) {
		log.info("Inside method getEmployeeObject empid:{}", empId);
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

	@Override
	@TriggerBPMN(entityName = "Overtime", appName = "HRMS_APP_NAME")
	public Overtime createOvertime(Employee employee, String overtimeStatus, Integer month, Integer year, List<OvertimeLogs> overtimeLogsList) {
		log.info("Inside createOvertime method status:{}", overtimeStatus);
		Overtime overtimeForCreation = new Overtime();
		log.debug("Inside createOvertime employee:{}", employee);
		overtimeForCreation.setEmployee(employee);
		if (overtimeStatus != null && overtimeStatus.equalsIgnoreCase("SAVE")) {
			overtimeForCreation.setWorkflowStage("draft");
		} else {
			overtimeForCreation.setApprovalStatus("Pending");
		}
		Integer overtimeSubmittedMinutes =0;
		for(OvertimeLogs overtimeLogs : overtimeLogsList)
		{
			if(overtimeLogs.getOvertimeMinutes() != null) {
				log.info("Inside method updateOvertimeDetails iterating overtimeLogs ");
			Integer overtimeLogsMinutes= overtimeLogs.getOvertimeMinutes();
			overtimeSubmittedMinutes+=overtimeLogsMinutes;
			}
			else {
				throw new BusinessException("Time limit is not appropriate");
			}
		}
		log.info("Inside createOvertime method overtimeSubmittedMinutes: {} ",overtimeSubmittedMinutes);
		overtimeForCreation.setDeleted(false);
		overtimeForCreation.setOvertimeMonth(month);
		overtimeForCreation.setOvertimeYear(year);
		overtimeForCreation.setSubmittedHours(convertToHours(overtimeSubmittedMinutes));
		Date currentDate = new Date();
		log.info("Inside createOvertime currentDate :{}", currentDate);
		overtimeForCreation.setSubmissionDate(currentDate);
		log.debug("Inside createOvertime overtimeForCreation:{}", overtimeForCreation);
		return create(overtimeForCreation);
	}

	@Override
	@TriggerBPMN(entityName = "Overtime", appName = "HRMS_APP_NAME")
	public Overtime updateOvertimeDetails(Overtime overtime, String overtimeStatus, Integer month, Integer year, List<OvertimeLogs> overtimeLogsList) {
		log.info("Inside method updateOvertimeDetails status: {} ", overtimeStatus);
		log.debug("ProcessInstanceId found for overtime is : {} ", overtime.getProcessInstanceId());
		if (overtime.getProcessInstanceId() != null) {
			if (overtimeStatus != null && overtimeStatus.equalsIgnoreCase("SUBMIT")) {
				overtime.setApprovalStatus("Closed");
				
			}else if (overtimeStatus != null && overtimeStatus.equalsIgnoreCase("RESUBMIT")) {
				overtime.setApprovalStatus("On Hold");
			}
		}else if(overtime.getProcessInstanceId() == null && overtimeStatus.equalsIgnoreCase("SAVE")) {
			log.info("Inside method updateOvertimeDetails save in update");
			overtime.setWorkflowStage("draft");
		} else {
			overtime.setApprovalStatus("Pending");
		}
		Integer overtimeSubmittedMinutes =0;
		for(OvertimeLogs overtimeLogs : overtimeLogsList)
		{
			if(overtimeLogs.getOvertimeMinutes() != null) {
				log.info("Inside method updateOvertimeDetails iterating overtimeLogs ");
			Integer overtimeLogsMinutes= overtimeLogs.getOvertimeMinutes();
			overtimeSubmittedMinutes+=overtimeLogsMinutes;
			}
			else {
				throw new BusinessException("Time limit is not appropriate");
			}
		}
		log.info("Inside createOvertime method overtimeSubmittedMinutes: {} ",overtimeSubmittedMinutes);
		overtime.setOvertimeMonth(month);
		overtime.setOvertimeYear(year);
		overtime.setSubmittedHours(convertToHours(overtimeSubmittedMinutes));
		log.debug("Inside method updateOvertimeDetails updatedovertime:{}", overtime);
		
		if (null == overtime.getProcessInstanceId()) {
			return updateWithBpmn(overtime);
		}
		log.info("Completed method updateOvertimeDetails");

		return overtimeRepository.save(overtime);
	}

	@Override
	@TriggerBPMN(entityName = "Overtime", appName = "HRMS_APP_NAME")
	public Overtime updateWithBpmn(Overtime overtime) {
		log.info("Inside OvertimeServiceImpl method updateWithBpmn");
		return overtimeRepository.save(overtime);
	}
	
	private double convertToHours(int minutes) {
		double hours1 =0.0;
		int hours = minutes / 60;
		int remainingMinutes = minutes % 60;
		if(minutes >=60){
		     hours1 = hours+(remainingMinutes/100.0);
		}
		else
		  hours1 = (minutes/100.0);
		return Double.parseDouble(String.format("%.2f", hours1));
	}

	@Override
	public String deleteOvertimeLogsAndOvertimeId(Integer id) {
		log.debug("Inside method deleteOvertimeLogsAndOvertimeId id : {}", id);
		try
		{
			overtimeLogsRepository.deleteAllOvertimeLogsAssociatedWithOvertime(id);
			log.debug("All OvertimeLogs deleted associated with id: {} ",id);
			overtimeRepository.deleteById(id);
			log.debug("Overtime Deleted with Id : {} ", id);
			return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e)
		{
			log.debug("SomeThing Went Wrong While Deleting Overtime And overTimeLogs ");
			return APIConstants.FAILURE_JSON;
		}
	}

}
