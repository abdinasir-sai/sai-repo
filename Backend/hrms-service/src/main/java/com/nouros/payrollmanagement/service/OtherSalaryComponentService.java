package com.nouros.payrollmanagement.service;



import java.util.List;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryComponent.Type;

public interface OtherSalaryComponentService extends GenericService<Integer, OtherSalaryComponent>{

	List<OtherSalaryComponent> findByEmployeeIdAndTypeAndDeleted(Integer employeeId,Type type,Boolean deleted);
	
	OtherSalaryComponent create(OtherSalaryComponent employeeUnstructuredSalary);
	
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);
}
