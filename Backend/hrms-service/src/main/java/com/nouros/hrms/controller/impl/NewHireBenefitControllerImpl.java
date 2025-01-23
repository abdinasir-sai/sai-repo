package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.NewHireBenefitController;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.service.NewHireBenefitService;
import com.nouros.hrms.wrapper.NewHireBenefitDto;

@Primary
@RestController
@RequestMapping("/NewHireBenefit")
public class NewHireBenefitControllerImpl implements NewHireBenefitController{

	 private static final Logger log = LogManager.getLogger(NewHireBenefitControllerImpl.class);

	  @Autowired
	  private NewHireBenefitService newHireBenefitService;

	  @Override
	  @TriggerBPMN(entityName = "NewHireBenefit", appName = "HRMS_APP_NAME")
	  public NewHireBenefit create(NewHireBenefit newHireBenefit) {
		  log.info("inside @class NewHireBenefitControllerImpl @method create");
	    return newHireBenefitService.create(newHireBenefit);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return newHireBenefitService.count(filter);
	  }

	  @Override
	  public List<NewHireBenefit> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return newHireBenefitService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public NewHireBenefit update(NewHireBenefit newHireBenefit) {
	    return newHireBenefitService.update(newHireBenefit);
	  }

	  @Override
	  public NewHireBenefit findById(Integer id) {
	    return newHireBenefitService.findById(id);
	  }

	  @Override
	  public List<NewHireBenefit> findAllById(List<Integer> id) {
	    return newHireBenefitService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  newHireBenefitService.deleteById(id);
	  }


	@Override
	public ResponseEntity<byte[]> createWpsTxtFileForNewHireBenefit(String processInstanceId) {
		 
		return newHireBenefitService.createWpsTxtFileForNewHireBenefit(processInstanceId) ;
	}


	@Override
	public ResponseEntity<byte[]> downloadWpsFile(String educationBenefitProcessInstanceId) {
		 
		return newHireBenefitService.downloadWpsFile(educationBenefitProcessInstanceId);
	}


	@Override
	public NewHireBenefit updateNewHireBenefitWorkflowStage(NewHireBenefitDto newHireBenefitDto) {
		return newHireBenefitService.updateNewHireBenefitWorkflowStage(newHireBenefitDto);
	}
	
	@Override
	public ResponseEntity<byte[]> createCommonFileForNewHireBenefit()
	   {
		   return newHireBenefitService.createWpsTxtFileForAllNewHireBenefit();
	   }
	
	   @Override
	public ResponseEntity<byte[]> getAllRecordsWps(Integer weekNum)
	{
	  return newHireBenefitService.downloadCommonWpsFile(weekNum);
	}
	
}
