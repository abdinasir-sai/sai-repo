package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.EducationalBenefitController;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.service.EducationalBenefitService;
import com.nouros.hrms.wrapper.EducationalBenefitDto;

@Primary
@RestController
@RequestMapping("/EducationalBenefit")
public class EducationalBenefitControllerImpl implements EducationalBenefitController{

	private static final Logger log = LogManager.getLogger(EducationalBenefitControllerImpl.class);

	  @Autowired
	  private EducationalBenefitService educationalBenefitService;

	  @Override
	 @TriggerBPMN(entityName = "EducationalBenefit", appName = "HRMS_APP_NAME")
	  public EducationalBenefit create(EducationalBenefit educationalBenefit) {
		  log.info("inside @class EducationalBenefitControllerImpl @method create");
	    return educationalBenefitService.create(educationalBenefit);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return educationalBenefitService.count(filter);
	  }

	  @Override
	  public List<EducationalBenefit> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return educationalBenefitService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public EducationalBenefit update(EducationalBenefit educationalBenefit) {
	    return educationalBenefitService.update(educationalBenefit);
	  }

	  @Override
	  public EducationalBenefit findById(Integer id) {
	    return educationalBenefitService.findById(id);
	  }

	  @Override
	  public List<EducationalBenefit> findAllById(List<Integer> id) {
	    return educationalBenefitService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  educationalBenefitService.deleteById(id);
	  }
	  
	  @Override
	 public ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(String processInstanceId)
	 {
		 return educationalBenefitService.createWpsTxtFileForEducationalBenefit(processInstanceId);
		 
	 }
	 @Override
	 public ResponseEntity<byte[]> downloadWpsFile(String processInstanceId)
	 {
		 return educationalBenefitService.downloadWpsFile(processInstanceId);
		 
	 }


	@Override
	public EducationalBenefit updateEducationalBenefitWorkflowStage(EducationalBenefitDto educationalBenefitDto) {
	return educationalBenefitService.updateEducationalBenefitWorkflowStage(educationalBenefitDto);
	} 
	
	@Override
	public  ResponseEntity<byte[]> createCommonFileForEducationBenefit()
	  {
		return  educationalBenefitService.createWpsTxtFileForAllEducationalBenefit();
	  }
	  
	@Override
	public ResponseEntity<byte[]> getAllRecordsWps(Integer weekNum)
	{
		return educationalBenefitService.downloadCommonWpsFile(weekNum);
	}
	
}
