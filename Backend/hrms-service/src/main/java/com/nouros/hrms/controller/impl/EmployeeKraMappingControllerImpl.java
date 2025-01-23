package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeKraMappingController;
import com.nouros.hrms.model.EmployeeKraMapping;
import com.nouros.hrms.service.EmployeeKraMappingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeKraMappingController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeKraMappingController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeKraMappingService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeKraMapping EmployeeKraMapping): creates an EmployeeKraMapping and returns the newly created EmployeeKraMapping.
count(String filter): returns the number of EmployeeKraMapping that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeKraMapping that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeKraMapping EmployeeKraMapping): updates an EmployeeKraMapping and returns the updated EmployeeKraMapping.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeKraMapping with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeKraMapping")
//@Tag(name="/EmployeeKraMapping",tags="EmployeeKraMapping",description="EmployeeKraMapping")
public class EmployeeKraMappingControllerImpl implements EmployeeKraMappingController {

  private static final Logger log = LogManager.getLogger(EmployeeKraMappingControllerImpl.class);

  @Autowired
  private EmployeeKraMappingService employeeKraMappingService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeKraMapping create(EmployeeKraMapping employeeKraMapping) {
	  log.info("inside @class EmployeeKraMappingControllerImpl @method create");
    return employeeKraMappingService.create(employeeKraMapping);
  }

  @Override
  public Long count(String filter) {
    return employeeKraMappingService.count(filter);
  }

  @Override
  public List<EmployeeKraMapping> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeKraMappingService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeKraMapping update(EmployeeKraMapping employeeKraMapping) {
    return employeeKraMappingService.update(employeeKraMapping);
  }

  @Override
  public EmployeeKraMapping findById(Integer id) {
    return employeeKraMappingService.findById(id);
  }

  @Override
  public List<EmployeeKraMapping> findAllById(List<Integer> id) {
    return employeeKraMappingService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeKraMappingService.deleteById(id);
  }
  
  
		
   
   
   
   
}
