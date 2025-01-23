package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.service.generic.GenericService;

public interface EmployeeChildrenService extends GenericService<Integer, EmployeeChildren>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public EmployeeChildren create(EmployeeChildren employeeChildren);

}
