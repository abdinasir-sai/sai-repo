package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantDependentDetailsController;
import com.nouros.hrms.model.ApplicantDependentDetails;
import com.nouros.hrms.service.ApplicantDependentDetailsService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/ApplicantDependentDetails")
public class ApplicantDependentDetailsControllerImpl implements ApplicantDependentDetailsController{

	private static final Logger log = LogManager.getLogger(ApplicantDependentDetailsControllerImpl.class);

	  @Autowired
	  private ApplicantDependentDetailsService applicantDependentDetailsService;
	
	  @Override
	  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE DEPENDENT DETAILS")
	  public ApplicantDependentDetails create(ApplicantDependentDetails applicantDependentDetails) {
		  log.info("inside @class EmployeeDependentDetailsControllerImpl @method create");
	    return applicantDependentDetailsService.create(applicantDependentDetails);
	  }

	  @Override
	  public Long count(String filter) {
	    return applicantDependentDetailsService.count(filter);
	  }

	  @Override
	  public List<ApplicantDependentDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return applicantDependentDetailsService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE DEPENDENT DETAILS")
	  public ApplicantDependentDetails update(ApplicantDependentDetails applicantDependentDetails) {
	    return applicantDependentDetailsService.update(applicantDependentDetails);
	  }

	  @Override
	  public ApplicantDependentDetails findById(Integer id) {
	    return applicantDependentDetailsService.findById(id);
	  }

	  @Override
	  public List<ApplicantDependentDetails> findAllById(List<Integer> id) {
	    return applicantDependentDetailsService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE DEPENDENT DETAILS")
	  public void deleteById(Integer id) {
		  applicantDependentDetailsService.deleteById(id);
	  }
	

}
