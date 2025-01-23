package com.nouros.hrms.service;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.EducationalBenefitDto;

public interface EducationalBenefitService extends GenericService<Integer, EducationalBenefit>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public EducationalBenefit create(EducationalBenefit educationalBenefit);
	
	public HashMap<Integer, Double> getIdAndAmountByEmployeeId(Integer eid);
	
	public Double getSumOfAmount(Map<Integer,Double> healthClubBenefitHashMap);
	
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage);
	
	ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(String payrollRunProcessInstanceId);
	
	ResponseEntity<byte[]> downloadWpsFile(String educationBenefitProcessInstanceId);
	
	public EducationalBenefit update(EducationalBenefit educationalBenefit);

	EducationalBenefit updateEducationalBenefitWorkflowStage(EducationalBenefitDto educationalBenefitDto);
	
	 public ResponseEntity<byte[]> createWpsTxtFileForAllEducationalBenefit();
	 
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
	
	   List<Object[]> getAmountForEducationalBenefit(LocalDate startDate , LocalDate endDate);
	   List<Object[]> getAmountForAccuralEducationalBenefit(LocalDate startDate , LocalDate endDate);
}
