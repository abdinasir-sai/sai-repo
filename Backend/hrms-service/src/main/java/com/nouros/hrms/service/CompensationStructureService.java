package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * CompensationStructureService interface is a service layer interface which
 * handles all the business logic related to CompensationStructure model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext CompensationStructure
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface CompensationStructureService extends GenericService<Integer, CompensationStructure> {

	


	List<CompensationStructure> findByDepartmentIdAndTitle(Integer departmentId, String title);

	CompensationStructure findByTitle(String title);

}
