package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeCompetenciesDetailsController;
import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.service.EmployeeCompetenciesDetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeCompetenciesDetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeCompetenciesDetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeCompetenciesDetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeCompetenciesDetails EmployeeCompetenciesDetails): creates an EmployeeCompetenciesDetails and returns the newly created EmployeeCompetenciesDetails.
count(String filter): returns the number of EmployeeCompetenciesDetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeCompetenciesDetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeCompetenciesDetails EmployeeCompetenciesDetails): updates an EmployeeCompetenciesDetails and returns the updated EmployeeCompetenciesDetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeCompetenciesDetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeCompetenciesDetails")
//@Tag(name="/EmployeeCompetenciesDetails",tags="EmployeeCompetenciesDetails",description="EmployeeCompetenciesDetails")
public class EmployeeCompetenciesDetailsControllerImpl implements EmployeeCompetenciesDetailsController {

  private static final Logger log = LogManager.getLogger(EmployeeCompetenciesDetailsControllerImpl.class);

  @Autowired
  private EmployeeCompetenciesDetailsService employeeCompetenciesDetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeCompetenciesDetails create(EmployeeCompetenciesDetails employeeCompetenciesDetails) {
	  log.info("inside @class EmployeeCompetenciesDetailsControllerImpl @method create");
    return employeeCompetenciesDetailsService.create(employeeCompetenciesDetails);
  }

  @Override
  public Long count(String filter) {
    return employeeCompetenciesDetailsService.count(filter);
  }

  @Override
  public List<EmployeeCompetenciesDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeCompetenciesDetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeCompetenciesDetails update(EmployeeCompetenciesDetails employeeCompetenciesDetails) {
    return employeeCompetenciesDetailsService.update(employeeCompetenciesDetails);
  }

  @Override
  public EmployeeCompetenciesDetails findById(Integer id) {
    return employeeCompetenciesDetailsService.findById(id);
  }

  @Override
  public List<EmployeeCompetenciesDetails> findAllById(List<Integer> id) {
    return employeeCompetenciesDetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeCompetenciesDetailsService.deleteById(id);
  }
  
 
 
  @Override
  public List<EmployeeCompetenciesDetails> showEmployeeCompetenciesDetailsByEmployeeReviewId(Integer employeeReviewId)
  {
	  return employeeCompetenciesDetailsService.getAllEmployeeCompetencies(employeeReviewId);
  }
		
   
   
   
   
}
