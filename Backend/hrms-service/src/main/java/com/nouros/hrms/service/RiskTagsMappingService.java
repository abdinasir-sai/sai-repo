package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.RiskTagsMapping;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * RiskTagsMappingService interface is a service layer interface which handles
 * all the business logic related to RiskTagsMapping model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves RiskTagsMapping
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface RiskTagsMappingService extends GenericService<Integer,RiskTagsMapping> {

	/**
	 * 
	 * This method is used to soft delete an RiskTagsMapping identified by id.
	 * 
	 * @param id The id of the RiskTagsMapping to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

}
