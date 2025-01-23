package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;

/**
 * 
 * DepartmentService interface is a service layer interface which handles all
 * the business logic related to Department model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Department
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface DepartmentService extends GenericService<Integer, Department> {

	



	Department findByName(String departmentName);

	List<DepartmentDetailsWrapper> getAllChildDepartments(String name);

	/**
	 * @param departmentWithId
	 * @return
	 */
	DepartmentDetailsWrapper setDepartmentHeirarchyData(Department departmentWithId);

	/**
	 * 
	 */
	DepartmentDetailsWrapper getDepartmentWiseDetails(List<Integer> departmentIds);

	DepartmentDetailsWrapper setDepartmentHeirarchyDataWithEmployee(Department department, Employee employee);
	Integer sendNotificationToSpecificDepartment();

}
