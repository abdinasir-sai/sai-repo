
package com.nouros.payrollmanagement.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TimeSheetService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.repository.OvertimeLogsRepository;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.OvertimeLogsService;
import com.nouros.payrollmanagement.service.OvertimeService;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

@Service
public class OvertimeLogsServiceImpl extends AbstractService<Integer,OvertimeLogs> implements OvertimeLogsService {
	private static final Logger log = LogManager.getLogger(OvertimeLogsServiceImpl.class);

	protected OvertimeLogsServiceImpl(GenericRepository<OvertimeLogs> repository) {
		super(repository, OvertimeLogs.class);

	}

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	UserRest userRest;

	@Autowired
	private OvertimeLogsRepository overtimeLogsRepository;
	
	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	CustomerInfo customerInfo;
	
	private static final String MAX_OVERTIME_LOG_MINUTES="maxOvertimeLogMinutes";
	private static final String MIN_OVERTIME_LOG_MINUTES="minOvertimeLogMinutes";

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	@Override
	public OvertimeLogs create(OvertimeLogs overtimeLogs) {
		return overtimeLogsRepository.save(overtimeLogs);
	}

	@Override
	public void softDelete(int id) {
		log.info("OvertimeLogs Id is : {}", id);
		OvertimeLogs overtimeLogs = super.findById(id);

		if (overtimeLogs != null) {
			OvertimeLogs overtimeLogs1 = overtimeLogs;
			overtimeLogs1.setDeleted(true);
			overtimeLogsRepository.save(overtimeLogs1);
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
	public OvertimeLogs findById(Integer id) {
		log.info("Finding OvertimeLogs Id is : {}", id);
		OvertimeLogs overtimeLogs = super.findById(id);
		if (overtimeLogs != null) {
			log.info("OvertimeLogs found:{}", overtimeLogs);
		} else {
			log.warn("OvertimeLogs with id:{} not found", id);
		}
		return overtimeLogs;

	}

	public List<OvertimeLogs> getOvertimeLogsbyOvertimeIdAndPayrollDate(Integer overtimeId, Date payrollRunstartDate,
			Date payrollRunEndDate) {
		try {
			log.info("Inside @class  OvertimeLogsServiceImpl @method getOvertimeLogsbyOvertimeIdAndPayrollDate");
			log.debug(
					"Inside @class  OvertimeLogsServiceImpl @method getOvertimeLogsbyOvertimeIdAndPayrollDate  overtimeId :{} Payroll_StartDate :{} Payroll_EndDate :{}",
					overtimeId, payrollRunstartDate, payrollRunEndDate);
			List<OvertimeLogs> overtimeLogsList = overtimeLogsRepository.findByOvertimeIdAndPayrollDate(overtimeId,
					payrollRunstartDate, payrollRunEndDate,PRConstant.APPROVED_BY_HR_HEAD);
			log.debug(
					"Inside @class  OvertimeLogsServiceImpl @method getOvertimeLogsbyOvertimeIdAndPayrollDate size of List :{}",
					overtimeLogsList.size());
			return overtimeLogsList;

		} catch (Exception e) {
			log.error(
					"Error inside @class OvertimeLogsServiceImpl @method getOvertimeLogsbyOvertimeIdAndPayrollDate :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	@Override
	public OvertimeDto createOrUpdateOvertimeLogs(OvertimeDto overtimeDto, Integer month, Integer year) {
		log.debug("Inside method createOrUpdateOvertimeLogs overtimeDto :{} month:{} year:{}", overtimeDto, month, year);
		Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
		String mappedMaxOvertimeLogMinutes = hrmsSystemConfigMap.get(MAX_OVERTIME_LOG_MINUTES);
		String mappedMinOvertimeLogMinutes = hrmsSystemConfigMap.get(MIN_OVERTIME_LOG_MINUTES);
		log.debug("values from system config mappedMaxOvertimeLogMinutes: {}, mappedMinOvertimeLogMinutes: {}",
				mappedMaxOvertimeLogMinutes, mappedMinOvertimeLogMinutes );
		if(mappedMaxOvertimeLogMinutes == null || mappedMinOvertimeLogMinutes ==null)
		{
			log.error("Value from HRMS SYSTEM CONFIG fOR OVERTIME_LOG_MINUTES  is NULL ");
		}
		Integer maxOvertimeLogMinutes = Integer.parseInt(mappedMaxOvertimeLogMinutes);
		Integer minOvertimeLogMinutes = Integer.parseInt(mappedMinOvertimeLogMinutes);
		log.debug("values from system config maxOvertimeLogMinutes: {}, minOvertimeLogMinutes: {}",
				maxOvertimeLogMinutes, minOvertimeLogMinutes );
		
		OvertimeService overtimeService = ApplicationContextProvider.getApplicationContext()
				.getBean(OvertimeService.class);
		OvertimeDto updatedOvertimeDto = new OvertimeDto();
		Employee employee = new Employee();
		try {
			List<OvertimeLogs> overtimeLogsList = overtimeDto.getOvertimeLogs();
			log.debug("Inside createOrUpdateOvertimeLogs list:{}", overtimeLogsList);
			Overtime overtime = new Overtime();
			if (overtimeLogsList != null && !overtimeLogsList.isEmpty()) {
				log.debug("Inside createOrUpdateOvertimeLogs overtimelogs:{}", overtimeLogsList.get(0));
				OvertimeLogs overtimeLogs1 = overtimeLogsList.get(0);
				Overtime overtimeOld = overtimeLogs1.getOvertime();
				log.debug("Inside createOrUpdateOvertimeLogs overtime found from overtimeLogs1 is : {}", overtimeOld);
				User contextUser = getUserContext();
				log.debug("contextUser user Id is : {}", contextUser.getUserid());
				try {
					employee = employeeRepository.findByUserId(contextUser.getUserid());
				} catch (Exception e) {
					log.error("Employee Not found For This User Context");
					throw new BusinessException("Employee Not found For This User Context");
				}
				if(employee ==null)
				{
					throw new BusinessException("Employee Not found For This User Context");
				}
				log.debug("Inside createOrUpdateOvertimeLogs employee : {} ", employee);
				log.debug("Inside createOrUpdateOvertimeLogs status : {} ", overtimeDto.getOvertimeStatus());
				if (overtimeOld != null && overtimeDto.getOvertimeStatus() != null) {
					overtime = overtimeService.updateOvertimeDetails(overtimeOld, overtimeDto.getOvertimeStatus(),
							month, year,overtimeLogsList );
				} else {
					overtime = overtimeService.createOvertime(employee, overtimeDto.getOvertimeStatus(), month, year, overtimeLogsList);
				}
				
				deleteOvertimeLogsForOvertime(overtime.getId());
				log.info("deleted overtimeLogs exist");
				log.debug("Inside createOrUpdateOvertimeLogs overtime created/found:{}", overtime);
				
				List<OvertimeLogs> updatedOvertimeLogsList = new ArrayList<>();
				for (OvertimeLogs overtimeLogs : overtimeLogsList) {
					log.debug("Minutes : {} ", overtimeLogs.getOvertimeMinutes());
					if (overtimeLogs.getOvertimeMinutes() != null  && overtimeLogs.getOvertimeMinutes()>=minOvertimeLogMinutes && overtimeLogs.getOvertimeMinutes()<=maxOvertimeLogMinutes) {
						Double submittedHours = convertToHours(overtimeLogs.getOvertimeMinutes());
						log.debug("submittedHours is : {}", submittedHours);
						overtimeLogs.setOvertimeHours(submittedHours);
						overtimeLogs.setOvertime(overtime);
						overtimeLogs.setWorkflowStage(PRConstant.PENDING_SM);
					}
					else
					{
						log.error("Inappropriate overtime limit.");
						throw new BusinessException("Inappropriate overtime limit");
					}
					OvertimeLogs updatedOvertimeLogs = overtimeLogsRepository.save(overtimeLogs);
					log.debug("updated overtimeLogs id:{}", updatedOvertimeLogs.getId());
					updatedOvertimeLogsList.add(updatedOvertimeLogs);
				}
				updatedOvertimeDto.setOvertimeLogs(updatedOvertimeLogsList);
				updatedOvertimeDto.setOvertimeStatus(overtimeDto.getOvertimeStatus());
				updatedOvertimeDto.setMonth(month);
				updatedOvertimeDto.setYear(year);
			}
			return updatedOvertimeDto;
		}catch (Exception e) {
			log.error("Exception occured : {} ", e);
			throw new BusinessException(e.getMessage());
		}
	}
	private void deleteOvertimeLogsForOvertime(Integer overtimeId) {
		log.info("Inside method deleteovertimeLogsForEmployee");
		overtimeLogsRepository.deleteAllOvertimeLogsAssociatedWithOvertime(overtimeId);
		log.info("Overtimelogs deleted For overtimeId: {} ",overtimeId );
		
		
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
	public List<OvertimeLogs> getOvertimeLogsForEmployeeByWorkflowStageAndDate(Integer employeeId ,String workflowStage,Date payrollRunStartDate)
	{
		try
		{
		  log.debug("Inside @method getOvertimeLogsForEmployeeByWorkflowStageAndDate employeeId :{} workflowStage :{} payrollRunStartDate  :{} ",
				  employeeId,workflowStage,payrollRunStartDate);
		  List<OvertimeLogs> overtimeLogsList = overtimeLogsRepository.getOvertimeLogsByWorkflowStageAndDate(workflowStage, payrollRunStartDate, employeeId);
		  log.debug("Size of OvertimeLogsList :{} ",overtimeLogsList.size());
		  return overtimeLogsList;
		}
		catch(Exception e)
		{
			log.error("Error inside @class OvertimeLogsServiceImpl @method getOvertimeLogsForEmployeeByWorkflowStageAndDate :{} :{}",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

}
