package com.nouros.hrms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.BusinessExpenseDto;

public interface BusinessExpenseService extends GenericService<Integer, BusinessExpense>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public BusinessExpense create(BusinessExpense businessExpense);
	public ResponseEntity<byte[]> createWpsTxtFileForBusinessExpense(String businessExpenseProcessInstanceId);
	public ResponseEntity<byte[]> downloadWpsFile(String businessExpenseProcessInstanceId) ;

	BusinessExpense updateBusinessExpenseWorkflowStage(BusinessExpenseDto businessExpenseDto);
	
	ResponseEntity<byte[]> createWpsTxtFileForAllBusinessExpense();

	ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
	
	public List<BusinessExpense> getListOfBusinessExpenseByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest);

	public List<Object[]> getBusinessExpenseforExpense(List<Integer> businessExpenseList);
	public  Object getBusinessExpenseforAccural(List<Integer> businessExpenseList);

}
