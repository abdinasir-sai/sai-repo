package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeGoalsDetailsController;
import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.service.EmployeeGoalsDetailsService;
import com.nouros.hrms.wrapper.GoalWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeGoalsMappingController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeGoalsMappingController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeGoalsMappingService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeGoalsMapping EmployeeGoalsMapping): creates an EmployeeGoalsMapping and returns the newly created EmployeeGoalsMapping.
count(String filter): returns the number of EmployeeGoalsMapping that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeGoalsMapping that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeGoalsMapping EmployeeGoalsMapping): updates an EmployeeGoalsMapping and returns the updated EmployeeGoalsMapping.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeGoalsMapping with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeGoalsDetails")
//@Tag(name="/EmployeeGoalsMapping",tags="EmployeeGoalsMapping",description="EmployeeGoalsMapping")
public class EmployeeGoalsDetailsControllerImpl implements EmployeeGoalsDetailsController {

  private static final Logger log = LogManager.getLogger(EmployeeGoalsDetailsControllerImpl.class);

  @Autowired
  private EmployeeGoalsDetailsService employeeGoalsDetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeGoalsDetails create(EmployeeGoalsDetails employeeGoalsDetails) {
	  log.info("inside @class EmployeeGoalsDetailsControllerImpl @method create");
    return employeeGoalsDetailsService.create(employeeGoalsDetails);
  }

  @Override
  public Long count(String filter) {
    return employeeGoalsDetailsService.count(filter);
  }

  @Override
  public List<EmployeeGoalsDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeGoalsDetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeGoalsDetails update(EmployeeGoalsDetails employeeGoalsDetails) {
    return employeeGoalsDetailsService.update(employeeGoalsDetails);
  }

  @Override
  public EmployeeGoalsDetails findById(Integer id) {
    return employeeGoalsDetailsService.findById(id);
  }

  @Override
  public List<EmployeeGoalsDetails> findAllById(List<Integer> id) {
    return employeeGoalsDetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
	  employeeGoalsDetailsService.deleteById(id);
  }
  
  @Override
  public List<EmployeeGoalsDetails> createEmployeeGoals(GoalWrapper goalWrapper)
  {
	  return employeeGoalsDetailsService.createEmployeeGoals(goalWrapper);
  }
    
  @Override
  public String deleteEmployeeGoalDetailById(Integer employeeGoalId) {
	  return employeeGoalsDetailsService.deleteEmployeeGoalDetailById(employeeGoalId);
  }
  
  @Override
  public List<EmployeeGoalsDetails> showEmployeeGoalDetailByEmployeeReviewId(Integer employeeReviewId)
  {
	  return employeeGoalsDetailsService.getEmployeeGoalsDetailsByEmployeeReviewId(employeeReviewId);
  }
  
  @Override 
  public String updateEmployeeGoalDetailList(List<EmployeeGoalsDetails> employeeGoalsDetailsList)
  {
	  return employeeGoalsDetailsService.updateEmployeeGoalsDetails(employeeGoalsDetailsList);
  }
}
