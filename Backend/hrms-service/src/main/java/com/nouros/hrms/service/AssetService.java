package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Asset;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * AssetService interface is a service layer interface which handles all the
 * business logic related to Asset model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Asset
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface AssetService extends GenericService<Integer, Asset> {

	/**
	 * 
	 * This method is used to soft delete an Asset identified by id.
	 * 
	 * @param id The id of the Asset to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

}
