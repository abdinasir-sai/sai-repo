package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.EmployeePerformanceReviewCycleController;
import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.service.EmployeePerformanceReviewCycleService;


@Primary
@RestController
@RequestMapping("/EmployeePerformanceReviewCycle")
public class EmployeePerformanceReviewCycleControllerImpl implements EmployeePerformanceReviewCycleController {
	
	private static final Logger log = LogManager.getLogger(EmployeePerformanceReviewCycleControllerImpl.class);
	
	@Autowired
	private EmployeePerformanceReviewCycleService employeePerformanceReviewCycleService;
	
	  @Override
	  @TriggerBPMN(entityName = "EmployeePerformanceReviewCycle", appName = "HRMS_APP_NAME")
	  public EmployeePerformanceReviewCycle create(EmployeePerformanceReviewCycle employeePerformanceReviewCycle) {
		  log.info("inside @class EmployeePerformanceReviewCycleControllerImpl @method create");
	    return employeePerformanceReviewCycleService.create(employeePerformanceReviewCycle);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return employeePerformanceReviewCycleService.count(filter);
	  }

	  @Override
	  public List<EmployeePerformanceReviewCycle> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return employeePerformanceReviewCycleService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public EmployeePerformanceReviewCycle update(EmployeePerformanceReviewCycle employeePerformanceReviewCycle) {
	    return employeePerformanceReviewCycleService.update(employeePerformanceReviewCycle);
	  }

	  @Override
	  public EmployeePerformanceReviewCycle findById(Integer id) {
	    return employeePerformanceReviewCycleService.findById(id);
	  }

	  @Override
	  public List<EmployeePerformanceReviewCycle> findAllById(List<Integer> id) {
	    return employeePerformanceReviewCycleService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  employeePerformanceReviewCycleService.softDelete(id);
	  }
	  @Override
	  public void bulkDelete(List<Integer> list) {
		  employeePerformanceReviewCycleService.softBulkDelete(list);
	  }

	  @Override
	  public Boolean checkEmployeeReviewCycle()
	  {
		 return employeePerformanceReviewCycleService.getValueforEmployeeReview();
	  }

}
