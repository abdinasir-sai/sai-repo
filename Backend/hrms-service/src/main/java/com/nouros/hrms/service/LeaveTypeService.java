package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.service.generic.GenericService;

/**

LeaveTypeService interface is a service layer interface which handles all the business logic related to LeaveType model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext LeaveType

@version 1.0

@since 2022-07-01
*/
public interface LeaveTypeService extends GenericService<Integer,LeaveType> {



	List<LeaveType> findByDepartmentNameAndDesignationNameAndLocationName(String departmentName, String designationName,
			String locationName);

	LeaveType findByName(String name);

	List<LeaveType> findAll();
	
   
   
}
