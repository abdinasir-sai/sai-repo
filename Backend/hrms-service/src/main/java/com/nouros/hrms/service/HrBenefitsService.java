package com.nouros.hrms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.BenefitWrapper;
import com.nouros.hrms.wrapper.HrBenefitsDto;

public interface HrBenefitsService extends GenericService<Integer,HrBenefits>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public HrBenefits create(HrBenefits hrBenefits);
	
	public List<Object[]> getForHRBenefitsForExpense(String status ,String type,List<Integer> listOfHrBenefitsId);

	public Object[] getForHRBenefitsForAccural(String status ,String type,List<Integer> listOfHrBenefitsId);
	
	public void updateGLStatus(String newStatus, String oldStatus,String workFlowStage); 
	
	//public String benefitValidate(BenefitWrapper benefitWrapper);
	
	HrBenefits updateHrBenefitsWorkflowStage(HrBenefitsDto hrBenefitsDto);

	public ResponseEntity<byte[]> createWpsTxtFileForAllBenefits(String benefitName);

	 public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum,String benefitName);
	 
	 public String benefitValidateString(Object benefitString);

	String deleteHrBenefitsAndItsReferences(Integer id);
	public List<HrBenefits> getListOfHrBenefitsByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest);
}
