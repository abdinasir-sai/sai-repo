package com.nouros.payrollmanagement.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.wrapper.PayrollTotals;


/**

EmployeeMonthlySalaryService interface is a service layer interface which handles all the business logic related to EmployeeMonthlySalary model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves EmployeeMonthlySalary

@version 1.0

@since 2022-07-01
*/
public interface EmployeeMonthlySalaryService extends GenericService<Integer, EmployeeMonthlySalary> {

/**

This method is used to retrieve audit history for an EmployeeMonthlySalary identified by id.
@param id The id of the EmployeeMonthlySalary whose audit history is to be retrieved.
@param limit The maximum number of records to retrieve.
@param skip The number of records to skip before retrieving.
@return A string representation of the audit history.
*/
	String auditHistory(int id, Integer limit, Integer skip);
	
	/**

This method is used to import EmployeeMonthlySalary data from an excel file.
@param excelFile The excel file containing EmployeeMonthlySalary data.
@return A string indicating the status of the import operation.
@throws IOException If there is an error reading the file.
@throws InstantiationException If there is an error creating an instance of the class.
@throws ClassNotFoundException If the class specified in the file is not found.
*/
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	 
	 /**

This method is used to export EmployeeMonthlySalary data to an excel file.
@param employeeMonthlySalary The list of employeeMonthlySalary to be exported.
@return A byte array containing the excel file.
@throws IOException If there is an error writing to the file.
*/
    byte[] export(List<EmployeeMonthlySalary> employeeMonthlySalary) throws IOException;
    
   
      
	/**

This method is used to soft delete an EmployeeMonthlySalary identified by id.
@param id The id of the EmployeeMonthlySalary to be soft deleted.
*/
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);
	
	BigDecimal getRecentNetworthForEmployee(Integer employeeId);
	
	List<EmployeeMonthlySalary> getEmployeeMonthlySalaryByPayrollId(Integer payrollId);
	
	byte[] generateExcelReport(List<EmployeeMonthlySalary> employeeMonthlySalary) throws IOException;
	
	ResponseEntity<byte[]> generateExcelForUpload(Integer payrollId) throws IOException;
	
	
	Boolean checkForRelocationAllowance(Integer employeeId);
	
	Boolean checkForSignUpBonus(Integer employeeId);
	
	ResponseEntity<byte[]> generateExcel(Integer payrollId) throws IOException;
	void generateExcelForPayroll(Integer payrollId) throws IOException;
	String getEmployeeMonthlySalary(Integer employeeId);
	ResponseEntity<byte[]> createWpsTxtFile(String payrollRunProcessInstanceId);
	EmployeeMonthlySalary getRecentEmployeeMonthlySalary(Integer employeeId,Integer employeeMonthlySalaryId);
	String getTop3ReasonByPayrollid(Integer payrollId);
	
	void softBulkDeleteByPayrollId(List<Integer> list);

    ResponseEntity<byte[]> createGOSIReportFile(Integer payrollRunId);
    List<Object[]> getSumOfValue(Integer payrollId,String[] parts);
    List<Object[]> getSumOfValueOfAccural(Integer payrollId,String[] parts);

   public ResponseEntity<byte[]> createWpsTxtFileForOnBoarded(String payrollRunProcessInstanceId);
    
  public ResponseEntity<byte[]> downloadWpsFileForBoardedMember(String year , String month);
  public List<Object[]> getExpenseForBOD(Integer payrollId ,String employeeType);
  public  Object[] getAccuralsForBOD(Integer payrollId ,String employeeType);
   
  public PayrollTotals getPayrollSum(Integer payrollRunId);
  
  EmployeeMonthlySalary getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId(Integer employeeId,Integer payrollRunId);
}
