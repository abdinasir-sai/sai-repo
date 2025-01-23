package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * EmployeeDependentDetailsService interface is a service layer interface which
 * handles all the business logic related to EmployeeDependentDetails model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext EmployeeDependentDetails
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeDependentDetailsService extends GenericService<Integer, EmployeeDependentDetails> {

	List<EmployeeDependentDetails> findDependentDetailsOfEmployee(Integer id);

	List<EmployeeDependentDetails> getSelfEmployeeDependentDetails( Integer id, Integer userId);

	List<EmployeeDependentDetails> updateSelfEmployeeDependentDetails(List<EmployeeDependentDetails> employeeDependentDetailsList);

}
