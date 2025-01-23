package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.AttendanceRegularization;
import com.nouros.hrms.service.generic.GenericService;

/**

AttendanceRegularizationService interface is a service layer interface which handles all the business logic related to AttendanceRegularization model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext AttendanceRegularization

@version 1.0

@since 2022-07-01
*/
public interface AttendanceRegularizationService extends GenericService<Integer, AttendanceRegularization> {

	
	/**

This method is used to soft delete a list of AttendanceRegularization identified by their ids.
@param list The list of ids of the AttendanceRegularization to be soft deleted.
*/
	void bulkDelete(List<Integer> list);
	
   
   
}
