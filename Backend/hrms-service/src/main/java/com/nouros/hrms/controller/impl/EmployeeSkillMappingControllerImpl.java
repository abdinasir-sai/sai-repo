package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeSkillMappingController;
import com.nouros.hrms.model.EmployeeSkillMapping;
import com.nouros.hrms.service.EmployeeSkillMappingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeSkillMappingController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeSkillMappingController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeSkillMappingService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeSkillMapping EmployeeSkillMapping): creates an EmployeeSkillMapping and returns the newly created EmployeeSkillMapping.
count(String filter): returns the number of EmployeeSkillMapping that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeSkillMapping that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeSkillMapping EmployeeSkillMapping): updates an EmployeeSkillMapping and returns the updated EmployeeSkillMapping.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeSkillMapping with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeSkillMapping")
//@Tag(name="/EmployeeSkillMapping",tags="EmployeeSkillMapping",description="EmployeeSkillMapping")
public class EmployeeSkillMappingControllerImpl implements EmployeeSkillMappingController {

  private static final Logger log = LogManager.getLogger(EmployeeSkillMappingControllerImpl.class);

  @Autowired
  private EmployeeSkillMappingService employeeSkillMappingService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeSkillMapping create(EmployeeSkillMapping employeeSkillMapping) {
	  log.info("inside @class EmployeeSkillMappingControllerImpl @method create");
    return employeeSkillMappingService.create(employeeSkillMapping);
  }

  @Override
  public Long count(String filter) {
    return employeeSkillMappingService.count(filter);
  }

  @Override
  public List<EmployeeSkillMapping> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeSkillMappingService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeSkillMapping update(EmployeeSkillMapping employeeSkillMapping) {
    return employeeSkillMappingService.update(employeeSkillMapping);
  }

  @Override
  public EmployeeSkillMapping findById(Integer id) {
    return employeeSkillMappingService.findById(id);
  }

  @Override
  public List<EmployeeSkillMapping> findAllById(List<Integer> id) {
    return employeeSkillMappingService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeSkillMappingService.deleteById(id);
  }
  

   
}
