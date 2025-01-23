package com.nouros.payrollmanagement.service;

import java.util.List;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;

public interface OtherSalaryPayrollMappingService extends GenericService<Integer, OtherSalaryPayrollMapping>{

	
	
	List<OtherSalaryComponent> getComponentsByPayrollId(Integer payrollRunId ,String type, Boolean deleted);
	
	
	OtherSalaryPayrollMapping getMappingAmount(Integer othersalaryComponentId , Boolean deleted);
}

