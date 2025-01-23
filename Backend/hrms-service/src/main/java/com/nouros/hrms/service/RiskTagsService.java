package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.RiskTags;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * RiskTagsService interface is a service layer interface which handles all the
 * business logic related to RiskTags model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves RiskTags
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface RiskTagsService extends GenericService<Integer,RiskTags> {

	/**
	 * 
	 * This method is used to soft delete an RiskTags identified by id.
	 * 
	 * @param id The id of the RiskTags to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

}
