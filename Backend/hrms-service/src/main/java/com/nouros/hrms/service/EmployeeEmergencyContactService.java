package com.nouros.hrms.service;

import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.service.generic.GenericService;

import jakarta.validation.Valid;

public interface EmployeeEmergencyContactService extends GenericService<Integer, EmployeeEmergencyContact>{
	
	
	    EmployeeEmergencyContact findEmergencyContactOfEmployee(Integer id);

	    EmployeeEmergencyContact getSelfEmployeeEmergencyContact(@Valid Integer id, @Valid Integer userId);

		EmployeeEmergencyContact updateEmployeeEmergencyContact(EmployeeEmergencyContact employeeEmergencyContact);

		
}
