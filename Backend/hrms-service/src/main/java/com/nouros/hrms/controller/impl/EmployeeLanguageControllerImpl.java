/* (C)2024 */
package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeLanguageController;
import com.nouros.hrms.model.EmployeeLanguage;
import com.nouros.hrms.service.EmployeeLanguageService;


@Primary
@RestController
@RequestMapping("/EmployeeLanguage")
public class EmployeeLanguageControllerImpl implements EmployeeLanguageController {

	@Autowired
	private EmployeeLanguageService employeeLanguageService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public EmployeeLanguage create(EmployeeLanguage employeeLanguage) {
		return employeeLanguageService.create(employeeLanguage);
	}

	@Override
	public Long count(String filter) {
		return employeeLanguageService.count(filter);
	}

	@Override
	public List<EmployeeLanguage> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return employeeLanguageService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public EmployeeLanguage update(EmployeeLanguage employeeLanguage) {
		return employeeLanguageService.update(employeeLanguage);
	}

	@Override
	public EmployeeLanguage findById(Integer id) {
		return employeeLanguageService.findById(id);
	}

	@Override
	public List<EmployeeLanguage> findAllById(List<Integer> id) {
		return employeeLanguageService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		employeeLanguageService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		employeeLanguageService.softBulkDelete(list);
	}

}
