package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeExpensesController;
import com.nouros.hrms.model.EmployeeExpenses;
import com.nouros.hrms.service.EmployeeExpensesService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/EmployeeExpenses")

public class EmployeeExpensesControllerImpl implements EmployeeExpensesController {

	private static final Logger log = LogManager.getLogger(EmployeeExpensesControllerImpl.class);

	@Autowired
	private EmployeeExpensesService employeeExpensesService;

	@TriggerBPMN(entityName = "EmployeeExpenses", appName = "HRMS_APP_NAME")
	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public EmployeeExpenses create(EmployeeExpenses employeeExpenses) {
		log.info("inside @class EmployeeExpensesControllerImpl @method create");
		return employeeExpensesService.create(employeeExpenses);
	}

	@Override
	public Long count(String filter) {
		return employeeExpensesService.count(filter);
	}

	@Override
	public List<EmployeeExpenses> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return employeeExpensesService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public EmployeeExpenses update(EmployeeExpenses employeeExpenses) {
		return employeeExpensesService.update(employeeExpenses);
	}

	@Override
	public EmployeeExpenses findById(Integer id) {
		return employeeExpensesService.findById(id);
	}

	@Override
	public List<EmployeeExpenses> findAllById(List<Integer> id) {
		return employeeExpensesService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		employeeExpensesService.deleteById(id);
	}


	

}
