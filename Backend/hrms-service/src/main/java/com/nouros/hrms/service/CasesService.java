package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Cases;
import com.nouros.hrms.service.generic.GenericService;

/**

CasesService interface is a service layer interface which handles all the business logic related to Cases model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext Cases

@version 1.0

@since 2022-07-01
*/
public interface CasesService extends GenericService<Integer, Cases> {


	
	/**

This method is used to soft delete a list of Cases identified by their ids.
@param list The list of ids of the Cases to be soft deleted.
*/
	void bulkDelete(List<Integer> list);
	
   
   
}
