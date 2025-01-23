package com.nouros.hrms.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.service.generic.GenericService;


public interface OtherExpenseBankRequestService  extends GenericService<Integer,OtherExpenseBankRequest> {

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
	
	public OtherExpenseBankRequest create(OtherExpenseBankRequest otherExpenseBankRequest);
	
	public OtherExpenseBankRequest savePath(String type, String path, LocalDate localDate, Integer year,
			Integer weekNumber, String stage,String json);
	
	public String getPathForWps(String type,Integer weeknum,Integer year);
	
	public void addRequestIdForOtherExpenseRequest(String requestId,String workFlowStage);
	
	public List<Integer> getEmployeeIdList(String workFlowStage);
	public List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByStage(String workflowStage,LocalDate localEndDate,String[] benefitsName);
	public List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByStageForEntity(String workflowStage,LocalDate localEndDate,String entityName);
}
