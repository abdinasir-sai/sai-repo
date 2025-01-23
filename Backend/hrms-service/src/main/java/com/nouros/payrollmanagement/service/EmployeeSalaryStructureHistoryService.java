package com.nouros.payrollmanagement.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructureHistory;

/**

EmployeeSalaryStructureHistoryService interface is a service layer interface which handles all the business logic related to EmployeeSalaryStructureHistory model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves EmployeeSalaryStructureHistory

@version 1.0

@since 2022-07-01
*/
public interface EmployeeSalaryStructureHistoryService extends GenericService<Integer, EmployeeSalaryStructureHistory> {

/**

This method is used to retrieve audit history for an EmployeeSalaryStructureHistory identified by id.
@param id The id of the EmployeeSalaryStructureHistory whose audit history is to be retrieved.
@param limit The maximum number of records to retrieve.
@param skip The number of records to skip before retrieving.
@return A string representation of the audit history.
*/
	String auditHistory(int id, Integer limit, Integer skip);
	
	/**

This method is used to import EmployeeSalaryStructureHistory data from an excel file.
@param excelFile The excel file containing EmployeeSalaryStructureHistory data.
@return A string indicating the status of the import operation.
@throws IOException If there is an error reading the file.
@throws InstantiationException If there is an error creating an instance of the class.
@throws ClassNotFoundException If the class specified in the file is not found.
*/
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	 
	 /**

This method is used to export EmployeeSalaryStructureHistory data to an excel file.
@param employeeSalaryStructureHistory The list of employeeSalaryStructureHistory to be exported.
@return A byte array containing the excel file.
@throws IOException If there is an error writing to the file.
*/
    byte[] export(List<EmployeeSalaryStructureHistory> employeeSalaryStructureHistory) throws IOException;
    
	/**

This method is used to soft delete an EmployeeSalaryStructureHistory identified by id.
@param id The id of the EmployeeSalaryStructureHistory to be soft deleted.
*/
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);
	EmployeeSalaryStructureHistory getHistoryInGivenDate(Date historyDate,Integer employeeId);
	
   
   
}
