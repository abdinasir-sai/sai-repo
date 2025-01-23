package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeWorkExperienceController;
import com.nouros.hrms.model.EmployeeWorkExperience;
import com.nouros.hrms.service.EmployeeWorkExperienceService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeWorkExperienceController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeWorkExperienceController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeWorkExperienceService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeWorkExperience EmployeeWorkExperience): creates an EmployeeWorkExperience and returns the newly created EmployeeWorkExperience.
count(String filter): returns the number of EmployeeWorkExperience that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeWorkExperience that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeWorkExperience EmployeeWorkExperience): updates an EmployeeWorkExperience and returns the updated EmployeeWorkExperience.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeWorkExperience with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeWorkExperience")
//@Tag(name="/EmployeeWorkExperience",tags="EmployeeWorkExperience",description="EmployeeWorkExperience")
public class EmployeeWorkExperienceControllerImpl implements EmployeeWorkExperienceController {

  private static final Logger log = LogManager.getLogger(EmployeeWorkExperienceControllerImpl.class);

  @Autowired
  private EmployeeWorkExperienceService employeeWorkExperienceService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeWorkExperience create(EmployeeWorkExperience employeeWorkExperience) {
	  log.info("inside @class EmployeeWorkExperienceControllerImpl @method create");
    return employeeWorkExperienceService.create(employeeWorkExperience);
  }

  @Override
  public Long count(String filter) {
    return employeeWorkExperienceService.count(filter);
  }

  @Override
  public List<EmployeeWorkExperience> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeWorkExperienceService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeWorkExperience update(EmployeeWorkExperience employeeWorkExperience) {
    return employeeWorkExperienceService.update(employeeWorkExperience);
  }

  @Override
  public EmployeeWorkExperience findById(Integer id) {
    return employeeWorkExperienceService.findById(id);
  }

  @Override
  public List<EmployeeWorkExperience> findAllById(List<Integer> id) {
    return employeeWorkExperienceService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeWorkExperienceService.deleteById(id);
  }
  
 

		
   
   
   
   
}
