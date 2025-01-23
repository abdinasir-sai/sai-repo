package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeExpenses;
import com.nouros.hrms.repository.EmployeeExpensesRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeExpensesService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class EmployeeExpensesServiceImpl extends AbstractService<Integer,EmployeeExpenses> implements EmployeeExpensesService {

	public EmployeeExpensesServiceImpl(GenericRepository<EmployeeExpenses> repository) {
		super(repository, EmployeeExpenses.class);
	}
 
	private static final Logger log = LogManager.getLogger(EmployeeExpensesServiceImpl.class);
	
	@Autowired
	private EmployeeExpensesRepository employeeExpensesRepository;

	@Override
	public EmployeeExpenses create(EmployeeExpenses employeeExpenses) {
		log.info("inside @class EmployeeExpensesServiceImpl @method create");
		return employeeExpensesRepository.save(employeeExpenses);
	}

}
