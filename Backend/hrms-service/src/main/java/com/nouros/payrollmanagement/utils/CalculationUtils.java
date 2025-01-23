package com.nouros.payrollmanagement.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.service.EmployeeDailyAttendanceService;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.HolidayService;
import com.nouros.hrms.service.LeaveTypeService;
import com.nouros.hrms.service.LeavesService;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure.EmployeeMobileType;
import com.nouros.payrollmanagement.model.OtherSalaryComponent.Type;
import com.nouros.payrollmanagement.repository.EmployeeMonthlySalaryRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.OtherSalaryComponentService;
import com.nouros.payrollmanagement.service.OtherSalaryPayrollMappingService;
import com.nouros.payrollmanagement.service.OvertimeLogsService;
import com.nouros.payrollmanagement.service.OvertimeService;
import com.nouros.payrollmanagement.wrapper.PayrollResponseWrapper;

@EnableAsync
@Component
public class CalculationUtils {

	private static final Logger log = LogManager.getLogger(CalculationUtils.class);

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	public EmployeeMonthlySalaryRepository repository;

	@Autowired
	public OtherSalaryComponentService otherSalaryComponentService;

	DecimalFormat df = new DecimalFormat("#.##");

	@Autowired
	public OtherSalaryPayrollMappingService otherSalaryPayrollMappingService;

	public Double calculateBasicSalay(Double monthSalary, PayrollRun payrollRun, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject,Employee employee,EmployeeMonthlySalary employeeMonthlySalary) {
		log.debug("going to calculateBasicSalay  monthSalary :{} payrollrun: {}", monthSalary, payrollRun);

		Double basicSalary = 0.0;
		Double amount = 0.0;
		
		try {
			if (payrollRun != null) {
				log.debug("going to calculateBasicSalay  monthSalary :{} payrollrun {}", monthSalary, payrollRun);
			Map<String,String>	hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue(); 

				Integer totalDays = payrollRun.getDurationDayCount();
				Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);
				
				basicSalary = (monthSalary / totalDays) * workingDays;
				
				//first count the leave for sick 
				//if it is >30 then find for that payroll 
				//and then calculate the amount and deduct from basic salary
        
				//first we find the leave type Id
				LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext().getBean(LeaveTypeService.class);
              LeaveType leaveType =   leaveTypeService.findByName(PRConstant.SICK_LEAVE);
				Integer totalCountOfPayroll = 0;
                  Integer totalCount = 0;
              EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);  
              Date endDate = payrollRun.getEndDate();
              Date startDate = payrollRun.getStartDate();
              LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
              LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
              if(endLocalDate.getYear() != startLocalDate.getYear())
              {
            	  int lastDayOfMonth = startLocalDate.lengthOfMonth();
                    LocalDate modifiedEndDate = startLocalDate.withDayOfMonth(lastDayOfMonth);
                   endDate = Date.from(modifiedEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                  log.info("StartDate month EndDate month are same");
                  log.debug("lastDayOfMonth :{} modifiedEndDate:{} endDate :{} ",lastDayOfMonth,modifiedEndDate,endDate);
              }
              Integer leaveTaken =  employeeDailyAttendanceService.getListOfLeavesByLeaveTypeId(employee.getId(), leaveType.getId(),endDate);
                Integer leaveTakenbeforePayroll =  employeeDailyAttendanceService.getListOfLeavesByLeaveTypeId(employee.getId(), leaveType.getId(),startDate);
                log.debug("The leaveTaken :{} employeeId :{} leaveTakenbeforePayroll :{} ",leaveTaken,employee.getId(),leaveTakenbeforePayroll);
               
                if(leaveTaken<=30 && leaveTakenbeforePayroll<30)
                {
                	log.debug("Leave Taken :{} leaveTakenbeforePayroll :{} both less than 30 ",leaveTaken,leaveTakenbeforePayroll);
                	Integer diff = leaveTaken-leaveTakenbeforePayroll;
                	Double count = 0.0;
                	log.debug("Difference between leaveTaken and LeaveTakenBefore Payroll  :{}",diff);
                	count = (double)diff;
  
                	employeeMonthlySalary.setSickPaidCount(count);
                	employeeMonthlySalary.setSickUnpaidCount(0.0);
                }
                // 25-35
                else if (leaveTaken>30 && leaveTakenbeforePayroll<=30)
 				{
 					log.debug(" The Leave before Payroll endDate :{} leave before Payroll StartDate :{} ",leaveTaken,leaveTakenbeforePayroll);
 				 
 				Integer count =  leaveTaken-30;
 					log.debug(" Count of Leave 30 and leaveDay :{} ",count);
 					amount = ((monthSalary/totalDays)/4)*count;
 					log.debug(" The Amount in 30>leaveTakenbeforePayroll and leaveTaken>30 :{}",amount);
 					Integer sickLeaveValue =  Integer.parseInt(hrmsSystemConfigMap.get(PRConstant.SICK_LEAVE_VALUE));
 					log.debug("Sick Leave Value :{} ",sickLeaveValue);
 					Double sickPaidLeave = (double)count*(sickLeaveValue/100);
 					Integer sickFullPaidCount = 30 - leaveTakenbeforePayroll;
 					log.debug("Sick Leave Value after 75/100 :{} sickFullPaidCount:{} ",sickPaidLeave,sickFullPaidCount);
 					Double totalSum = 0.0;
 					totalSum = sickPaidLeave+sickFullPaidCount;
 					employeeMonthlySalary.setSickPaidCount(totalSum);
 					Double sickUnpaidCount = 0.0;
 					sickUnpaidCount = (double)count/4;
 					 log.debug("Value of SickUnpaidCount :{}  ",sickUnpaidCount);
 					 
 					 employeeMonthlySalary.setSickUnpaidCount(sickUnpaidCount);
 				}
                //30-60
                else if(leaveTaken>30 && leaveTaken<=60 && leaveTakenbeforePayroll>30 )
				{
                	
				 totalCountOfPayroll = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(),payrollRun.getStartDate(), payrollRun.getEndDate(),leaveType.getId());

                	log.debug("The count of Sick leave in this payroll is :{} ",totalCountOfPayroll);
				
				Integer count = leaveTaken - leaveTakenbeforePayroll;
				log.debug("The count of Sick leave which exceds in this payroll :{} ",count);
				 
				amount = ((monthSalary/totalDays)/4)*count    ;
				
				log.debug("the basic salary of EmployeeID :{}  basicSalary :{}",employee.getId(),basicSalary);
				Integer sickLeaveValue =  Integer.parseInt(hrmsSystemConfigMap.get(PRConstant.SICK_LEAVE_VALUE));
					log.debug("Sick Leave Value :{} ",sickLeaveValue);
					Double sickPaidLeave = 0.0;
					sickPaidLeave = (double)count*(sickLeaveValue/100);
					log.debug("Sick Leave Value after 75/100 :{} ",sickPaidLeave);
					employeeMonthlySalary.setSickPaidCount(sickPaidLeave);
					Double sickUnpaidCount = 0.0;
					sickUnpaidCount = (double)count/4;
					log.debug("Sick Unpaid Count :{} ",sickUnpaidCount);
					employeeMonthlySalary.setSickUnpaidCount(sickUnpaidCount);
					 
				}
                //55-65
 				else if (leaveTaken>60 && leaveTakenbeforePayroll<=60)
 				{
 					log.debug(" The Leave before Payroll endDate :{} leave before Payroll StartDate :{} ",leaveTaken,leaveTakenbeforePayroll);
 					Integer count = leaveTaken-60;
 					log.debug(" Count of Leave leaveDay and 60 :{} ",count);
 					Double sickUnpaidCount = (double)count;
 					log.debug("SickUnpaidCount :{} ",sickUnpaidCount);
 					amount = ((monthSalary/totalDays))*count ;

 					log.debug(" the Amount after reducing the amount :{}" ,amount);
 
 					count = 60-leaveTakenbeforePayroll;
 					log.debug(" Count of Leave 60 and leaveDay :{} ",count);
 					amount = ((monthSalary/totalDays)/4)*count+amount;
 					Integer sickLeaveValue =  Integer.parseInt(hrmsSystemConfigMap.get(PRConstant.SICK_LEAVE_VALUE));
 					log.debug("Sick Leave Value :{} ",sickLeaveValue);
 					Double sickPaidLeave = 0.0;
 					 sickPaidLeave = (double)count*(sickLeaveValue/100);
 					log.debug(" The Amount in 60>leaveTakenbeforePayroll and leaveTaken>60 :{}",amount);
 					log.debug("Sick Leave Value after 75/100 :{} ",sickPaidLeave);
 					Double sickUnpaidCountHalf = (double)count/4;
 				    employeeMonthlySalary.setSickPaidCount( sickPaidLeave );
 				   Double totalCount1  = 0.0;
 				     totalCount1 = sickUnpaidCount+sickUnpaidCountHalf ;
                    employeeMonthlySalary.setSickUnpaidCount(totalCount1);
 				}
                //60>
                 else if(leaveTaken>60 && leaveTakenbeforePayroll>60)
  				{
 					totalCount = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(),payrollRun.getStartDate(), payrollRun.getEndDate(),leaveType.getId());
 					log.debug("The count of Sick leave in this payroll is :{} ",totalCount);
 					Integer count = leaveTaken - leaveTakenbeforePayroll;
 					log.debug("The count of Sick leave which exceds in this payroll >60 :{} ",count);
 					Double sickUnpaidCount = (double)count;
 					log.debug(" Value of SickUnpaid Count :{}  ",sickUnpaidCount);
 					amount = ((monthSalary/totalDays))*count    ;
 				}

