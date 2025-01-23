package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.BusinessPlan;
import com.nouros.hrms.service.generic.GenericService;

/**

BusinessPlanService interface is a service layer interface which handles all the business logic related to BusinessPlan model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext BusinessPlan

@version 1.0

@since 2022-07-01
*/
public interface BusinessPlanService extends GenericService<Integer, BusinessPlan> {

	/**

This method is used to soft delete a list of BusinessPlan identified by their ids.
@param list The list of ids of the BusinessPlan to be soft deleted.
*/
	void bulkDelete(List<Integer> list);

	public BusinessPlan findByTitleAndMonth(String title, String planDate);
	
   
   
}
