package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeFeedbackMappingController;
import com.nouros.hrms.model.EmployeeFeedbackMapping;
import com.nouros.hrms.service.EmployeeFeedbackMappingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeFeedbackMappingController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeFeedbackMappingController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeFeedbackMappingService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeFeedbackMapping EmployeeFeedbackMapping): creates an EmployeeFeedbackMapping and returns the newly created EmployeeFeedbackMapping.
count(String filter): returns the number of EmployeeFeedbackMapping that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeFeedbackMapping that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeFeedbackMapping EmployeeFeedbackMapping): updates an EmployeeFeedbackMapping and returns the updated EmployeeFeedbackMapping.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeFeedbackMapping with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeFeedbackMapping")
//@Tag(name="/EmployeeFeedbackMapping",tags="EmployeeFeedbackMapping",description="EmployeeFeedbackMapping")
public class EmployeeFeedbackMappingControllerImpl implements EmployeeFeedbackMappingController {

  private static final Logger log = LogManager.getLogger(EmployeeFeedbackMappingControllerImpl.class);

  @Autowired
  private EmployeeFeedbackMappingService employeeFeedbackMappingService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeFeedbackMapping create(EmployeeFeedbackMapping employeeFeedbackMapping) {
	  log.info("inside @class EmployeeFeedbackMappingControllerImpl @method create");
    return employeeFeedbackMappingService.create(employeeFeedbackMapping);
  }

  @Override
  public Long count(String filter) {
    return employeeFeedbackMappingService.count(filter);
  }

  @Override
  public List<EmployeeFeedbackMapping> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeFeedbackMappingService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeFeedbackMapping update(EmployeeFeedbackMapping employeeFeedbackMapping) {
    return employeeFeedbackMappingService.update(employeeFeedbackMapping);
  }

  @Override
  public EmployeeFeedbackMapping findById(Integer id) {
    return employeeFeedbackMappingService.findById(id);
  }

  @Override
  public List<EmployeeFeedbackMapping> findAllById(List<Integer> id) {
    return employeeFeedbackMappingService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeFeedbackMappingService.deleteById(id);
  }
  

   
}
