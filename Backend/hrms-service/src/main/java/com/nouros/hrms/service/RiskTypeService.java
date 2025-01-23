package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.RiskType;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * RiskTypeService interface is a service layer interface which handles all the
 * business logic related to RiskType model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves RiskType
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface RiskTypeService extends GenericService<Integer,RiskType> {



	/**
	 * 
	 * This method is used to soft delete an RiskType identified by id.
	 * 
	 * @param id The id of the RiskType to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

}