 				else
 				{
 					basicSalary = formatDouble(basicSalary);
 			       employeeMonthlySalary.setAnnualLeaveCount(0);
 			       employeeMonthlySalary.setUnpaidLeaveCount(0);
 			       employeeMonthlySalary.setSickPaidCount(0.0);
 			        employeeMonthlySalary.setSickUnpaidCount(0.0);   
 				}
				
			}

		} catch (Exception ex) {

			failuremsgsetter(employeeMap, execptionObject, ex);

			log.error("Inside CalculationUtilsError calculateBasicSalay : {}", Utils.getStackTrace(ex), ex);
		}
		basicSalary = basicSalary-amount;
		log.debug("Employee Monthly Salary for Employee Id :{} :{} ",employee.getId(),employeeMonthlySalary);
		log.debug("the basic salary of EmployeeID :{} basicSalary :{} ",employee.getId(),basicSalary);
		return formatDouble(basicSalary);
	}

	public Double calculateShortTermIncentive(Double basicSalary, PayrollRun payrollrun,
			Map<String, Object> employeeMap, Map<Object, Object> execptionObject) {
		log.debug("going to calculateShortTermIncentive  basicSalary :{} payrollrun :{}", basicSalary, payrollrun);
		Double value = 0.0;

		try {
			Date startDate = payrollrun.getStartDate();
			Date endDate = payrollrun.getEndDate();

			Date stiDate = (Date) employeeMap.get(PRConstant.STI_DATE);

			if (isDateInBetweenIncludingEndPoints(startDate, endDate, stiDate)) {

				Double gradeMultipler = (Double) employeeMap.get(PRConstant.GRADE_MULTIPLER);

				value = basicSalary * gradeMultipler;

				return formatDouble(value);
			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateShortTermIncentive : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculateSignUpBonus(Double signUpBonus, PayrollRun payrollrun, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject) {
		log.debug("going to calculateSignUpBonus  basicSalary :{} payrollrun :{}", signUpBonus, payrollrun);
		Double value = 0.0;

		try {

			Date startDate = payrollrun.getStartDate();
			Date endDate = payrollrun.getEndDate();

			Date signUpBonusDate = (Date) employeeMap.get(PRConstant.SIGN_UP_BONUS_DATE);

			if (isDateInBetweenIncludingEndPoints(startDate, endDate, signUpBonusDate)) {

				value = signUpBonus;

				log.debug("going to calculateSignUpBonus  value :{}", value);

				return formatDouble(value);
			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateSignUpBonus : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	private void failuremsgsetter(Map<String, Object> employeeMap, Map<Object, Object> execptionObject, Exception ex) {
		execptionObject.put(employeeMap.get(PRConstant.EMPLOYEE_ID), ex.getMessage());
	}

	private Double calculateBasicSalaryAfterDeduction(Double basicSalary, Double HRA,
			Map<String, String> hrmsSystemConfigMap, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject) {
		log.debug(
				"Going to calculate Basic salary after deduction basic salary :{}  hrmsSystemConfigMap :{} employeeMap:{} ",
				basicSalary, hrmsSystemConfigMap, employeeMap);
		Double value = 0.0;
		try {

			String employeeCitizenship = (String) employeeMap.get(PRConstant.EMPLOYEE_CITIZENSHIP);
			if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.SAUDI)) {
				Double gosiEmployeepctsaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_SAUDI));

				if ((basicSalary + HRA) > 45000) {

					value = basicSalary - 3510.00;
	              	  log.debug("The Value of basic In after cap is :{} ",value);
				} else {
					Double tem = (basicSalary * gosiEmployeepctsaudi) / 100;
					log.debug("The value of amount for deducation is :{} ", tem);
					value = basicSalary - tem;
					log.debug("The value of Basic after deduction :{} ", value);

				}

				return formatDouble(value);

			} else if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.BAHRAIN)) {
				Double gosiEmployeepctb = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_BAHRAIN));

				if ((basicSalary + HRA) > 45000) {
					value = basicSalary - 2160.00;
              	  log.debug("The Value of basic In after cap is :{} ",value);
				} else {
					Double tem = (basicSalary * gosiEmployeepctb) / 100;
					log.debug("The value of amount for deducation is :{} ", tem);
					value = basicSalary - tem;
					log.debug("The value of Basic after deduction :{} ", value);

				}
				return formatDouble(value);

			} else {

				Double gosiEmployeepctnonSaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_NON_SAUDI));

				Double tem = (basicSalary * gosiEmployeepctnonSaudi) / 100;
				log.debug("The value of amount for deducation is :{} ", tem);
				value = basicSalary - tem;
				log.debug("The value of Basic after deduction :{} ", value);

				return formatDouble(value);

			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateBasicSalaryAfterDeduction : {}", Utils.getStackTrace(ex),
					ex);
		}
		return value;
	}

	private Double calculateHRAAfterDeduction(Double BasicSalary, Double HRA, Map<String, String> hrmsSystemConfigMap,
			Map<String, Object> employeeMap, Map<Object, Object> execptionObject) {
		log.debug(
				"Going to calculate Basic salary after deduction basic salary :{}  hrmsSystemConfigMap :{} employeeMap:{} ",
				HRA, hrmsSystemConfigMap, employeeMap);
		Double value = 0.0;
		try {

			String employeeCitizenship = (String) employeeMap.get(PRConstant.EMPLOYEE_CITIZENSHIP);
			if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.SAUDI)) {
				Double gosiEmployeepctsaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_SAUDI));

				if ((HRA + BasicSalary) > 45000)
				{
				   value = HRA - 877.5;
             	  log.debug("The Value of HRA In after cap is :{} ",value);
				}
				else
				{
					Double tem = (HRA * gosiEmployeepctsaudi) / 100;
					log.debug("The value of amount for deducation is :{} ", tem);
					value = HRA - tem;
					log.debug("The value of Basic after deduction :{} ", value);
				}
					return formatDouble(value);

			} else if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.BAHRAIN)) {
				Double gosiEmployeepctb = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_BAHRAIN));
                  if((HRA+BasicSalary)>45000)
                  {
                	  value = HRA - 540;
                	  log.debug("The Value of HRA In after cap is :{} ",value);
                  }
                  else
                  {
      				Double tem = (HRA * gosiEmployeepctb) / 100;
    				log.debug("The value of amount for deducation is :{} ", tem);
    				value = HRA - tem;
    				log.debug("The value of Basic after deduction :{} ", value);
                  }

				return formatDouble(value);

			} else {

				Double gosiEmployeepctnonSaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_NON_SAUDI));

				Double tem = (HRA * gosiEmployeepctnonSaudi) / 100;
				log.debug("The value of amount for deducation is :{} ", tem);
				value = HRA - tem;
				log.debug("The value of Basic after deduction :{} ", value);

				return formatDouble(value);

			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateBasicSalaryAfterDeduction : {}", Utils.getStackTrace(ex),
					ex);
		}
		return value;
	}

	public Double calculateGOSIEmployeeDeduction(Double basicSalary, Double hraValue, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject, Map<String, String> hrmsSystemConfigMap) {
		log.debug("going to calculateGOSIEmployeeDeduction  basicSalary :{} hraValue :{} hrmsSystemConfigMap :{}",
				basicSalary, hraValue, hrmsSystemConfigMap);
		Double value = 0.0;

		try {

			String employeeCitizenship = (String) employeeMap.get(PRConstant.EMPLOYEE_CITIZENSHIP);

			if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.SAUDI)) {

				Double gosiEmployeepctsaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_SAUDI));

				if ((basicSalary + hraValue) > 45000) {
					value = (45000 * gosiEmployeepctsaudi) / 100;
				} else {
					value = (basicSalary + hraValue) * gosiEmployeepctsaudi / 100;
				}
            log.debug("EmployeeGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
				return formatDouble(value);
			} else if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.BAHRAIN)) {

				Double gosiEmployeepctb = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_BAHRAIN));

				if (basicSalary != null && hraValue != null && gosiEmployeepctb != null) {

					if ((basicSalary + hraValue) > 45000) {
						value = (45000 * gosiEmployeepctb) / 100;

					} else {
						value = (basicSalary + hraValue) * gosiEmployeepctb / 100;
					}
		            log.debug("EmployeeGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
					return formatDouble(value);
				}
			} else {

				Double gosiEmployeepctnonSaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYEE_PCT_NON_SAUDI));

				if (basicSalary != null && hraValue != null && gosiEmployeepctnonSaudi != null) {

					if ((basicSalary + hraValue) > 45000) {
						value = (45000 * gosiEmployeepctnonSaudi) / 100;
					} else {
						value = (basicSalary + hraValue) * gosiEmployeepctnonSaudi / 100;
					}
		            log.debug("EmployeeGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
					return formatDouble(value);
				}
			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateGOSIEmployeeDeduction : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculateGOSIEmployerDeduction(Double basicSalary, Double hraValue, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject, Map<String, String> hrmsSystemConfigMap) {
		log.debug("going to calculateGOSIEmployeeDeduction  basicSalary :{} hraValue :{} hrmsSystemConfigMap :{}",
				basicSalary, hraValue, hrmsSystemConfigMap);
		Double value = 0.0;

		try {

			String employeeCitizenship = (String) employeeMap.get(PRConstant.EMPLOYEE_CITIZENSHIP);

			if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.SAUDI)) {

				Double gosiEmployerpctSaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYER_PCT_SAUDI));
				if ((basicSalary + hraValue) > 45000) {
					value = (45000 * gosiEmployerpctSaudi) / 100;
				} else {
					value = (basicSalary + hraValue) * gosiEmployerpctSaudi / 100;
				}
				
				
	            log.debug("EmployerGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
				return formatDouble(value);
			} else if (employeeCitizenship != null && employeeCitizenship.equalsIgnoreCase(PRConstant.BAHRAIN)) {

				Double gosiEmployeepctb = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYER_PCT_BAHRAIN));

				if (basicSalary != null && hraValue != null && gosiEmployeepctb != null) {

					if ((basicSalary + hraValue) > 45000) {
						value = (45000 * gosiEmployeepctb) / 100;
					} else {
						value = (basicSalary + hraValue) * gosiEmployeepctb / 100;
					}


		            log.debug("EmployerGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
					return formatDouble(value);
				}
			} else {

				Double gosiEmployerpctNonSaudi = Double
						.parseDouble(hrmsSystemConfigMap.get(PRConstant.GOSI_EMPLOYER_PCT_NON_SAUDI));

				if ((basicSalary + hraValue) > 45000) {
					value = (45000 * gosiEmployerpctNonSaudi) / 100;
				} else {
					value = (basicSalary + hraValue) * gosiEmployerpctNonSaudi / 100;
				}
				
	            log.debug("EmployerGosiDeduction for Citizenship :{} amount :{} ",employeeCitizenship,value);
				return formatDouble(value);
			}
		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateGOSIEmployerDeduction : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculatOverbase(Double salary, Double percentage, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject) {
		Double value = 0.0;
		log.debug("going to calculatOverbase  basicSalary :{} percentage :{}", salary, percentage);

		try {
			Double resultvalue = (salary * percentage) / 100;
			return formatDouble(resultvalue);

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculatOverbase : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculateCriticalskills(Double salary, Double percentage, Map<String, Object> employeeMap,
			Map<Object, Object> execptionObject) {
		Double value = 0.0;
		log.debug("going to calculateCriticalskills  basicSalary :{} percentage :{}", salary, percentage);
		try {

			Double resultvalue = (salary * percentage) / 100;
			return formatDouble(resultvalue);
		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateCriticalskills : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculateHousing(Double salary, Map<String, String> hrmsSystemConfigMap,PayrollRun payrollRun,Map<String,Object> employeeMap,Employee employee) {
		log.debug("going to calculateCriticalskills  basicSalary :{} hrmsSystemConfigMap :{}", salary,
				hrmsSystemConfigMap);
		Double minimumHousing = 0.0;
		Double minHra = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.MIN_HRA));

		Double hraPct = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.HRA_PCT));
		
		Integer totalDays = payrollRun.getDurationDayCount();
		Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);

		Double housing = (salary * hraPct) / 100;

		log.debug("Housing before deducation :{} employeeId :{}",housing,employee.getId());
		housing = (housing/totalDays)*workingDays;
		log.debug(" After Deducation By Days :{} ",housing);
		
		if (!housing.equals(0)) {
			log.debug("going to calculateCriticalskills housing :{} employeeId:{}", housing,employee.getId());
			minimumHousing = formatDouble(minHra);
		}
		if (housing > minimumHousing) {

			return formatDouble(housing);

		}

		return minimumHousing;
	}

	public Double calculateTransportation(Double salary, Map<String, String> hrmsSystemConfigMap,PayrollRun payrollRun,Map<String,Object> employeeMap,Employee employee) {
		log.debug("going to calculateTransportation  basicSalary :{} hrmsSystemConfigMap :{} ", salary,
				hrmsSystemConfigMap);

		Double maxTa = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.MAX_TA));

		Double taPct = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.TA_PCT));

		Integer totalDays = payrollRun.getDurationDayCount();
		Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);
		
		Double transportation = (salary * taPct) / 100;
		log.debug(" Total Transportation before Deduction :{} employeeId :{} ",transportation,employee.getId());
			
		transportation = (transportation/totalDays)*workingDays;
      log.debug(" Total Transporatation after deduction :{} employeeId :{} ",transportation,employee.getId());
		
		if (transportation < maxTa) {

			return formatDouble(transportation);
		}

		return maxTa;
	}

	public Double calculateOvertime(Double salary, Map<String, Object> employeeMap, Map<Object, Object> execptionObject,
			Map<String, String> hrmsSystemConfigMap) {
		log.debug("going to calculateOvertime  salary :{}  hrmsSystemConfigMap :{} ", salary, hrmsSystemConfigMap);
		Double value = 0.0;
		try {
			log.debug("Inside @class CalculationUtils @method calculateOvertime Class of Working Hour  :{}",
					employeeMap.get(PRConstant.EMPLOYEE_WORKING_HOUR).getClass());

			Double workingHour = (Double) employeeMap.get(PRConstant.EMPLOYEE_WORKING_HOUR);
			Double holidayWorkingHour = (Double) employeeMap.get(PRConstant.EMPLOYEE_HOLIDAY_OVERTIME);
			
            Double holidayOvertimeRate = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.HOLIDAY_OVERTIME_RATE));
			Double overtimeRate = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.OVERTIME_RATE));
			log.debug(" Holiday Overtime rate :{} ",holidayOvertimeRate);
			log.debug("going to calculateOvertime  salary :{} workinghour :{} overtimeRate :{} ", salary, workingHour,
					overtimeRate);
             Double holidayOvertime = (((salary * 12) / 365) / 8) * holidayWorkingHour * holidayOvertimeRate;
             log.debug(" Holiday Overtime :{} ",holidayOvertime);
			Double overtime = (((salary * 12) / 365) / 8) * workingHour * overtimeRate+holidayOvertime;

			return formatDouble(overtime);
		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtils Error calculateOvertime : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public boolean isDateInBetweenIncludingEndPoints(Date fromDate, Date toDate, Date date) {
		log.debug("going to isDateInBetweenIncludingEndPoints  fromDate :{} toDate :{}  date:{} ", fromDate, toDate,
				date);
		return !(date.before(fromDate) || date.after(toDate));
	}

	Double calculateRellocationAllowance(Double salary, Map<String, Object> employeeMap, PayrollRun payrollRun,
			Map<Object, Object> execptionObject, Map<String, String> hrmsSystemConfigMap) {

		log.debug("going to calculateRellocationAllowance  salary :{} employeeMap :{}  hrmsSystemConfigMap :{}", salary,
				employeeMap, hrmsSystemConfigMap);
		Double value = 0.0;
		try {

			Date relocationAllowanceDate = (Date) employeeMap.get(PRConstant.RELOCATION_ALLOWANCE_DATE);
			Date startDate = payrollRun.getStartDate();
			Date endDate = payrollRun.getEndDate();

			Long applicableDistance = Long.parseLong(hrmsSystemConfigMap.get(PRConstant.APPLICABLE_DISTANCE));

			Double singlePct = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.SINGLE_PCT));

			Double marriedPct = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.MARRIED_PCT));

			if (applicableDistance != null
					&& isDateInBetweenIncludingEndPoints(startDate, endDate, relocationAllowanceDate)) {

				String employeeMaritialStatus = (String) employeeMap.get(PRConstant.EMPLOYEE_MARITIAL_STATUS);
				log.debug("going to calculateRellocationAllowance  employeeMaritialStatus :{} ",
						employeeMaritialStatus);

				if (employeeMaritialStatus != null && !employeeMaritialStatus.isBlank()
						&& employeeMaritialStatus.equalsIgnoreCase(PRConstant.SINGLE)) {

					value = (salary * singlePct) / 100;

					return formatDouble(value);

				} else if (employeeMaritialStatus != null && !employeeMaritialStatus.isBlank()
						&& employeeMaritialStatus.equalsIgnoreCase(PRConstant.MARRIED)) {

					value = salary * marriedPct;
					return formatDouble(value);

				}
			}

		} catch (Exception ex) {
			failuremsgsetter(employeeMap, execptionObject, ex);
			log.error("Inside CalculationUtilsError calculateRellocationAllowance : {}", Utils.getStackTrace(ex), ex);
		}
		return value;
	}

	public Double calculateMobileAllowance(Map<String, Object> employeeMap, Map<String, String> hrmsSystemConfigMap,PayrollRun payrollRun,Employee employee) {

		log.debug("going to calculateMobileAllowance  employeeMap :{} hrmsSystemConfigMap :{} ", employeeMap,
				hrmsSystemConfigMap);

		EmployeeMobileType employeemobileFlag = (EmployeeMobileType) employeeMap
				.get(PRConstant.EMPLOYEE_MOBILE_ALLOWANCE_FLAG);
 
		Double employeeMobilePlan = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.EMPLOYEE_MOBILE_PLAN));
		Double companyMobile = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.COMPANY_MOBILE));
		Double companyMobilePlan = Double.parseDouble(hrmsSystemConfigMap.get(PRConstant.COMPANY_MOBILE_PLAN));
       log.debug(" employeeMobilePlan :{}  companyMobile :{} companyMobilePlan :{} employeeId :{} ",
    		   employeeMobilePlan,companyMobile,companyMobilePlan,employee.getId());
		
		if (employeemobileFlag.equals(EmployeeMobileType.EMPLOYEE_MOBILE_PLAN)) {
		 			return employeeMobilePlan;

		} else if (employeemobileFlag.equals(EmployeeMobileType.COMPANY_MOBILE_PLAN)) {
		 			return companyMobilePlan;
		} else if (employeemobileFlag.equals(EmployeeMobileType.COMPANY_MOBILE)) {
		 			return companyMobile;
		}
		return 0.0;
	}
 
	public void employeeMonthlySalary(EmployeeSalaryStructure employeeSalaryStructure, Map<String, Object> employeeMap,
			PayrollRun payrollRun, PayrollResponseWrapper payrollResponseWrapper,
			Map<Object, Object> execptionObject) {
		log.debug("inside CalculationUtils @method runDetail :{}", employeeMap);

		
		try {
			Employee employee = employeeSalaryStructure.getEmployee();
			
			EmployeeMonthlySalary employeeMonthlySalary = new EmployeeMonthlySalary();
			Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);
            
			employeeMonthlySalary.setEmployee(employee);
			employeeMonthlySalary.setPayrollRun(payrollRun);
			
			

			if (employee != null && employee.getId() != null) {

				
				
				employeeMap.put(PRConstant.STI_DATE, employeeSalaryStructure.getStiDate());
				employeeMap.put(PRConstant.RELOCATION_ALLOWANCE_DATE,
						employeeSalaryStructure.getRelocationAllowanceDate());
				employeeMap.put(PRConstant.SIGN_UP_BONUS_DATE, employeeSalaryStructure.getSignUpBonusDate());
				employeeMap.put(PRConstant.EMPLOYEE_MOBILE_ALLOWANCE_FLAG,
						employeeSalaryStructure.getEmployeeMobileFlag());
				
				Double workingHours = getEmployeeOvertimeSum(payrollRun,employee.getId(),employeeMap);
				
				employeeMap.put(PRConstant.EMPLOYEE_WORKING_HOUR, workingHours);
				employeeMap.put(PRConstant.EMPLOYEE_ID, "emp-" + employee.getId());

//				processEmployeeBasedOnType(employeeSalaryStructure, employeeMap, payrollRun, employee,
//						payrollResponseWrapper, execptionObject, runDetails);
//
//				setPayrollVarianceAndSum(payrollRun);
//				
//				identifyRemarkForPayrollVariance(payrollRun);
	            
				
				processEmployeeBasedOnType(employeeSalaryStructure, employeeMap, payrollRun, employee, payrollResponseWrapper, execptionObject, employeeMonthlySalary);

	    
				//Integer payrollId = payrollRun.getId();
			
				//CompletableFuture.runAsync(() -> generateExcelInPayrollRun(payrollId));

			} else {
				log.error("inside CalculationUtils @method employee null ");
			}
		} catch (Exception ex) {
			log.error("Inside CalculationUtilsError  runDetail method ex : {}", Utils.getStackTrace(ex), ex);

		}
	}

	private void processEmployeeBasedOnType(EmployeeSalaryStructure employeeSalaryStructure,
			Map<String, Object> employeeMap, PayrollRun payrollRun, Employee employee,
			PayrollResponseWrapper payrollResponseWrapper, Map<Object, Object> execptionObject, EmployeeMonthlySalary employeeMonthlySalary) {
		try {
		Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();

		String listofFullTimeEmployee = hrmsSystemConfigMap.get(PRConstant.FULL_TIME_EMPLOYEE_EMPLOYEMENT_TYPE_LIST);
		String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);

		String employementType = employee.getEmploymentType();

		log.debug(
				"inside CalculationUtils @method listofFullTimeEmployee :{}  employementType:{}  employeeId :{}  hrmsSystemConfigMap :{}",
				listofFullTimeEmployee, employementType, employee.getId(), hrmsSystemConfigMap);

		boolean check = checkIfExists(employementType, listofFullTimeEmployee);
		boolean checkOnBoardMember = checkIfExists(employementType, onboardMemberTypeList); 
		
		log.debug("inside CalculationUtils @method processFullTimeEmployee check {}", check);

		
		if (listofFullTimeEmployee != null && !listofFullTimeEmployee.isEmpty() && check) {

			log.debug("inside CalculationUtils fulltime employee ");

			processFullTimeEmployee(employeeSalaryStructure, employeeMap, payrollRun, payrollResponseWrapper,
					execptionObject, employeeMonthlySalary, hrmsSystemConfigMap);
		} else  if ( onboardMemberTypeList!=null && !onboardMemberTypeList.isEmpty() && checkOnBoardMember ){
		
			log.info("inside @class CalculationUtils @method processEmployeeBasedOnType Employee is Board Member ");
			processBoardMemberEmployee(employee.getId() , employeeMonthlySalary , payrollRun);
		}
		else{
			log.debug("inside CalculationUtils contract employee ");
		}
		}catch(Exception ex) {
			log.error("Inside CalculationUtilsError processEmployeeBasedOnType : {}", Utils.getStackTrace(ex), ex);
		}
	}
 

	private void processFullTimeEmployee(EmployeeSalaryStructure employeeSalaryStructure,
			Map<String, Object> employeeMap, PayrollRun payrollRun, 
			PayrollResponseWrapper payrollResponseWrapper, Map<Object, Object> execptionObject, EmployeeMonthlySalary employeeMonthlySalary,
			Map<String, String> hrmsSystemConfigMap) {
		try {
			Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);
			employeeMonthlySalary.setWorkingDays(workingDays);
				Employee employee = employeeSalaryStructure.getEmployee();
		log.debug("inside CalculationUtils @method processFullTimeEmployee");

		processBasicSalaryDetails(employeeSalaryStructure, employeeMap, payrollRun, employee, execptionObject,
				employeeMonthlySalary, hrmsSystemConfigMap);

			if (!execptionObject.isEmpty()) {
				Integer failure = payrollResponseWrapper.getFailureCount();
				failure = failure + 1;
				payrollResponseWrapper.setFailureCount(failure);
				payrollResponseWrapper.setResponseList(execptionObject);
			} else if (payrollResponseWrapper != null && payrollResponseWrapper.getSucessCount() != null) {
				Integer sucesscount = payrollResponseWrapper.getSucessCount();
				sucesscount = sucesscount + 1;
				payrollResponseWrapper.setSucessCount(sucesscount);
			}
		} catch (Exception ex) {
			log.error("Inside CalculationUtilsError processFullTimeEmployee : {}", Utils.getStackTrace(ex), ex);

		}
	}

	
	
	public void processBoardMemberEmployee(Integer employeeId,EmployeeMonthlySalary runDetail ,PayrollRun payrollRun)
	{
		log.info("Inside @class CalculationUtils @method processBoardMemberEmployee");
		try
		{
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
		    Employee optionalEmployee = employeeService.findById(employeeId);
		    if(optionalEmployee != null)
		    {
		    	
		    	Employee employee = optionalEmployee;
 		    	String employeeType = employee.getEmploymentType();
		    	log.debug("The Employee_ment type of Employee :{}",employeeType);
		    	
		    		log.info("Inside @class CalculationUtils @method processBoardMemberEmployee Employee is board member");
		    		Date createdTime = payrollRun.getEndDate(); 
		    		LocalDate localDate = createdTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		            
		            int month = localDate.getMonthValue();
		    		log.debug("Inside @class CalculationUtils @method processBoardMemberEmployee month :{}",month);
		            if(month == 3 || month == 6 || month==9 || month ==12 )
		    		{
			    		EmployeeSalaryStructure employeeSalaryStructure = getEmployeeSalaryStructureObject(employeeId);
			    		Double boardMemberAmount = employeeSalaryStructure.getBoardMemberAmount();
			    		boardMemberAmount = boardMemberAmount/4;
			    log.debug("Inside @class CalculationUtils @method processBoardMemberEmployee amount of Board member after dividing by 4 :{}",boardMemberAmount);
			    log.debug("Inside @class CalculatioUtils @method processBoardMemberEmployee :{}",runDetail.getId());		
			    log.debug("The ID of Employee is :{}",runDetail.getEmployee().getId());
			  
			    		runDetail.setNetAmount(boardMemberAmount);
			    		
			    		 
			    		
           EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
			    		employeeMonthlySalaryService.create(runDetail);
			    		
		    		}
		    }
		}
		catch(Exception e)
		{
			log.error("Error inside  @class CalculationUtils @method processBoardMemberEmployee :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	       throw new BusinessException();
		}
		
	}
	
	
	private void processBasicSalaryDetails(EmployeeSalaryStructure employeeSalaryStructure,
			Map<String, Object> employeeMap, PayrollRun payrollRun, Employee employee,
			Map<Object, Object> execptionObject, EmployeeMonthlySalary employeeMonthlySalary, Map<String, String> hrmsSystemConfigMap) {
		
		if (employeeSalaryStructure.getBasicSalary() != null) {
			try {
				log.info("Inside @class CalculationUtils @method processBasicSalary Details ");
				log.debug("Going to start calculation for EmployeeId :{} ",employee.getId());
			Double totalEarning;
			Double totalDeduction;
			Double netAmount;
			Double monthSalary = employeeSalaryStructure.getBasicSalary();
			Double basicSalary = calculateBasicSalay(monthSalary, payrollRun, employeeMap, execptionObject,employee,employeeMonthlySalary);
            
			Double criticalSkillValue = ifEmployeeMappingDotGettNegoPctCriticalNotNullInEmployeeMonthlySalary(employeeSalaryStructure,
					employeeMap, execptionObject, basicSalary);
			
			Double overbase = ifEmployeeMappingDotGetNegoPctOverbaseNotNullInsideEmployeeMonthlySalary(employeeSalaryStructure,
					employeeMap, execptionObject, basicSalary);
					
			Double hra = calculateHousing(monthSalary, hrmsSystemConfigMap,payrollRun,employeeMap,employee);
			Double ta = calculateTransportation(monthSalary, hrmsSystemConfigMap,payrollRun,employeeMap,employee);
			Double mobileAllow = ifEmployeeMappingDotGetEmployeeMobileFlagNotNulInEmployeeMonthlySalary(employeeSalaryStructure,
					employeeMap, employeeMonthlySalary, hrmsSystemConfigMap,payrollRun,employee);
			
			
			Double sti = ifEmployeeMappingDotGetStiDateNotNullInEmployeeMonthlySalary(employeeSalaryStructure, employeeMap, payrollRun,
					execptionObject, basicSalary);
			
			Double signUpBonus = ifEmployeeMappingDotGetSignUpBonusNotNullInEmployeeMonthlySalary(employeeSalaryStructure, employeeMap,
					payrollRun, execptionObject);
			

			Double rellocationAllowance = ifEmployeeMappingDotGetRelocationAllowanceDateNotNullInEmployeeMonthlySalary(
					employeeSalaryStructure, employeeMap, payrollRun, execptionObject,
					basicSalary, hrmsSystemConfigMap);
		
			Double overtime = calculateOvertime(basicSalary, employeeMap, execptionObject, hrmsSystemConfigMap);
			
			Double employeeDeduction = calculateGOSIEmployeeDeduction(basicSalary, hra, employeeMap, execptionObject,
					hrmsSystemConfigMap);
			

			Double employerDeduction = calculateGOSIEmployerDeduction(basicSalary, hra, employeeMap, execptionObject,
					hrmsSystemConfigMap);
			
			Double basicSalaryAfterDeduction = calculateBasicSalaryAfterDeduction(basicSalary,hra, hrmsSystemConfigMap,
					employeeMap, execptionObject);

			Double HRAAfterDeduction = calculateHRAAfterDeduction(basicSalary ,hra, hrmsSystemConfigMap, employeeMap,
					execptionObject);
						 
			Integer annualLeaveCount = calculateAnnualLeaveDays(employee,payrollRun);
			
			Integer totalDays = payrollRun.getDurationDayCount();
			Integer workingDays = (Integer) employeeMap.get(PRConstant.EMPLOYEE_WORKING_DAYS);
			Integer unpaidLeaveCount  = totalDays-workingDays;
			log.debug(" Inside @method processBasicSalaryDetails totalDays :{} workingDays :{} unpaidLeaveCount :{} ",
					totalDays,workingDays,unpaidLeaveCount);
 			log.debug(
					"inside CalculationUtils @method runDetail basicSalary :{} criticalSkillValue:{}overbase:{} hra:{} ta:{} mobileAllow:{} overtime :{} employeeid:{}  rellocationAllowance:{} ",
					basicSalary, criticalSkillValue, overbase, hra, ta, mobileAllow, overtime, employee.getId(),
					rellocationAllowance);
			
			totalEarning = basicSalary + criticalSkillValue + overbase + hra + ta + sti + signUpBonus
					+ rellocationAllowance + overtime + mobileAllow ;

				log.debug(
						"inside CalculationUtils @method runDetail basicSalary :{} criticalSkillValue:{}overbase:{} hra:{} ta:{} mobileAllow:{} overtime :{} employeeid:{}  rellocationAllowance:{} ",
						basicSalary, criticalSkillValue, overbase, hra, ta, mobileAllow, overtime, employee.getId(),
						rellocationAllowance);

				totalEarning = basicSalary + criticalSkillValue + overbase + hra + ta + sti + signUpBonus
						+ rellocationAllowance + overtime + mobileAllow;

				totalEarning = formatDouble(totalEarning);
				log.debug("inside CalculationUtils @method runDetail totalEarning :{}", totalEarning);

				totalDeduction = employeeDeduction;

				totalDeduction = formatDouble(totalDeduction);
				log.debug("inside CalculationUtils @method runDetail totalDeduction :{}", totalDeduction);
				netAmount = totalEarning - totalDeduction;

				netAmount = formatDouble(netAmount);

				log.debug("inside CalculationUtils @method runDetail netAmount :{}", netAmount);
				
				Double otherEarning = setOtherEarningDetails(payrollRun, employee);

				log.debug("inside CalculationUtils @method runDetail otherEarning2 :{}", otherEarning);

				totalEarning = totalEarning + otherEarning;

				totalEarning = formatDouble(totalEarning);

				Double otherdeduction = setOtherDeductionDetails(payrollRun, employee, basicSalary,
						hrmsSystemConfigMap);

				log.debug("inside CalculationUtils @method runDetail otherdeduction :{}", otherdeduction);

				totalDeduction = totalDeduction + otherdeduction;

				totalDeduction = formatDouble(totalDeduction);

				log.debug("inside CalculationUtils @method runDetail totalDeduction2 :{}", totalDeduction);

				netAmount = totalEarning - totalDeduction;

				netAmount = formatDouble(netAmount);

				log.debug("inside CalculationUtils @method runDetail netAmount2 :{}", netAmount);


				EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext()
						.getBean(EmployeeMonthlySalaryService.class);
				BigDecimal recentSalary = employeeMonthlySalaryService.getRecentNetworthForEmployee(employee.getId());

				if (recentSalary != null) {

					Double recentSalaryd = formatDouble(recentSalary);
					employeeMonthlySalary.setLastMonthNetAmount(recentSalaryd);
					log.debug("inside CalculationUtils @method runDetail  recentSalary  :{} recentSalary :{}",
							recentSalary, recentSalaryd);
					calculateVarianceForEmployee(employeeMonthlySalary, netAmount, recentSalaryd);
				} else {
					employeeMonthlySalary.setVariance(0.0);
					employeeMonthlySalary.setVarianceAmount(0.0);
					employeeMonthlySalary.setLastMonthNetAmount(0.0);
				}

				// runDetails= getBenefitsAndSum(employee.getId(),runDetails,payrollRun);
//			Double benefitAmount = runDetails.getBenefitAmount();
//			totalEarning  = totalEarning + benefitAmount;
			
			employeeMonthlySalary.setBasicSalary(basicSalary);
			employeeMonthlySalary.setCriticalSkills(criticalSkillValue);
			employeeMonthlySalary.setOverbase(overbase);
			employeeMonthlySalary.setHra(hra);
			employeeMonthlySalary.setTa(ta);
			employeeMonthlySalary.setSti(sti);
			employeeMonthlySalary.setSignUpBonus(signUpBonus);
			employeeMonthlySalary.setRelocationAllowance(rellocationAllowance);
			employeeMonthlySalary.setOvertime(overtime);
				employeeMonthlySalary.setGosiEmployee(employeeDeduction);
				employeeMonthlySalary.setGosiEmployer(employerDeduction);	
			 
			employeeMonthlySalary.setTotalEarningAmount(totalEarning);
			employeeMonthlySalary.setTotalDeductionAmount(totalDeduction);
			employeeMonthlySalary.setNetAmount(netAmount);
			
			employeeMonthlySalary.setOtherDeduction(otherdeduction);
			employeeMonthlySalary.setOtherEarning(otherEarning);
			employeeMonthlySalary.setBasicSalaryAfterDeduction(basicSalaryAfterDeduction);
			employeeMonthlySalary.setHraAfterDeduction(HRAAfterDeduction);
            employeeMonthlySalary.setAnnualLeaveCount(Optional.ofNullable(annualLeaveCount).orElse(0));
            employeeMonthlySalary.setUnpaidLeaveCount(Optional.ofNullable(unpaidLeaveCount).orElse(0));	
			
			 
			employeeMonthlySalary = employeeMonthlySalaryService.create(employeeMonthlySalary);


				log.debug("inside CalculationUtils @method runDetail runDetails :{}", employeeMonthlySalary);
			} catch (Exception ex) {
				log.error("Inside CalculationUtils  processBasicSalaryDetails : {}", Utils.getStackTrace(ex), ex);
			}
		} else {

			log.debug("inside CalculationUtils @method basicsalary null for employee :{}",
					employeeMap.get(PRConstant.EMPLOYEE_ID));
			execptionObject.put(employeeMap.get(PRConstant.EMPLOYEE_ID), "basicsalary null for employee");

		}
	}

	private Double setOtherDeductionDetails(PayrollRun payrollRun, Employee employee, Double basicSalary,
			Map<String, String> hrmsSystemConfigMap) {
		Double totalDeductionAmount = 0.0;
		try {
			if (payrollRun == null) {
				return totalDeductionAmount;
			}
			log.debug("inside CalculationUtils @method setOtherEarningDetails payrollRun :{} employee:{} ",
					payrollRun.getId(), employee.getId());
			List<OtherSalaryComponent> listOtherSalary = otherSalaryComponentService
					.findByEmployeeIdAndTypeAndDeleted(employee.getId(), Type.DEDUCTION, false);
			if (listOtherSalary == null || listOtherSalary.isEmpty()) {
				return totalDeductionAmount;
			}
			log.debug("inside CalculationUtils @method setOtherDeductionDetails listOtherSalary is not empty");
			for (OtherSalaryComponent otherSalary : listOtherSalary) {
				if (otherSalary.getAmount() != null) {

					String limitdeductionper = hrmsSystemConfigMap.get(PRConstant.MIN_DEDUCTION_BASIC_PER);
					Double per = Double.parseDouble(limitdeductionper);
					log.debug("inside CalculationUtils @method setOtherDeductionDetails otherSalary :{} per :{} ",
							otherSalary, per);
					Double maxDeduction = basicSalary * per;
					Double deductionAmount = Math.min(otherSalary.getAmount(), maxDeduction);
					Double remainingDeduction = otherSalary.getAmount() - deductionAmount;
					totalDeductionAmount += deductionAmount;

					if (otherSalary.getPaidAmount() != null && !otherSalary.getPaidAmount().equals(0)) {
						Double totalOtherDeduction = deductionAmount + otherSalary.getPaidAmount();
						otherSalary.setPaidAmount(totalOtherDeduction);
						log.debug(
								"inside CalculationUtils @method setOtherDeductionDetails totalOtherDeduction :{}  otherSalaryId :{} remainingDeduction :{} ",
								totalOtherDeduction, otherSalary.getId(), remainingDeduction);

					} else {
						log.debug(
								"inside CalculationUtils @method setOtherDeductionDetails deductionAmount :{} otherSalaryId :{} ",
								deductionAmount, otherSalary.getId());
						otherSalary.setPaidAmount(deductionAmount);
					}
					// Update the remaining deduction amount and workflow stage
					if (remainingDeduction > 0) {
						otherSalary.setAmount(remainingDeduction);
						otherSalary.setWorkflowStage(PRConstant.PENDING);
					} else {
						otherSalary.setAmount(0.0);
						otherSalary.setWorkflowStage(PRConstant.DEDUCTED);
					}
					otherSalary = otherSalaryComponentService.update(otherSalary);

					setPayrollMappingDetails(otherSalary, payrollRun, employee, deductionAmount);
				}
			}
			totalDeductionAmount = formatDouble(totalDeductionAmount);

		} catch (Exception ex) {
			log.error("Inside CalculationUtils setOtherDeductionDetails : {}", Utils.getStackTrace(ex), ex);
		}
		return totalDeductionAmount;
	}

	private void setPayrollMappingDetails(OtherSalaryComponent otherSalary, PayrollRun payrollRun, Employee employee,
			Double amount) {

		if (otherSalary != null && payrollRun != null) {
			log.debug("inside CalculationUtils @method setPayrollMappingDetails payrollRun :{} employee:{} ",
					payrollRun, otherSalary);

			OtherSalaryPayrollMapping otherSalaryPayrollMapping = new OtherSalaryPayrollMapping();

			otherSalaryPayrollMapping.setPayrollRunId(payrollRun.getId());

			otherSalaryPayrollMapping.setEmployee(employee);

			otherSalaryPayrollMapping.setAmount(amount);

			otherSalaryPayrollMapping.setOtherSalaryComponent(otherSalary);

			otherSalaryPayrollMappingService.create(otherSalaryPayrollMapping);

		}

	}

	private Double setOtherEarningDetails(PayrollRun payrollRun, Employee employee) {
		Double otherEarning = 0.0;

		try {
			if (payrollRun != null) {
				log.debug("inside CalculationUtils @method setOtherEarningDetails payrollRun :{} employee:{} ",
						payrollRun, employee);
				List<OtherSalaryComponent> listOtherSalary = otherSalaryComponentService
						.findByEmployeeIdAndTypeAndDeleted(employee.getId(), Type.EARNING, false);

				if (listOtherSalary != null && !listOtherSalary.isEmpty()) {
					log.debug("inside CalculationUtils @method setOtherEarningDetails listOtherSalary is not empty");

					for (OtherSalaryComponent otherSalary : listOtherSalary) {
						if (otherSalary.getAmount() != null) {
							otherEarning = otherEarning + otherSalary.getAmount();
							otherSalary.setWorkflowStage(PRConstant.PAID);
							otherSalary = otherSalaryComponentService.update(otherSalary);

							setPayrollMappingDetails(otherSalary, payrollRun, employee, otherSalary.getAmount());
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("Error Inside CalculationUtilsError setOtherEarningDetails : {} :{}", Utils.getStackTrace(ex), ex);
		}

		return otherEarning;
	}

	private Double ifEmployeeMappingDotGetEmployeeMobileFlagNotNulInEmployeeMonthlySalary(EmployeeSalaryStructure employeeMapping,
			Map<String, Object> employeeMap, EmployeeMonthlySalary employeeMonthlySalary,
			Map<String, String> hrmsSystemConfigMap,PayrollRun payrollRun,Employee employee) {
		Double mobileAllow  =0.0;
		 if (employeeMapping.getEmployeeMobileFlag() != null) {
			mobileAllow = calculateMobileAllowance(employeeMap, hrmsSystemConfigMap,payrollRun,employee);
			employeeMonthlySalary.setMobileAllowance(mobileAllow);
		}
		return mobileAllow;
	}

	private void calculateVarianceForEmployee(EmployeeMonthlySalary employeeMonthlySalary, Double netAmount, Double recentSalary) {
		log.debug("Inside CalculationUtils @method calculateVarianceForEmployee netAmount:{} recentSalary:{}, employeeId:{} ",netAmount ,recentSalary,employeeMonthlySalary.getEmployee().getId());
		if (recentSalary != null && netAmount != null && netAmount != 0.0) {
		
			Double variance = ((netAmount - recentSalary) / recentSalary) * 100;
			Double varianceAmount = netAmount - recentSalary;
			log.debug("inside CalculationUtils @method calculateVarianceForEmployee variance:{} , varianceAmount:{} ", variance,varianceAmount );
			variance =formatDouble(variance);
			varianceAmount=formatDouble(varianceAmount);
			log.debug("inside CalculationUtils @method calculateVarianceForEmployee format variance:{} , varianceAmount:{} ", variance,varianceAmount);
			employeeMonthlySalary.setVariance(variance);
			employeeMonthlySalary.setVarianceAmount(varianceAmount);
		} else {
			employeeMonthlySalary.setVariance(0.0);
			employeeMonthlySalary.setVarianceAmount(0.0);
			
		}
	}

	private Double ifEmployeeMappingDotGetRelocationAllowanceDateNotNullInEmployeeMonthlySalary(
			EmployeeSalaryStructure employeeMapping, Map<String, Object> employeeMap, PayrollRun payrollRun,
			Map<Object, Object> execptionObject, Double basicSalary,
			Map<String, String> hrmsSystemConfigMap) {
		Double rellocationAllowance= 0.0;
		if (employeeMapping.getRelocationAllowanceDate() != null && employeeMapping.getRelocationAllowance() != null) {
			rellocationAllowance = calculateRellocationAllowance(basicSalary, employeeMap, payrollRun, execptionObject,
					hrmsSystemConfigMap);
		}
		return rellocationAllowance;
	}

	private Double ifEmployeeMappingDotGetSignUpBonusNotNullInEmployeeMonthlySalary(EmployeeSalaryStructure employeeMapping,
			Map<String, Object> employeeMap, PayrollRun payrollRun, Map<Object, Object> execptionObject) {
		Double signUpBonus=0.0;
		if ((employeeMapping.getSignUpBonus() != null || employeeMapping.getSignUpBonus()  !=0.0 ) && employeeMapping.getSignUpBonusDate() != null) {
			signUpBonus = employeeMapping.getSignUpBonus();
			signUpBonus = calculateSignUpBonus(signUpBonus, payrollRun, employeeMap, execptionObject);

		}
		return signUpBonus;
	}

	private Double ifEmployeeMappingDotGetStiDateNotNullInEmployeeMonthlySalary(EmployeeSalaryStructure employeeMapping,
			Map<String, Object> employeeMap, PayrollRun payrollRun, Map<Object, Object> execptionObject,
			Double basicSalary) {
		Double sti=0.0;
		if (employeeMapping.getStiDate() != null && (employeeMapping.getSti() != null|| employeeMapping.getSti() !=0.0 )) {
			sti = calculateShortTermIncentive(basicSalary, payrollRun, employeeMap, execptionObject);
		}
		return sti;
	}

	private Double ifEmployeeMappingDotGetNegoPctOverbaseNotNullInsideEmployeeMonthlySalary(EmployeeSalaryStructure employeeMapping,
			Map<String, Object> employeeMap, Map<Object, Object> execptionObject, Double basicSalary) {
		Double overbase=0.0;
		
		if (employeeMapping.getNegoPctOverbase() != null && (employeeMapping.getOverbase() != null || employeeMapping.getOverbase()!=0.0)) {

			Double pct = employeeMapping.getNegoPctOverbase();
			overbase = calculatOverbase(basicSalary, pct, employeeMap, execptionObject);

		}
		return overbase;
	}

	private Double ifEmployeeMappingDotGettNegoPctCriticalNotNullInEmployeeMonthlySalary(EmployeeSalaryStructure employeeMapping,
			Map<String, Object> employeeMap, Map<Object, Object> execptionObject, 
			Double basicSalary) {
		Double criticalSkillValue=0.0;
		if (employeeMapping.getNegoPctCritical() != null && (employeeMapping.getCriticalSkills() != null || employeeMapping.getCriticalSkills()!=0.0)) {
			Double pct = employeeMapping.getNegoPctCritical();
			 criticalSkillValue = calculateCriticalskills(basicSalary, pct, employeeMap, execptionObject);

		}
		return criticalSkillValue;
	}

	public boolean checkIfExists(String checkString, String inputString) {
		log.debug("inside CalculationUtils @method checkIfExists inputString :{}  checkString:{} ", inputString,
				checkString);
		try {
			String[] parts = inputString.split(",");
			log.debug("inside CalculationUtils @method checkIfExists iparts :{} ", parts);
			for (String part1 : parts) {
				log.debug("inside CalculationUtils @method checkIfExists iparts1{} ", part1);
				if (part1.contains(checkString)) {
					log.debug("inside CalculationUtils @method checkIfExists iparts true {} checkString {} ", part1,
							checkString);
					return true;
				}
			}
		} catch (Exception ex) {
			log.error("inside CalculationUtilsError @method checkIfExists error :{} ", Utils.getStackTrace(ex), ex);
		}
		return false;
	}

	private void generateExcelInPayrollRun(Integer payrollRunId) {
		log.debug("inside CalculationUtils @method generateExcelInPayrollRun payrollRunId :{} ", payrollRunId);
		try {
			 EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
			employeeMonthlySalaryService.generateExcelForPayroll(payrollRunId);
		} catch (IOException e) {
			log.error("Exception occured inside class CalculationUtilsError method generateExcelInPayrollRun {} ",
					e.getMessage());

		}
	}

	public Double formatDouble(Object value) {
		log.debug("inside CalculationUtils @method formatDouble value :{} ", value);
		if (value != null && value instanceof BigDecimal) {
			return ((BigDecimal) value).doubleValue();
		} else if (value != null) {
			return Double.parseDouble(df.format(value));
		} else {
			return 0.0;
		}
	}

//  private RunDetails getBenefitsAndSum(Integer eid , RunDetails runDetails, PayrollRun payrollRun)
//  {
//   log.info("Inside @class CalculationUtils @method getBenefitsAndSum ");	  
//  HealthClubBenefitService  healthClubBenefitService = ApplicationContextProvider.getApplicationContext().getBean(HealthClubBenefitService.class);
// EducationalBenefitService educationalBenefitService = ApplicationContextProvider.getApplicationContext().getBean(EducationalBenefitService.class);  
// ChildEducationBenefitService childEducationBenefitService = ApplicationContextProvider.getApplicationContext().getBean(ChildEducationBenefitService.class);
//  NewHireBenefitService newHireBenefitService  = ApplicationContextProvider.getApplicationContext().getBean(NewHireBenefitService.class); 
//  
//  try{
//	  HashMap<Integer, Double> healthClubBenefitMap = new HashMap<>();
//	  
//	  healthClubBenefitMap	 =  healthClubBenefitService.getIdAndAmountByEmployeeId(eid ,payrollRun.getRunDate());
//	Double healthClubBenefitSum = healthClubBenefitService.getSumOfAmount(healthClubBenefitMap);
// 
//	 HashMap<Integer, Double> educationalBenefitMap = new HashMap<>();
//	educationalBenefitMap =  educationalBenefitService.getIdAndAmountByEmployeeId(eid);
//	Double educationalBenefitSum = educationalBenefitService.getSumOfAmount(educationalBenefitMap);
//	
//	HashMap<Integer, Double> childEducationBenefitMap = new HashMap<>();
//   
//      childEducationBenefitMap =  childEducationBenefitService.getIdAndAmountByEmployeeId(eid);
//	Double childEducationBenefitSum = childEducationBenefitService.getSumOfAmount(childEducationBenefitMap);
//	
//	HashMap<Integer, Double> newHireBenefitMap = new HashMap<>();
//	
//  newHireBenefitMap = newHireBenefitService.getIdAndAmountByEmployeeId(eid);
//	Double newHireBenefitSum = newHireBenefitService.getSumOfAmount(newHireBenefitMap);
//	
//	healthClubBenefitSum = healthClubBenefitSum/12;
//	
//	
//	
//	Double sum = healthClubBenefitSum + educationalBenefitSum + childEducationBenefitSum + newHireBenefitSum;
//	log.debug("Inside @class CalculationUtils @method getBenefitsAndSum Sum:{}",sum);
//	
//	List<Integer> healthClubBenefitsKeys = getKeysList(healthClubBenefitMap);
//	healthClubBenefitService.setWorkFlowStageForBulk(healthClubBenefitsKeys,PRConstant.PAID_SM);
//   
//	
//	List<Integer> educationalBenefitKeys = getKeysList(educationalBenefitMap);
//	educationalBenefitService.setWorkFlowStageForBulk(educationalBenefitKeys,PRConstant.PAID_SM);
//	
//	   
//	List<Integer> childEducationBenefitKeys = getKeysList(childEducationBenefitMap);
//	childEducationBenefitService.setWorkFlowStageForBulk(childEducationBenefitKeys, PRConstant.PAID_SM);
//	
//	List<Integer> newHireBenefitKeys = getKeysList(newHireBenefitMap);
//	newHireBenefitService.setWorkFlowStageForBulk(newHireBenefitKeys,PRConstant.PAID_SM);
//	log.debug("Inside @class CalculationUtils @method getBenefitsAndSum workFlowStage changed to :{}",PRConstant.PAID);
//	
//	
//	runDetails.setBenefitAmount(sum);
//	
//	ObjectMapper objectMapper = new ObjectMapper();
////	HashMap<String, List<Integer>> jsonHashMap = storingDataInJson(healthClubBenefitsKeys,educationalBenefitKeys,childEducationBenefitKeys,newHireBenefitKeys);
//	String jsonListString = objectMapper.writeValueAsString(jsonHashMap);
//
//	runDetails.setBenefitObjectIdJson(jsonListString);
//	return runDetails;
// 
//  }
//  catch(IOException io)
//  {
//		log.error("IO Exception inside Inside @class CalculationUtils @method getBenefitsAndSum :{} :{}",
//				 io.getMessage(),Utils.getStackTrace(io));
//		throw new BusinessException();
//  }
//   catch(Exception e)
//   {
//		log.error("Error Inside @class CalculationUtils @method getBenefitsAndSum :{} :{}",
//				 e.getMessage(),Utils.getStackTrace(e));
//	throw new BusinessException();
//   }
//  
//}
//  
//  
//  public List<Integer> getKeysList(HashMap<Integer, Double> hashMap) {
//     
//      List<Integer> keysList = new ArrayList<>();
//	  
//      for (Map.Entry<Integer, Double> entry : hashMap.entrySet()) { 
//          keysList.add(entry.getKey());
//      }
//      return keysList;
//  }
// 
//  private HashMap<String, List<Integer>> storingDataInJson(List<Integer> healthClubBenefitsKeys,List<Integer> educationalBenefitKeys,List<Integer> childEducationBenefitKeys,List<Integer> newHireBenefitKeys)
//  {
//	  HashMap<String, List<Integer>> jsonHashMap = new HashMap<>();
//	  jsonHashMap.put(PRConstant.HEALTH_CLUB_BENEFIT, healthClubBenefitsKeys);
//	  jsonHashMap.put(PRConstant.EDUCATION_BENEFIT,educationalBenefitKeys );
//	  jsonHashMap.put(PRConstant.CHILD_EDUCATION_BENEFIT, childEducationBenefitKeys);
//	  jsonHashMap.put(PRConstant.NEW_HIRE_BENEFIT, newHireBenefitKeys);  
//	 return jsonHashMap;  
//  }
	private EmployeeSalaryStructure getEmployeeSalaryStructureObject(Integer Id) {
		log.info("Inside @class CalculationUtils @method getEmployeeSalaryStructureObject ");
		try {
			EmployeeSalaryStructureService employeeSalaryStructureService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeSalaryStructureService.class);
			List<EmployeeSalaryStructure> employeeSalaryStructureList = employeeSalaryStructureService
					.findByEmployeePk(Id);
			log.info(
					"Inside @class CalculationUtils @method getEmployeeSalaryStructureObject  Employee Salary Structure Present ");
			EmployeeSalaryStructure employeeSalaryStucture = null;
			for (EmployeeSalaryStructure employeeSalaryStructureInloop : employeeSalaryStructureList) {
				employeeSalaryStucture = employeeSalaryStructureInloop;
			}
			return employeeSalaryStucture;
		} catch (Exception e) {
			log.error("Error inside @class CalculationUtils @method getEmployeeSalaryStructureObject :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	private Double getEmployeeOvertimeSum(PayrollRun payrollRun, Integer employeeId,Map<String, Object> employeeMap) {
		log.info("Inside @class CalculationUtils @method getEmployeeOvertimeSum ");
		log.debug("Inside @class CalculationUtils @method getEmployeeOvertimeSum  EmployeeId :{}", employeeId);
		try {
			OvertimeService overtimeService = ApplicationContextProvider.getApplicationContext()
					.getBean(OvertimeService.class);
			OvertimeLogsService overtimeLogsService = ApplicationContextProvider.getApplicationContext()
					.getBean(OvertimeLogsService.class);
			HolidayService holidayService = ApplicationContextProvider.getApplicationContext()
					.getBean(HolidayService.class);
			List<Holiday> holidayList = holidayService.findAll();
			List<Overtime> overtimeList = overtimeService.getListOfOvertimeByEmployeeId(employeeId);
			Double overtimeHourTotalSum = 0.0;
			Double overtimeHourOnHoliday = 0.0;
			for (Overtime overtime : overtimeList) {
				//Double OvertimeLogsHoursSum = 0.0;
				Integer overtimeId = overtime.getId();
				log.debug("Inside @class CalculationUtils @method getEmployeeOvertimeSum OvertimeId :{} ", overtimeId);
				List<OvertimeLogs> overtimeLogsList = overtimeLogsService.getOvertimeLogsbyOvertimeIdAndPayrollDate(
						overtimeId, payrollRun.getStartDate(), payrollRun.getEndDate());
				Double[] updatedHours = processOvertimeLogs(overtimeLogsList, holidayList);
	            overtimeHourTotalSum += updatedHours[0];
	            overtimeHourOnHoliday += updatedHours[1];

			}
 
			List<OvertimeLogs> remainingOvertimeLogsList = overtimeLogsService.getOvertimeLogsForEmployeeByWorkflowStageAndDate(employeeId, PRConstant.PENDING_SM, payrollRun.getStartDate());
			Double[] updatedHours = processOvertimeLogs(remainingOvertimeLogsList, holidayList);
            overtimeHourTotalSum += updatedHours[0];
            overtimeHourOnHoliday += updatedHours[1];
			
			log.debug("Inside @class CalculationUtils @method getEmployeeOvertimeSum the total hours :{} Hour on Holiday",
					overtimeHourTotalSum,overtimeHourOnHoliday);
			employeeMap.put(PRConstant.EMPLOYEE_HOLIDAY_OVERTIME , overtimeHourOnHoliday);
	 
			
			return overtimeHourTotalSum;

		} catch (Exception e) {
			log.error("Error inside @class CalculationUtils @method getEmployeeOvertimeSum :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	
	private Integer calculateAnnualLeaveDays(Employee employee,PayrollRun payrollRun)
	{
		try
		{
			log.info("Inside @class CalculationUtils @method calculateAnnualLeaveDays ");
			LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext().getBean(LeaveTypeService.class);
            LeaveType leaveType =   leaveTypeService.findByName(PRConstant.ANNUAL_LEAVE_SM);
            EmployeeDailyAttendanceService employeeDailyAttendanceService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeDailyAttendanceService.class);
            Integer count = employeeDailyAttendanceService.getListOfLeavesBetweenDates(employee.getId(), payrollRun.getStartDate(), payrollRun.getEndDate(), leaveType.getId());
            log.debug("Annual Leave By Employee Id :{} :{} ",employee.getId(),count);  
            return count;
		}
		catch(Exception e)
		{
			log.error("Error inside @class CalculationUtils @method calculateAnnualLeaveDays :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}
	
	private boolean isHoliday(Date date, List<Holiday> holidays) {
             LocalDate localDate = convertDateToLocalDate(date);
		for (Holiday holiday : holidays) {

			LocalDate dateAsLocalDate = holiday.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (dateAsLocalDate.equals(localDate)) {
				return true;
			}
		}
		return false;
	}
	
	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	private Double[] processOvertimeLogs(List<OvertimeLogs> overtimeLogsList ,List<Holiday> holidayList)
	{
		try
		{
			 Double overtimeLogsHoursSum = 0.0;
			    Double overtimeHourOnHoliday = 0.0;	
			OvertimeLogsService overtimeLogsService = ApplicationContextProvider.getApplicationContext()
					.getBean(OvertimeLogsService.class);
		//	log.debug("Inside @method processOvertimeLogs OvertimeHourTotalSum :{} overtimeHourOnHoliDay ",overtimeHourTotalSum,overtimeHourOnHoliday );
			for (OvertimeLogs overtimeLogs : overtimeLogsList) {
                Date date = overtimeLogs.getOvertimeDate();
				if(isHoliday(date,holidayList))
				{
					overtimeHourOnHoliday += overtimeLogs.getOvertimeHours();
		              overtimeLogs.setWorkflowStage(PRConstant.PAID_SM);
		              log.debug(" Overtime Id while Iterating :{} ",overtimeLogs.getId());
		      		overtimeLogs = overtimeLogsService.update(overtimeLogs);
		              log.debug(" Overtime Id while Iterating :{} ",overtimeLogs.getId());
				}
				
				else
				{
					overtimeLogsHoursSum += overtimeLogs.getOvertimeHours();
		              overtimeLogs.setWorkflowStage(PRConstant.PAID_SM);
		              log.debug(" Overtime Id while Iterating :{} ",overtimeLogs.getId());
		      		overtimeLogs = overtimeLogsService.update(overtimeLogs);
		              log.debug(" Overtime Id while Iterating :{} ",overtimeLogs.getId());
		        }
			}
			return new Double[] { overtimeLogsHoursSum, overtimeHourOnHoliday };

		}
		catch(Exception e)
		{
			log.error("Error inside @method processOvertimeLogs :{} :{}",e.getMessage(),Utils.getStackTrace(e));
            throw new BusinessException();
		}
	}

  }
  


