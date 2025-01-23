/* (C)2024 */
package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeClearanceCertificateController;
import com.nouros.hrms.model.EmployeeClearanceCertificate;
import com.nouros.hrms.service.EmployeeClearanceCertificateService;

@Primary
@RestController
@RequestMapping("/EmployeeClearanceCertificate")

public class EmployeeClearanceCertificateControllerImpl implements EmployeeClearanceCertificateController {

	@Autowired
	private EmployeeClearanceCertificateService employeeClearanceCertificateService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public EmployeeClearanceCertificate create(EmployeeClearanceCertificate employeeClearanceCertificate) {
		return employeeClearanceCertificateService.create(employeeClearanceCertificate);
	}

	@Override
	public Long count(String filter) {
		return employeeClearanceCertificateService.count(filter);
	}

	@Override
	public List<EmployeeClearanceCertificate> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return employeeClearanceCertificateService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public EmployeeClearanceCertificate update(EmployeeClearanceCertificate employeeClearanceCertificate) {
		return employeeClearanceCertificateService.update(employeeClearanceCertificate);
	}

	@Override
	public EmployeeClearanceCertificate findById(Integer id) {
		return employeeClearanceCertificateService.findById(id);
	}

	@Override
	public List<EmployeeClearanceCertificate> findAllById(List<Integer> id) {
		return employeeClearanceCertificateService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		employeeClearanceCertificateService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		employeeClearanceCertificateService.softBulkDelete(list);
	}

}
