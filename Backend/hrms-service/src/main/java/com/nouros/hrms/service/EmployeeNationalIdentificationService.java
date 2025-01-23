package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * EmployeeNationalIdentificationService interface is a service layer interface
 * which handles all the business logic related to
 * EmployeeNationalIdentification model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves EmployeeNationalIdentification
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeNationalIdentificationService extends GenericService<Integer,EmployeeNationalIdentification> {

	/**
	 * 
	 * This method is used to soft delete an EmployeeNationalIdentification
	 * identified by id.
	 * 
	 * @param id The id of the EmployeeNationalIdentification to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	EmployeeNationalIdentification findNationalIdentificationDetailsOfEmployee(Integer id);

	List<EmployeeNationalIdentification> getSelfEmployeeNationalIdentification(Integer id, Integer userId);

	List<EmployeeNationalIdentification> updateEmployeeNationalIdentification(
			List<EmployeeNationalIdentification> employeeNationalIdentificationList);

	List<EmployeeNationalIdentification> findNationalIdentificationDetailsList(Integer id);
	
	EmployeeNationalIdentification getEmployeeNationalIdentificationByEmployeeId(Integer employeeId);

}
