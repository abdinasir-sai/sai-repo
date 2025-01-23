package com.nouros.payrollmanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.wrapper.PayrollRequestWrapper;

/**

EmployeeSalaryStructureService interface is a service layer interface which handles all the business logic related to EmployeeSalaryStructure model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves EmployeeSalaryStructure

@version 1.0

@since 2022-07-01
*/
public interface EmployeeSalaryStructureService extends GenericService<Integer, EmployeeSalaryStructure> {

/**

This method is used to retrieve audit history for an EmployeeSalaryStructure identified by id.
@param id The id of the EmployeeSalaryStructure whose audit history is to be retrieved.
@param limit The maximum number of records to retrieve.
@param skip The number of records to skip before retrieving.
@return A string representation of the audit history.
*/
	String auditHistory(int id, Integer limit, Integer skip);
	
	/**

This method is used to import EmployeeSalaryStructure data from an excel file.
@param excelFile The excel file containing EmployeeSalaryStructure data.
@return A string indicating the status of the import operation.
@throws IOException If there is an error reading the file.
@throws InstantiationException If there is an error creating an instance of the class.
@throws ClassNotFoundException If the class specified in the file is not found.
*/
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	 
	 /**

This method is used to export EmployeeSalaryStructure data to an excel file.
@param employeeSalaryStructure The list of employeeSalaryStructure to be exported.
@return A byte array containing the excel file.
@throws IOException If there is an error writing to the file.
*/
    byte[] export(List<EmployeeSalaryStructure> employeeSalaryStructure) throws IOException;
    
	/**

This method is used to soft delete an EmployeeSalaryStructure identified by id.
@param id The id of the EmployeeSalaryStructure to be soft deleted.
*/
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);

	PayrollRun executePayrollRun(PayrollRequestWrapper payrollRequestWrapper);

	EmployeeSalaryStructure getEmployeeMappedSalaryStructure(Integer employeeId);

	Object verifyOneTimeComponentCalculated(Integer employeeId);

	List<EmployeeSalaryStructure> getEmployeeSalaryStructureByUserId();
	
	Integer getWorkingDaysOfEmployee(Date fromDate, Date toDate, Integer employeeId);
   
   List<EmployeeSalaryStructure> findByEmployeePk(Integer employeeId);
   
   public List<EmployeeSalaryStructure> importEmployeeSalaryStructureData(MultipartFile excelFile);

}
