package com.nouros.hrms.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;
import com.nouros.hrms.wrapper.SelfAssessmentWrapper;

/**
 * 
 * EmployeeReviewService interface is a service layer interface which handles
 * all the business logic related to EmployeeReview model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext EmployeeReview
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeReviewService extends GenericService<Integer,EmployeeReview> {

	EmployeePerformanceReviewCycle createEmployeeReview(EmployeePerformanceReviewCycle employeePerformanceReviewCycle);
	public EmployeeReview getEmployeeReviewByEmployeeAndDate(Integer employeeId , LocalDate currentDate);
	public SelfAssessmentWrapper getEmployeeSelfAssessmentFormData(Integer employeeReviewId);
	public String updateEmployeePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper);
	public String updateManagerPerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper);
	public String optionalUpdatePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper);
	List<EmployeeReview> getTopEmployeeReviewByPerformanceScore();
}
