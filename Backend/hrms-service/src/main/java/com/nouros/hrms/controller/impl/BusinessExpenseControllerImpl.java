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
import com.nouros.hrms.controller.BusinessExpenseController;
import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.service.BusinessExpenseService;
import com.nouros.hrms.wrapper.BusinessExpenseDto;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/BusinessExpense")
public class BusinessExpenseControllerImpl implements BusinessExpenseController {
	
	private static final Logger log = LogManager.getLogger(BusinessExpenseControllerImpl.class);

	  @Autowired
	  private BusinessExpenseService businessExpenseService;
	  
	  @Override
	  @TriggerBPMN(entityName = "BusinessExpense", appName = "HRMS_APP_NAME")
	  public BusinessExpense create(BusinessExpense businessExpense) {
		  log.info("inside @class BusinessExpenseControllerImpl @method create");
	    return businessExpenseService.create(businessExpense);
	  }
	  
	  @Override
	  public Long count(String filter) {
	    return businessExpenseService.count(filter);
	  }

	  @Override
	  public List<BusinessExpense> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return businessExpenseService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public BusinessExpense update(BusinessExpense businessExpense) {
	    return businessExpenseService.update(businessExpense);
	  }

	  @Override
	  public BusinessExpense findById(Integer id) {
	    return businessExpenseService.findById(id);
	  }

	  @Override
	  public List<BusinessExpense> findAllById(List<Integer> id) {
	    return businessExpenseService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  businessExpenseService.deleteById(id);
	  }

	@Override
	public void bulkDelete(@Valid List<Integer> list) {
		businessExpenseService.softBulkDelete(list);
		
	}
	 
	@Override
	 public ResponseEntity<byte[]> createWpsTxtFileForBusinessExpense(@RequestParam("processInstanceId") String processInstanceId)
	 {
		return businessExpenseService.createWpsTxtFileForBusinessExpense(processInstanceId);
	 }

	@Override
	public ResponseEntity<byte[]> downloadWpsFile(String businessExpenseProcessInstanceId) {
		 
		return businessExpenseService.downloadWpsFile(businessExpenseProcessInstanceId);
	}

	@Override
	public BusinessExpense updateBusinessExpenseWorkflowStage(BusinessExpenseDto businessExpenseDto) {
		return businessExpenseService.updateBusinessExpenseWorkflowStage(businessExpenseDto);
	}
	
	@Override
	  public ResponseEntity<byte[]> createCommonWpsForBusinessExpense()
	  {
		return  businessExpenseService.createWpsTxtFileForAllBusinessExpense();
	  }
	  
	  @Override 
	  public ResponseEntity<byte[]> getAllRecordsWps(Integer weekNum)
	  {
		  return businessExpenseService.downloadCommonWpsFile(weekNum);
	  }
}
