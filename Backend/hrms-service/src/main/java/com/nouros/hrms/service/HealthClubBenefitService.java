package com.nouros.hrms.service;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.Date;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.HealthClubBenefitDto;

public interface HealthClubBenefitService extends GenericService<Integer,HealthClubBenefit> {
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public HealthClubBenefit create(HealthClubBenefit healthClubBenefit);
	
	public HashMap<Integer,Double> getIdAndAmountByEmployeeId(Integer employeeId , Date payrollRun);
	
	public Double getSumOfAmount(HashMap<Integer,Double> healthClubBenefitHashMap);
	
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage);
	
	ResponseEntity<byte[]> downloadWpsFile(String healthClubBenefitProcessInstanceId);
	ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(String healthClubBenefitProcessInstanceId);
	
	public HealthClubBenefit update(HealthClubBenefit healthClubBenefit);

	HealthClubBenefit updateHealthClubBenefitWorkflowStage(HealthClubBenefitDto healthClubBenefitDto);
	
	public ResponseEntity<byte[]> createWpsTxtFileForAllHealthClubBenefit();
	 
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
	
	public List<Object[]> getAmountForHealthClubBenefit(LocalDate startDate , LocalDate endDate);
	public List<Object[]> getAmountForAccuralHealthClubBenefit(LocalDate startDate , LocalDate endDate);
}
