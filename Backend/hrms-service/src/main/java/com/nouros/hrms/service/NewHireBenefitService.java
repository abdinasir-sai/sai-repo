package com.nouros.hrms.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.NewHireBenefitDto;

public interface NewHireBenefitService extends GenericService<Integer,NewHireBenefit>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public NewHireBenefit create(NewHireBenefit newHireBenefit);

	public HashMap<Integer,Double> getIdAndAmountByEmployeeId(Integer eid);
	
	public Double getSumOfAmount(HashMap<Integer,Double> healthClubBenefitHashMap);

	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage);
	ResponseEntity<byte[]> createWpsTxtFileForNewHireBenefit(String newHireBenefitProcessInstanceId);
	ResponseEntity<byte[]> downloadWpsFile(String newHireBenefitProcessInstanceId);

	NewHireBenefit updateNewHireBenefitWorkflowStage(NewHireBenefitDto newHireBenefitDto);
	
	public ResponseEntity<byte[]> createWpsTxtFileForAllNewHireBenefit();
	 
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
 
	List<Object[]> getAmountForNewHireBenefit(LocalDate startDate , LocalDate endDate);
	
	List<Object[]> getAmountForAccuralNewHireBenefit(LocalDate startDate , LocalDate endDate);

}
