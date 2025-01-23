package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.ApplicantLanguageController;
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.service.ApplicantLanguageService;


@Primary
@RestController
@RequestMapping("/ApplicantLanguage")
public class ApplicantLanguageControllerImpl implements ApplicantLanguageController{
	
	

	private static final Logger log = LogManager.getLogger(ApplicantLanguageControllerImpl.class);

	  @Autowired
	  private ApplicantLanguageService applicantLanguageService;

	  @Override
	  //@TriggerBPMN(entityName = "ApplicantLanguage", appName = "HRMS_APP_NAME")
	  public ApplicantLanguage create(ApplicantLanguage applicantLanguage) {
		  log.info("inside @class ApplicantLanguageControllerImpl @method create");
	    return applicantLanguageService.create(applicantLanguage);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return applicantLanguageService.count(filter);
	  }

	  @Override
	  public List<ApplicantLanguage> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return applicantLanguageService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public ApplicantLanguage update(ApplicantLanguage applicantLanguage) {
	    return applicantLanguageService.update(applicantLanguage);
	  }

	  @Override
	  public ApplicantLanguage findById(Integer id) {
	    return applicantLanguageService.findById(id);
	  }

	  @Override
	  public List<ApplicantLanguage> findAllById(List<Integer> id) {
	    return applicantLanguageService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  applicantLanguageService.deleteById(id);
	  }
	

}
