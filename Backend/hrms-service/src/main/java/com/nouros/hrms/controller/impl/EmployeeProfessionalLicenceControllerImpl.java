/* (C)2024 */
package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeProfessionalLicenceController;
import com.nouros.hrms.model.EmployeeProfessionalLicence;
import com.nouros.hrms.service.EmployeeProfessionalLicenceService;

@Primary
@RestController
@RequestMapping("/EmployeeProfessionalLicence")

public class EmployeeProfessionalLicenceControllerImpl implements EmployeeProfessionalLicenceController {

	@Autowired
	private EmployeeProfessionalLicenceService employeeProfessionalLicenceService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public EmployeeProfessionalLicence create(EmployeeProfessionalLicence employeeProfessionalLicence) {
		return employeeProfessionalLicenceService.create(employeeProfessionalLicence);
	}

	@Override
	public Long count(String filter) {
		return employeeProfessionalLicenceService.count(filter);
	}

	@Override
	public List<EmployeeProfessionalLicence> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return employeeProfessionalLicenceService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public EmployeeProfessionalLicence update(EmployeeProfessionalLicence employeeProfessionalLicence) {
		return employeeProfessionalLicenceService.update(employeeProfessionalLicence);
	}

	@Override
	public EmployeeProfessionalLicence findById(Integer id) {
		return employeeProfessionalLicenceService.findById(id);
	}

	@Override
	public List<EmployeeProfessionalLicence> findAllById(List<Integer> id) {
		return employeeProfessionalLicenceService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		employeeProfessionalLicenceService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		employeeProfessionalLicenceService.softBulkDelete(list);
	}

}
