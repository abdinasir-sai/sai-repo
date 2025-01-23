package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.service.generic.GenericService;

/**

EmployeeCompetenciesDetailsService interface is a service layer interface which handles all the business logic related to EmployeeCompetenciesDetails model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext EmployeeCompetenciesDetails

@version 1.0

@since 2022-07-01
*/
public interface EmployeeCompetenciesDetailsService extends GenericService<Integer, EmployeeCompetenciesDetails> {

  void createCompetenciesForEmployee(Employee employee,EmployeeReview employeeReview);
	public List<EmployeeCompetenciesDetails> getAllEmployeeCompetencies(Integer employeeReviewId);   
   
}
