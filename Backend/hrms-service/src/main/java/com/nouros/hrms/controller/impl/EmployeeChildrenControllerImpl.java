package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.EmployeeChildrenController;
import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.service.EmployeeChildrenService;

@Primary
@RestController
@RequestMapping("/EmployeeChildren")
public class EmployeeChildrenControllerImpl implements EmployeeChildrenController{
	
	 private static final Logger log = LogManager.getLogger(EmployeeChildrenControllerImpl.class);

	  @Autowired
	  private EmployeeChildrenService employeeChildrenService;

	  @Override
	  @TriggerBPMN(entityName = "EmployeeChildren", appName = "HRMS_APP_NAME")
	  public EmployeeChildren create(EmployeeChildren employeeChildren) {
		  log.info("inside @class EmployeeChildrenControllerImpl @method create");
	    return employeeChildrenService.create(employeeChildren);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return employeeChildrenService.count(filter);
	  }

	  @Override
	  public List<EmployeeChildren> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return employeeChildrenService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public EmployeeChildren update(EmployeeChildren employeeChildren) {
	    return employeeChildrenService.update(employeeChildren);
	  }

	  @Override
	  public EmployeeChildren findById(Integer id) {
	    return employeeChildrenService.findById(id);
	  }

	  @Override
	  public List<EmployeeChildren> findAllById(List<Integer> id) {
	    return employeeChildrenService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  employeeChildrenService.deleteById(id);
	  }

}
