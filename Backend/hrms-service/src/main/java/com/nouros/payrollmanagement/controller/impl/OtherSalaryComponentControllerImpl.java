package com.nouros.payrollmanagement.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.OtherSalaryComponentController;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.service.OtherSalaryComponentAttachmentService;
import com.nouros.payrollmanagement.service.OtherSalaryComponentService;

import jakarta.validation.constraints.NotEmpty;

@Primary
@RestController
@RequestMapping("/OtherSalaryComponent")
//@Tag(name="/OtherSalaryComponentControllerImpl",tags="OtherSalaryComponent",description="OtherSalaryComponent")
public class OtherSalaryComponentControllerImpl implements OtherSalaryComponentController{

	private static final Logger log = LogManager.getLogger(OtherSalaryComponentControllerImpl.class);
	

	@Autowired
	  private OtherSalaryComponentService otherSalaryComponentService;
	
	@Autowired
	  private OtherSalaryComponentAttachmentService otherSalaryComponentAttachmentService;
	
	  @Override
	  public OtherSalaryComponent create(OtherSalaryComponent employeeUnstructuredSalary) {
		  log.info("inside @class OtherSalaryComponentControllerImpl @method create");
	    return otherSalaryComponentService.create(employeeUnstructuredSalary);
	  }

	  @Override
	  public Long count(String filter) {
	    return otherSalaryComponentService.count(filter);
	  }

	  @Override
	  public List<OtherSalaryComponent> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return otherSalaryComponentService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	  public OtherSalaryComponent update(OtherSalaryComponent employeeUnstructuredSalary) {
	    return otherSalaryComponentService.update(employeeUnstructuredSalary);
	  }

	  @Override
	  public OtherSalaryComponent findById(Integer id) {
	    return otherSalaryComponentService.findById(id);
	  }

	  @Override
	  public List<OtherSalaryComponent> findAllById(List<Integer> id) {
	    return otherSalaryComponentService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void deleteById(Integer id) {
		  otherSalaryComponentService.softDelete(id);
	  }

		
	  @Override
	  public List<WorkflowActions> getActions(@NotEmpty Integer id) {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id,"OtherSalaryComponent");
	   }

	@Override
	public OtherSalaryComponent updateFile(MultipartFile file, Integer id) {
		return  otherSalaryComponentAttachmentService.upload(file, id);
	}
	
	@Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void bulkDelete(List<Integer> list) {
		otherSalaryComponentService.softBulkDelete(list);
	  }

	  

}
