/* (C)2024 */
package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.FacilityManagementController;
import com.nouros.hrms.model.FacilityManagement;
import com.nouros.hrms.service.FacilityManagementService;

@Primary
@RestController
@RequestMapping("/FacilityManagement")
public class FacilityManagementControllerImpl implements FacilityManagementController {

	@Autowired
	private FacilityManagementService facilityManagementService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public FacilityManagement create(FacilityManagement facilityManagement) {
		return facilityManagementService.create(facilityManagement);
	}

	@Override
	public Long count(String filter) {
		return facilityManagementService.count(filter);
	}

	@Override
	public List<FacilityManagement> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return facilityManagementService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public FacilityManagement update(FacilityManagement facilityManagement) {
		return facilityManagementService.update(facilityManagement);
	}

	@Override
	public FacilityManagement findById(Integer id) {
		return facilityManagementService.findById(id);
	}

	@Override
	public List<FacilityManagement> findAllById(List<Integer> id) {
		return facilityManagementService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		facilityManagementService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		facilityManagementService.softBulkDelete(list);
	}

}
