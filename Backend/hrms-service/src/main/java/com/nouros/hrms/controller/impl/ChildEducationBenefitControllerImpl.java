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
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ChildEducationBenefitController;
import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.service.ChildEducationBenefitService;
import com.nouros.hrms.wrapper.ChildEducationBenefitDto;

@Primary
@RestController
@RequestMapping("/ChildEducationBenefit")
public class ChildEducationBenefitControllerImpl implements ChildEducationBenefitController {

	private static final Logger log = LogManager.getLogger(CityControllerImpl.class);

	  @Autowired
	  private ChildEducationBenefitService childEducationBenefitService;
	  
	  @Override
	  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE ChildEducationBenefit")
	  @TriggerBPMN(entityName = "ChildEducationBenefit", appName = "HRMS_APP_NAME")
	  public ChildEducationBenefit create(ChildEducationBenefit childEducationBenefit) {
		  log.info("inside @class CityControllerImpl @method create");
	    return childEducationBenefitService.create(childEducationBenefit);
	  }

	  @Override
	  public Long count(String filter) {
	    return childEducationBenefitService.count(filter);
	  }

	  @Override
	  public List<ChildEducationBenefit> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return childEducationBenefitService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE ChildEducationBenefit")
	  public ChildEducationBenefit update(ChildEducationBenefit childEducationBenefit) {
	    return childEducationBenefitService.update(childEducationBenefit);
	  }

	  @Override
	  public ChildEducationBenefit findById(Integer id) {
	    return childEducationBenefitService.findById(id);
	  }

	  @Override
	  public List<ChildEducationBenefit> findAllById(List<Integer> id) {
	    return childEducationBenefitService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE ChildEducationBenefit")
	  public void deleteById(Integer id) {
		  childEducationBenefitService.deleteById(id);
	  }
	  
	  
	  @Override
		 public ResponseEntity<byte[]> createWpsTxtFileForChildEducationalBenefit(String processInstanceId)
		 {
			 return childEducationBenefitService.createWpsTxtFileForChildEducationalBenefit(processInstanceId);
			 
		 }
		 @Override
		 public ResponseEntity<byte[]> downloadWpsFile(String processInstanceId)
		 {
			 return childEducationBenefitService.downloadWpsFile(processInstanceId);
			 
		 }

		@Override
		public ChildEducationBenefit updateChildEducationBenefitWorkflowStage(
				ChildEducationBenefitDto childEducationBenefitDto) {
			return childEducationBenefitService.updateChildEducationBenefitWorkflowStage(childEducationBenefitDto);
		} 
	  
		
		@Override 
		public  ResponseEntity<byte[]> createCommonWpsForAllChildEducationBenefit()
		{
			return childEducationBenefitService.createWpsTxtFileForAllChildEducationBenefit();
		}
		
		@Override
		public ResponseEntity<byte[]> getAllRecordsWps(Integer weekNum)
		{
			return childEducationBenefitService.downloadCommonWpsFile(weekNum);
		}
}