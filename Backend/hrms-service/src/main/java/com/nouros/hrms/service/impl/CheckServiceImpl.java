package com.nouros.hrms.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.repository.BusinessTripRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.service.CheckService;
import com.nouros.hrms.service.EmployeeNationalIdentificationService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;

@Service
public class CheckServiceImpl implements CheckService {

	private static final Logger log = LogManager.getLogger(CheckServiceImpl.class);

	@Autowired
	IWorkorderController workorderController;

	@Autowired
	WorkflowActionsController workflowActionsController;
	
	@Autowired
	UserRest userRest;
	
	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	HrmsSystemConfigService hrmsSystemConfigService;
	
	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	@Override
	public Boolean checkBODInRunDetail(String processInstanceId) {

		log.info("Inside @class CheckServiceImpl @method checkBODInRunDetail ");
		try {
			log.debug("Inside @class CheckServiceImpl @method checkBODInRunDetail processId :{}", processInstanceId);
			PayrollRunService payRollService = ApplicationContextProvider.getApplicationContext()
					.getBean(PayrollRunService.class);
			PayrollRun payRollRun = payRollService.getPayrollByProcessInstanceId(processInstanceId);
			log.debug("Inside @class CheckServiceImpl @method checkBODInRunDetail PayrollId :{}", payRollRun.getId());
			EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeMonthlySalaryService.class);
			List<EmployeeMonthlySalary> employeeMonthlySalaryList = employeeMonthlySalaryService.getEmployeeMonthlySalaryByPayrollId(payRollRun.getId());
			log.debug("Inside @class CheckServiceImpl @method checkBODInRunDetail Size of EmployeeMonthlySalary :{}",
					employeeMonthlySalaryList.size());
			for (EmployeeMonthlySalary employeeMonthlySalary : employeeMonthlySalaryList) {
				Employee employee = employeeMonthlySalary.getEmployee();
				if (employee.getEmploymentType().equalsIgnoreCase(PRConstant.BOARD_OF_DIRECTORS)) {
					log.info("The Board of diretors is present ");
					employeeMonthlySalaryService.createWpsTxtFileForOnBoarded(payRollRun.getProcessInstanceId());
					return true;
				}
			}
			return false;

		} catch (Exception e) {

			log.error("Error inside @class CheckServiceImpl @method checkBODInRunDetail  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	@Override
	public Integer getValueForDesignation(String processInstanceId,Integer userId) {
		log.info("Inside @class CheckServiceImpl @method getValueForDesignation ");
		try {
			log.debug("Inside @class CheckServiceImpl @method getValueForDesignation processId :{}", processInstanceId);
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
					if (userId != null ) {
						Employee employee = employeeService.getEmployeeByCreator(userId);
						if (employee != null) {
							log.debug("The value of Employee is : {}", employee);
							Department department = employee.getDepartment();
							Designation designation = employee.getDesignation();
							if (designation != null && designation.getName() != null
									&& designation.getName().equalsIgnoreCase("CHRO")) {
								log.debug("The value of designation is :{}", designation.getName());
								return 1;
							} else if (department != null && department.getDepartmentLead() != null
									&& department.getDepartmentLead().equals(userId)) {
								log.debug("The value of Department Lead  is :{}", department.getDepartmentLead());
								return 2;
							} else {
								return 3;
							}
						} else {
							throw new BusinessException("Employee Not present");
						}

					} else {
						throw new BusinessException(" User Id not present ");
					}
 					} catch (Exception e) {
			log.error("Error inside @class CheckServiceImpl @method getValueForDesignation  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	@Override
	@Transactional
	public String getJobLevelForEmployee() {
       String jobLevel=null;
		User contextUser = getUserContext();
		log.debug("contextUser user Id is : {}", contextUser.getUserid());
		EmployeeRepository employeeRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeRepository.class);
		Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
		if(employee!=null) {
			log.debug("Inside getJobLevelForEmployee employee found with id:{}", employee.getId());
			Designation designation=employee.getDesignation();
			log.debug("Inside getJobLevelForEmployee designation:{}", designation.getId());
			if(designation!=null) {
				jobLevel=designation.getJobLevel();
				log.debug("Inside getJobLevelForEmployee jobLevel found is:{}",jobLevel);
				if(jobLevel!=null) {
			    if((jobLevel.equals("L1") || jobLevel.equals("L2") || jobLevel.equals("L3")) ) {
			    	return jobLevel;  }
				}
			}
		} else {
			log.error("Employee not found for context user id:{}",contextUser.getUserid());
			throw new BusinessException("Employee not found");
		       }	
		
		return "L4";
	}

	@Override
	public Integer getEmploymentTypeCheck(String employeeNationalIdentificationType)
	{
		try
		{
			log.info("Inside @class CheckServiceImpl @method getEmploymentTypeCheck employeeNationalIdentificationType :{}",employeeNationalIdentificationType);
//			EmployeeNationalIdentificationService employeeIdentificationNationalService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeNationalIdentificationService.class);
//		   EmployeeNationalIdentification employeeNationalIdentification = employeeIdentificationNationalService.getEmployeeNationalIdentificationByEmployeeId(employeeId);
//		   log.debug("Inside @method getEmploymentTypeCheck employeeNationalIdentification Id :{}",employeeNationalIdentification.getId());
		   String employmentTypeCheckList = hrmsSystemConfigService.getValue(PRConstant.EMPLOYMENT_TYPE_CHECK_LIST);
		 log.debug("Inside @getEmploymentTypeCheck value of employmentTypeList :{}",employmentTypeCheckList);
		 String[] employeeTypeList ;
		 if(employmentTypeCheckList==null)
		 {
			 log.info("Value from HRMS SYSTEM CONFIG  is NULL ");
		  String employeeTypeValueList = PRConstant.EMPLOYMENT_TYPE_CHECK_LIST_VALUE;	
			 employeeTypeList = employeeTypeValueList.split(",");

		 }
		 else
		 {
			 employeeTypeList = employmentTypeCheckList.split(",");
		 }
		 log.debug("Size of employeeTypeList :{} ",employeeTypeList.length);
		 Integer count = 1;
		 for(String type : employeeTypeList)
		   if(employeeNationalIdentificationType !=null  && employeeNationalIdentificationType.equalsIgnoreCase(type))
		   {
			   log.debug("Value of count :{} ",count);
			   return count;
		   }
		   else {
			   count++;
		   }
		 log.error("Not Matched with Any value of Type ");
		 return 0;
		}
		catch(Exception e)
		{
			log.error("Error inside @class CheckServiceImpl @method getEmploymentTypeCheck  :{} :{}", e.getMessage(),
				Utils.getStackTrace(e));
		throw new BusinessException();
			
		}
	}
	
}
