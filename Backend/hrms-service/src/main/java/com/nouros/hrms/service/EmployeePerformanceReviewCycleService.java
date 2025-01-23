package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;

public interface EmployeePerformanceReviewCycleService extends GenericService<Integer,EmployeePerformanceReviewCycle>{


	void softDelete(int id);
	
	void softBulkDelete(List<Integer> list);
	
	EmployeePerformanceReviewCycle createEmployeePerformanceReviewCycleByWrapper(EmployeePerformanceReviewCycleWrapper employeePerformanceReviewCycleWrapper);

	Boolean getValueforEmployeeReview();

}