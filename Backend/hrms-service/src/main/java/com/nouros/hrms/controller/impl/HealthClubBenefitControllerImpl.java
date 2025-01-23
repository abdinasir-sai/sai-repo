package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.HealthClubBenefitController;
import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.service.HealthClubBenefitService;
import com.nouros.hrms.wrapper.HealthClubBenefitDto;

@Primary
@RestController
@RequestMapping("/HealthClubBenefit")
public class HealthClubBenefitControllerImpl implements HealthClubBenefitController{
	
	
	private static final Logger log = LogManager.getLogger(HealthClubBenefitControllerImpl.class);

	  @Autowired
	  private HealthClubBenefitService healthClubBenefitService;

	  @Override
	  @TriggerBPMN(entityName = "HealthClubBenefit", appName = "HRMS_APP_NAME")
	  public HealthClubBenefit create(HealthClubBenefit healthClubBenefit) {
		  log.info("inside @class HealthClubBenefitControllerImpl @method create");
	    return healthClubBenefitService.create(healthClubBenefit);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return healthClubBenefitService.count(filter);
	  }

	  @Override
	  public List<HealthClubBenefit> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return healthClubBenefitService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public HealthClubBenefit update(HealthClubBenefit healthClubBenefit) {
	    return healthClubBenefitService.update(healthClubBenefit);
	  }

	  @Override
	  public HealthClubBenefit findById(Integer id) {
	    return healthClubBenefitService.findById(id);
	  }

	  @Override
	  public List<HealthClubBenefit> findAllById(List<Integer> id) {
	    return healthClubBenefitService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  healthClubBenefitService.deleteById(id);
	  }

		@Override
		  public  ResponseEntity<byte[]> createWpsTxtFileForHealthClubBenefit( String processInstanceId)
		   {
			   return healthClubBenefitService.createWpsTxtFileForEducationalBenefit(processInstanceId);
		   }
			
		   @Override
		public
		   ResponseEntity<byte[]> downloadWpsFile (String healthClubBenefitProcessInstanceId)
		   {
			   return healthClubBenefitService.downloadWpsFile(healthClubBenefitProcessInstanceId);
		   }


		@Override
		public HealthClubBenefit updateHealthClubBenefitWorkflowStage(HealthClubBenefitDto healthClubBenefitDto) {
			return healthClubBenefitService.updateHealthClubBenefitWorkflowStage(healthClubBenefitDto);
		}

		@Override
		public ResponseEntity<byte[]> createCommonWpsForAllHealthClubBenefit()
		{
		return	healthClubBenefitService.createWpsTxtFileForAllHealthClubBenefit();
		}
		   
 
		@Override
		 public  ResponseEntity<byte[]> getAllRecordsWps( Integer weekNum)
		   {
			   return healthClubBenefitService.downloadCommonWpsFile(weekNum);
		   }
}
