/* (C)2024 */
package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.FacilityManagement;
import com.nouros.hrms.service.generic.GenericService;

public interface FacilityManagementService extends GenericService<Integer, FacilityManagement> {
	
	FacilityManagement create(FacilityManagement facilityManagement);

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
