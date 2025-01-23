package com.nouros.hrms.controller.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.ApplicantReferralController;
import com.nouros.hrms.model.ApplicantReferral;
import com.nouros.hrms.service.ApplicantReferralService;

@Primary
@RestController
@RequestMapping("/ApplicantReferral")
public class ApplicantReferralControllerImpl implements ApplicantReferralController{
	
	private static final Logger log = LogManager.getLogger(ApplicantReferralControllerImpl.class);

	  @Autowired
	  private ApplicantReferralService applicantReferralService;

	  @Override
	  @TriggerBPMN(entityName = "ApplicantReferral", appName = "HRMS_APP_NAME")
	  public ApplicantReferral create(ApplicantReferral applicantReferral) {
		  log.info("inside @class ApplicantReferralControllerImpl @method create");
	    return applicantReferralService.create(applicantReferral);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return applicantReferralService.count(filter);
	  }

	  @Override
	  public List<ApplicantReferral> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return applicantReferralService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public ApplicantReferral update(ApplicantReferral applicantReferral) {
	    return applicantReferralService.update(applicantReferral);
	  }

	  @Override
	  public ApplicantReferral findById(Integer id) {
	    return applicantReferralService.findById(id);
	  }

	  @Override
	  public List<ApplicantReferral> findAllById(List<Integer> id) {
	    return applicantReferralService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  applicantReferralService.deleteById(id);
	  }
	
	

}
