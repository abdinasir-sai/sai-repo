package com.nouros.hrms.service;

public interface CheckService {
 
	Boolean checkBODInRunDetail(String processInstanceId);
	
	public Integer getValueForDesignation(String processInstanceId,Integer userId);
	
	public String getJobLevelForEmployee();
	 Integer getEmploymentTypeCheck(String employeeNationalIdentificationType);
	
}
