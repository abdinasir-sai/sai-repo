package com.nouros.payrollmanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.HrmsSystemConfig;

/**

HrmsSystemConfigService interface is a service layer interface which handles all the business logic related to HrmsSystemConfig model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves HrmsSystemConfig

@version 1.0

@since 2022-07-01
*/
public interface HrmsSystemConfigService extends GenericService<Integer, HrmsSystemConfig> {

/**

This method is used to retrieve audit history for an HrmsSystemConfig identified by id.
@param id The id of the HrmsSystemConfig whose audit history is to be retrieved.
@param limit The maximum number of records to retrieve.
@param skip The number of records to skip before retrieving.
@return A string representation of the audit history.
*/
	String auditHistory(int id, Integer limit, Integer skip);
	
	/**

This method is used to import HrmsSystemConfig data from an excel file.
@param excelFile The excel file containing HrmsSystemConfig data.
@return A string indicating the status of the import operation.
@throws IOException If there is an error reading the file.
@throws InstantiationException If there is an error creating an instance of the class.
@throws ClassNotFoundException If the class specified in the file is not found.
*/
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	 
	 /**

This method is used to export HrmsSystemConfig data to an excel file.
@param hrmsSystemConfig The list of hrmsSystemConfig to be exported.
@return A byte array containing the excel file.
@throws IOException If there is an error writing to the file.
*/
    byte[] export(List<HrmsSystemConfig> hrmsSystemConfig) throws IOException;
    
	/**

This method is used to soft delete an HrmsSystemConfig identified by id.
@param id The id of the HrmsSystemConfig to be soft deleted.
*/
	void softDelete(int id);
	void softBulkDelete(List<Integer> list);
	String getValue(String key);

	Map<String, String>  getHrmsKeyValue();
	
   
   
}
