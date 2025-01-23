package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.repository.EmployeeChildrenRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeChildrenService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class EmployeeChildrenServiceImpl extends AbstractService<Integer,EmployeeChildren> implements EmployeeChildrenService  {
	
	public EmployeeChildrenServiceImpl(GenericRepository<EmployeeChildren> repository) {
		super(repository, EmployeeChildren.class);
	}
	
	@Autowired
	private EmployeeChildrenRepository employeeChildrenRepository;

	private static final Logger log = LogManager.getLogger(EmployeeChildrenServiceImpl.class);

	@Override
	public EmployeeChildren create(EmployeeChildren employeeChildren) {
		log.info("inside @class EmployeeChildrenServiceImpl @method create");
		return employeeChildrenRepository.save(employeeChildren);
	}
	
	@Override
	public void softDelete(int id) {

		EmployeeChildren employeeChildren = super.findById(id);

		if (employeeChildren != null) {

			EmployeeChildren employeeChildren1 = employeeChildren;
			employeeChildrenRepository.save(employeeChildren1);

		}
	}
	
	
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

}
