package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.ApplicantCertificationsController;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.service.ApplicantCertificationsService;


@Primary
@RestController
@RequestMapping("/ApplicantCertifications")
public class ApplicantCertificationsControllerImpl implements ApplicantCertificationsController{
	

	private static final Logger log = LogManager.getLogger(ApplicantCertificationsControllerImpl.class);

	  @Autowired
	  private ApplicantCertificationsService applicantCertificationsService;

	  @Override
	  //@TriggerBPMN(entityName = "ApplicantCertifications", appName = "HRMS_APP_NAME")
	  public ApplicantCertifications create(ApplicantCertifications applicantCertifications) {
		  log.info("inside @class ApplicantCertificationsControllerImpl @method create");
	    return applicantCertificationsService.create(applicantCertifications);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return applicantCertificationsService.count(filter);
	  }

	  @Override
	  public List<ApplicantCertifications> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return applicantCertificationsService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public ApplicantCertifications update(ApplicantCertifications applicantCertifications) {
	    return applicantCertificationsService.update(applicantCertifications);
	  }

	  @Override
	  public ApplicantCertifications findById(Integer id) {
	    return applicantCertificationsService.findById(id);
	  }

	  @Override
	  public List<ApplicantCertifications> findAllById(List<Integer> id) {
	    return applicantCertificationsService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  applicantCertificationsService.deleteById(id);
	  }
	

}
