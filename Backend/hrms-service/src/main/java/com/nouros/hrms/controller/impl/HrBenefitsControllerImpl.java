package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.HrBenefitsController;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.service.HrBenefitsService;
import com.nouros.hrms.wrapper.BenefitWrapper;
import com.nouros.hrms.wrapper.HrBenefitsDto;

@Primary
@RestController
@RequestMapping("/HrBenefits")
public class HrBenefitsControllerImpl implements HrBenefitsController {

	private static final Logger log = LogManager.getLogger(HrBenefitsControllerImpl.class);

	@Autowired
	private HrBenefitsService hrBenefitsService;

	@Override
	@TriggerBPMN(entityName = "HrBenefits", appName = "HRMS_APP_NAME")
	public HrBenefits create(HrBenefits hrBenefits) {
		log.info("inside @class HrBenefitsControllerImpl @method create");
		return hrBenefitsService.create(hrBenefits);
	}

	@Override
	public Long count(String filter) {
		return hrBenefitsService.count(filter);
	}

	@Override
	public List<HrBenefits> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return hrBenefitsService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public HrBenefits update(HrBenefits hrBenefits) {
		return hrBenefitsService.update(hrBenefits);
	}

	@Override
	public HrBenefits findById(Integer id) {
		return hrBenefitsService.findById(id);
	}

	@Override
	public List<HrBenefits> findAllById(List<Integer> id) {
		return hrBenefitsService.findAllById(id);
	}

	@Override
	public void deleteById(Integer id) {
		hrBenefitsService.deleteById(id);
	}

//	@Override
//	public String validateBenefit(BenefitWrapper benefitWrapper) {
//		 return hrBenefitsService.benefitValidate(benefitWrapper);
//	}

	@Override
	public String validateBenefit(Object benefitWrapper) {
		   return hrBenefitsService.benefitValidateString(benefitWrapper);
  
	}

	
	@Override
	public HrBenefits updateHrBenefitsWorkflowStage(HrBenefitsDto hrBenefitsDto)
	{
		return hrBenefitsService.updateHrBenefitsWorkflowStage(hrBenefitsDto);
		
	}

	@Override
	public ResponseEntity<byte[]> createCommonFileForBenefits(String benefitName) {
	 
		return  hrBenefitsService.createWpsTxtFileForAllBenefits(benefitName);
	}
	
	@Override
	public ResponseEntity<byte[]> getAllRecordsWps(Integer weekNum,String benefitName)
	{
		return hrBenefitsService.downloadCommonWpsFile(weekNum,benefitName);
	}

	@Override
	public String deleteHrBenefitsAndItsReferences(Integer id) {
		return hrBenefitsService.deleteHrBenefitsAndItsReferences(id);
	}

}
