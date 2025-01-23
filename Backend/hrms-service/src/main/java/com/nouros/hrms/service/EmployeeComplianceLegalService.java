package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * EmployeeComplianceLegalService interface is a service layer interface which
 * handles all the business logic related to EmployeeComplianceLegal model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves EmployeeComplianceLegal
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeComplianceLegalService extends GenericService<Integer, EmployeeComplianceLegal> {

	
	/**
	 * 
	 * This method is used to soft delete an EmployeeComplianceLegal identified by
	 * id.
	 * 
	 * @param id The id of the EmployeeComplianceLegal to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	EmployeeComplianceLegal findComplianceLegalDetailsOfEmployee(Integer id);

	EmployeeComplianceLegal getSelfEmployeeComplianceLegal(Integer id, Integer userId);

	EmployeeComplianceLegal updateEmployeeComplianceLegal(EmployeeComplianceLegal employeeComplianceLegal);
	
}
