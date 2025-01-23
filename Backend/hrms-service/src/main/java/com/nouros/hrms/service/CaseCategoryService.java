package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.CaseCategory;
import com.nouros.hrms.service.generic.GenericService;

/**

CaseCategoryService interface is a service layer interface which handles all the business logic related to CaseCategory model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext CaseCategory

@version 1.0

@since 2022-07-01
*/
public interface CaseCategoryService extends GenericService<Integer, CaseCategory> {


	
	/**

This method is used to soft delete a list of CaseCategory identified by their ids.
@param list The list of ids of the CaseCategory to be soft deleted.
*/
	void bulkDelete(List<Integer> list);
	
   
   
}
