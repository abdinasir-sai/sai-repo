package com.nouros.payrollmanagement.service;

import java.io.IOException;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.wrapper.PayrollRunWrapper;

/**

PayrollRunService interface is a service layer interface which handles all the business logic related to PayrollRun model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves PayrollRun

@version 1.0

@since 2022-07-01
*/
public interface PayrollRunService extends GenericService<Integer, PayrollRun> {

/**

This method is used to retrieve audit history for an PayrollRun identified by id.
@param id The id of the PayrollRun whose audit history is to be retrieved.
@param limit The maximum number of records to retrieve.
@param skip The number of records to skip before retrieving.
@return A string representation of the audit history.
*/
	String auditHistory(int id, Integer limit, Integer skip);
	
	/**

This method is used to import PayrollRun data from an excel file.
@param excelFile The excel file containing PayrollRun data.
@return A string indicating the status of the import operation.
@throws IOException If there is an error reading the file.
@throws InstantiationException If there is an error creating an instance of the class.
@throws ClassNotFoundException If the class specified in the file is not found.
*/
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	 
	 /**

This method is used to export PayrollRun data to an excel file.
@param payrollRun The list of payrollRun to be exported.
@return A byte array containing the excel file.
@throws IOException If there is an error writing to the file.
*/
    byte[] export(List<PayrollRun> payrollRun) throws IOException;
    
	/**

This method is used to soft delete an PayrollRun identified by id.
@param id The id of the PayrollRun to be soft deleted.
*/
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	PayrollRun createPayroll(PayrollRun payrollRun );
	PayrollRun update(PayrollRun payrollRun);
	PayrollRun identifyVarianceReasons(Integer payrollRunId);

	PayrollRun reExecutePayroll(Integer month, Integer year);

	Object getPayrollEmployeeMonthlySalary();
	 PayrollRun getPayrollByProcessInstanceId(String processInstanceId);
	 
	 PayrollRun getPayrollById(Integer payRollId);

	PayrollRun updatePayrollRunWorkflowStage(PayrollRunWrapper payrollRunWrapper);
}
