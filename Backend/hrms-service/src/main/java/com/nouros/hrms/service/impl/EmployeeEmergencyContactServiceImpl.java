package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.repository.EmployeeEmergencyContactRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeEmergencyContactService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class EmployeeEmergencyContactServiceImpl extends AbstractService<Integer,EmployeeEmergencyContact> implements EmployeeEmergencyContactService {
 
	private static final Logger log = LogManager.getLogger(EmployeeEmergencyContactServiceImpl.class);

	public EmployeeEmergencyContactServiceImpl(GenericRepository<EmployeeEmergencyContact> repository) {
		super(repository, EmployeeEmergencyContact.class);
	}
	
	@Autowired
	private EmployeeEmergencyContactRepository employeeEmergencyContactRepository;

	
 


	@Override
	public EmployeeEmergencyContact create(EmployeeEmergencyContact employeeEmergencyContact) {
		
		employeeEmergencyContact.setEmergencyContactFullName(employeeEmergencyContact.getEmergencyContactFirstName() + " " + employeeEmergencyContact.getEmergencyContactLastName());
		return employeeEmergencyContactRepository.save(employeeEmergencyContact);
	}



	@Override
	public EmployeeEmergencyContact findEmergencyContactOfEmployee(Integer id) {
		if (id != null) {
	        log.info("Inside if to fetch Employee Emergency Contact by employee Id ");
	        return employeeEmergencyContactRepository.findEmergencyContactOfEmployee(id);
	    }
		return null;
	}

	@Override
	public EmployeeEmergencyContact getSelfEmployeeEmergencyContact(Integer id, Integer userId) {
		log.info("Inside Method getSelfEmployeeEmergencyContact");
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		Employee employee = employeeService.getEmployeeByIdOrUserId(id, userId);
		if (employee != null) {
			return findEmergencyContactOfEmployee(employee.getId());
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
	}

	@Override
	public EmployeeEmergencyContact updateEmployeeEmergencyContact(
			EmployeeEmergencyContact employeeEmergencyContact) {
		log.info("Inside Method updateEmployeeEmergencyContact");
		employeeEmergencyContact.setEmergencyContactFullName(employeeEmergencyContact.getEmergencyContactFirstName() + " " + employeeEmergencyContact.getEmergencyContactLastName());
		return  employeeEmergencyContactRepository.save(employeeEmergencyContact);
		}
	

}
