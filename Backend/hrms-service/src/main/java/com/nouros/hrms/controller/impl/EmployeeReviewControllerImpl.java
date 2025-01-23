package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeReviewController;
import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.service.EmployeeReviewService;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;
import com.nouros.hrms.wrapper.SelfAssessmentWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeReviewController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeReviewController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeReviewService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeReview EmployeeReview): creates an EmployeeReview and returns the newly created EmployeeReview.
count(String filter): returns the number of EmployeeReview that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeReview that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeReview EmployeeReview): updates an EmployeeReview and returns the updated EmployeeReview.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeReview with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeReview")
//@Tag(name="/EmployeeReview",tags="EmployeeReview",description="EmployeeReview")
public class EmployeeReviewControllerImpl implements EmployeeReviewController {

  private static final Logger log = LogManager.getLogger(EmployeeReviewControllerImpl.class);

  @Autowired
  private EmployeeReviewService employeeReviewService;
  

	
  @Override
  @TriggerBPMN(entityName = "EmployeeReview", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeReview create(EmployeeReview employeeReview) {
	  log.info("inside @class EmployeeReviewControllerImpl @method create");
    return employeeReviewService.create(employeeReview);
  }

  @Override
  public Long count(String filter) {
    return employeeReviewService.count(filter);
  }

  @Override
  public List<EmployeeReview> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeReviewService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeReview update(EmployeeReview employeeReview) {
    return employeeReviewService.update(employeeReview);
  }

  @Override
  public EmployeeReview findById(Integer id) {
    return employeeReviewService.findById(id);
  }

  @Override
  public List<EmployeeReview> findAllById(List<Integer> id) {
    return employeeReviewService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeReviewService.deleteById(id);
  }
  
  @Override
  public EmployeePerformanceReviewCycle createEmployeeReview(EmployeePerformanceReviewCycle employeePerformanceReviewCycle)
  {
	 return employeeReviewService.createEmployeeReview(employeePerformanceReviewCycle);
  }

	@Override
	public SelfAssessmentWrapper getEmployeeSelfAssessmentData(Integer employeeReviewId)
  {
	  return employeeReviewService.getEmployeeSelfAssessmentFormData(employeeReviewId);
  }

	@Override
  public String updateEmployeePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper)
	  {
		return employeeReviewService.updateEmployeePerfomanceData(selfAssessmentWrapper);  
	  }

	@Override
	  public String updateManagerPerfomanceData( SelfAssessmentWrapper selfAssessmentWrapper)
		  {
			return employeeReviewService.updateManagerPerfomanceData(selfAssessmentWrapper);  
		  }
	
	
	@Override
	public String optionalUpdatePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper)
	{
		return employeeReviewService.optionalUpdatePerfomanceData(selfAssessmentWrapper);
	}
   
}

