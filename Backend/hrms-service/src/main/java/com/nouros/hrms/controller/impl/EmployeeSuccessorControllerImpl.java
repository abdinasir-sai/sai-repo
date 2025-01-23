package com.nouros.hrms.controller.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 
import com.nouros.hrms.controller.EmployeeSuccessorController;
import com.nouros.hrms.model.EmployeeSuccessor;
import com.nouros.hrms.service.EmployeeSuccessorService;
import com.nouros.hrms.wrapper.EmployeeSuccessorWrapper;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/EmployeeSuccessor")
public class EmployeeSuccessorControllerImpl implements EmployeeSuccessorController {

	private static final Logger log = LogManager.getLogger(EmployeeSuccessorControllerImpl.class);
	
	@Autowired
	private EmployeeSuccessorService employeeSuccessorService;
	
	@Override
	public EmployeeSuccessor create(@Valid EmployeeSuccessor employeeSuccessor) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.create(employeeSuccessor);
	}

	@Override
	public Long count(String filter) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.count(filter);
	}

	@Override
	public List<EmployeeSuccessor> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public EmployeeSuccessor update(@Valid EmployeeSuccessor employeeSuccessor) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.update(employeeSuccessor);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		// TODO Auto-generated method stub
		employeeSuccessorService.deleteById(id);
	}

	@Override
	public EmployeeSuccessor findById(@Valid Integer id) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.findById(id);
	}

	@Override
	public List<EmployeeSuccessor> findAllById(@Valid List<Integer> id) {
		// TODO Auto-generated method stub
		return employeeSuccessorService.findAllById(id);
	}
	
	@Override
	 public List<EmployeeSuccessorWrapper> getPotentialCandidateList(Integer designationId )
	  {
		  return employeeSuccessorService.getPotentialSuccessorList(designationId);
	  }

}
