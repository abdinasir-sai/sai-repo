package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Breakdetails;
import com.nouros.hrms.service.generic.GenericService;

/**

BreakdetailsService interface is a service layer interface which handles all the business logic related to Breakdetails model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext Breakdetails

@version 1.0

@since 2022-07-01
*/
public interface BreakdetailsService extends GenericService<Integer, Breakdetails> {

	/**

This method is used to soft delete a list of Breakdetails identified by their ids.
@param list The list of ids of the Breakdetails to be soft deleted.
*/
	void bulkDelete(List<Integer> list);
	
   
   
}
