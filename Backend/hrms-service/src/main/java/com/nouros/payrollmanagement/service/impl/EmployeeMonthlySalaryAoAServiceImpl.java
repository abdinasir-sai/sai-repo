package com.nouros.payrollmanagement.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.repository.EmployeeMonthlySalaryJpaRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryAoAService;


@Service
public class EmployeeMonthlySalaryAoAServiceImpl implements EmployeeMonthlySalaryAoAService {

	 private static final Logger log = LogManager.getLogger(EmployeeMonthlySalaryAoAServiceImpl.class);

	@Autowired
	private UserRest userRest;

	@Autowired
	CustomerInfo customerInfo;
	

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}
	
	@Override
	public String getEmployeeEmployeeMonthlySalary(Integer employeeId) {
		log.info("Inside class RunDetailsServiceAoAImpl method getEmployeeRundetails employeeId :{} ",employeeId);
		JSONArray recordsArray = new JSONArray();

		try {
			
		if(employeeId==null) {
			User contextUser = getUserContext();
			log.debug("Inside class RunDetailsServiceAoAImpl  contextUser user Id is : {}", contextUser.getUserid());
			Integer userId = contextUser.getUserid();
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
			
			Employee employee = employeeService.getEmployeeByUserId(userId);
	    if(employee!=null){
			employeeId = employee.getId();
			log.debug("Inside class RunDetailsServiceAoAImpl  userContextemployeeId: {}", employeeId);		
	    }	
		}
		
		EmployeeMonthlySalaryJpaRepository employeeMonthlySalaryJpaRepository = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryJpaRepository.class);
			
		
		List<EmployeeMonthlySalary> payHistoryRecords =employeeMonthlySalaryJpaRepository.getPayHistoryRecord(employeeId);
				// runDetailsRepository.getPayHistoryRecord(employeeId);

	     for (EmployeeMonthlySalary record : payHistoryRecords) {
	         JSONObject recordJson = new JSONObject();
	         recordJson.put("id", record.getId());
	         
	         PayrollRun payrollRun= record.getPayrollRun();
	         if(payrollRun!=null) {
	        	 Date endDate =payrollRun.getEndDate();
	        	 recordJson.put("date", endDate); // Assuming the date is in a format that can be converted to string
	             recordsArray.put(recordJson);
	         }
	    
	     }
	     log.debug("Generated JSON: {}", recordsArray);
	     if(recordsArray!=null)
	     log.debug("Generated JSON: {}", recordsArray.toString());
	     return recordsArray.toString();
		}catch(Exception ex) {
			
			log.error("inside RunDetailsServiceAoAImpl @method getEmployeeRundetails error :{} ", Utils.getStackTrace(ex), ex);
		}
		return recordsArray.toString();
	}
}
