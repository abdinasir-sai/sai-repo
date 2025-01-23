package com.nouros.hrms.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.CheckController;
import com.nouros.hrms.service.CheckService;

@RestController
@RequestMapping("/Check")
public class CheckControllerImpl implements CheckController {

	@Autowired
	CheckService checkService;
	
	@Override
	public Boolean checkBoardOfDirectors(String processInstanceId) {
		return checkService.checkBODInRunDetail(processInstanceId);
	}

	@Override
	public Integer getValueForDesignation(String processInstanceId,Integer userId) {
	 
		return checkService.getValueForDesignation(processInstanceId,userId);
	}

	@Override
	public String getJobLevelForEmployee() {
		
		return checkService.getJobLevelForEmployee();
	}

	@Override
	public Integer getEmploymentTypeCheck(String employeeNationalIdentificationType)
	{
		return checkService.getEmploymentTypeCheck(employeeNationalIdentificationType);
	}
}
