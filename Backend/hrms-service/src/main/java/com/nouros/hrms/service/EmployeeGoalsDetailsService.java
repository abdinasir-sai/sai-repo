package com.nouros.hrms.service;

 
import java.util.List;

import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.GoalWrapper;

/**

EmployeeGoalsMappingService interface is a service layer interface which handles all the business logic related to EmployeeGoalsMapping model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext EmployeeGoalsMapping

@version 1.0

@since 2022-07-01
*/
public interface EmployeeGoalsDetailsService extends GenericService<Integer,EmployeeGoalsDetails> {


	public List<EmployeeGoalsDetails> createEmployeeGoals(GoalWrapper goalWrapper);
	public List<EmployeeGoalsDetails> getEmployeeGoalsDetailsByEmployeeReviewId(Integer employeeReviewId);
	public  String deleteEmployeeGoalDetailById(Integer id);
	public String updateEmployeeGoalsDetails(List<EmployeeGoalsDetails> employeeGoalsDetailsList);
}
