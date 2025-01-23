package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeEmergencyContactController;
import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.service.EmployeeEmergencyContactService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/EmployeeEmergencyContact")
public class EmployeeEmergencyContactControllerImpl implements EmployeeEmergencyContactController{

	private static final Logger log = LogManager.getLogger(EmployeeEmergencyContactControllerImpl.class);

	@Autowired
	  private EmployeeEmergencyContactService employeeEmergencyContactService;

	  @Override
	  @TriggerBPMN(entityName = "EmployeeEmergencyContact", appName = "HRMS_APP_NAME")
	  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE DEPENDENT DETAILS")
	  public EmployeeEmergencyContact create(EmployeeEmergencyContact employeeEmergencyContact) {
		  log.info("inside @class EmployeeEmergencyContactControllerImpl @method create");
	    return employeeEmergencyContactService.create(employeeEmergencyContact);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return employeeEmergencyContactService.count(filter);
	  }

	  @Override
	  public List<EmployeeEmergencyContact> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return employeeEmergencyContactService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE EMERGENCY CONTACT")
	  public EmployeeEmergencyContact update(EmployeeEmergencyContact employeeEmergencyContact) {
	    return employeeEmergencyContactService.update(employeeEmergencyContact);
	  }

	  @Override
	  public EmployeeEmergencyContact findById(Integer id) {
	    return employeeEmergencyContactService.findById(id);
	  }

	  @Override
	  public List<EmployeeEmergencyContact> findAllById(List<Integer> id) {
	    return employeeEmergencyContactService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE EMERGENCY CONTACT BY ID")
	  public void deleteById(Integer id) {
		  employeeEmergencyContactService.deleteById(id);
	  }


	@Override
	public EmployeeEmergencyContact getSelfEmployeeEmergencyContact(@Valid Integer id, @Valid Integer userId) {
		return employeeEmergencyContactService.getSelfEmployeeEmergencyContact(id,userId);
	}


	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE EMERGENCY CONTACT")
	public EmployeeEmergencyContact updateEmployeeEmergencyContact(EmployeeEmergencyContact employeeEmergencyContact) {
		return employeeEmergencyContactService.updateEmployeeEmergencyContact(employeeEmergencyContact);
	}
	 

	 
	 
}
