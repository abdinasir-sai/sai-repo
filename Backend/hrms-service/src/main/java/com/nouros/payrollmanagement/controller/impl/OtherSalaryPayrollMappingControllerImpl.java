package com.nouros.payrollmanagement.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.OtherSalaryPayrollMappingController;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;
import com.nouros.payrollmanagement.service.OtherSalaryPayrollMappingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/OtherSalaryPayrollMapping")
public class OtherSalaryPayrollMappingControllerImpl implements OtherSalaryPayrollMappingController {

	private static final Logger log = LogManager.getLogger(OtherSalaryPayrollMappingControllerImpl.class);

	@Autowired
	  private OtherSalaryPayrollMappingService otherSalaryPayrollMappingService;
	
	  @Override
	  public OtherSalaryPayrollMapping create(OtherSalaryPayrollMapping employeeUnstructuredSalary) {
		  log.info("inside @class OtherSalaryPayrollMappingControllerImpl @method create");
	    return otherSalaryPayrollMappingService.create(employeeUnstructuredSalary);
	  }

	  @Override
	  public Long count(String filter) {
	    return otherSalaryPayrollMappingService.count(filter);
	  }

	  @Override
	  public List<OtherSalaryPayrollMapping> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return otherSalaryPayrollMappingService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	  public OtherSalaryPayrollMapping update(OtherSalaryPayrollMapping employeeUnstructuredSalary) {
	    return otherSalaryPayrollMappingService.update(employeeUnstructuredSalary);
	  }

	  @Override
	  public OtherSalaryPayrollMapping findById(Integer id) {
	    return otherSalaryPayrollMappingService.findById(id);
	  }

	  @Override
	  public List<OtherSalaryPayrollMapping> findAllById(List<Integer> id) {
	    return otherSalaryPayrollMappingService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void deleteById(Integer id) {
		  otherSalaryPayrollMappingService.deleteById(id);
	  }
}
