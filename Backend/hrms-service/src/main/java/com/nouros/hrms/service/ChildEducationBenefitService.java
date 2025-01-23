package com.nouros.hrms.service;

import java.time.LocalDate;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.ChildEducationBenefitDto;

public interface ChildEducationBenefitService   extends GenericService<Integer, ChildEducationBenefit> {

	public HashMap<Integer,Double> getIdAndAmountByEmployeeId(Integer eid);
	
	public Double getSumOfAmount(HashMap<Integer,Double> healthClubBenefitHashMap);
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage);
	
	ResponseEntity<byte[]> createWpsTxtFileForChildEducationalBenefit(String childEducationBenefitProcessInstanceId);
	 ResponseEntity<byte[]> downloadWpsFile(String childEducationBenefitProcessInstanceId);

	public ChildEducationBenefit updateChildEducationBenefitWorkflowStage(
			ChildEducationBenefitDto childEducationBenefitDto);

	public ResponseEntity<byte[]> createWpsTxtFileForAllChildEducationBenefit();
	 
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
	
	public List<Object[]> getAmountForAccuralChildEducationBenefit(LocalDate startDate , LocalDate endDate);
	
   public List<Object[]> getAmountForChildEducationBenefit(LocalDate startDate , LocalDate endDate);
}
