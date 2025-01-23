package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CasesController;
import com.nouros.hrms.model.Cases;
import com.nouros.hrms.service.CasesService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CasesController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CasesController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CasesService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Cases Cases): creates an Cases and returns the newly created Cases.
count(String filter): returns the number of Cases that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Cases that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Cases Cases): updates an Cases and returns the updated Cases.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Cases with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Cases")

//@Tag(name="/Cases",tags="Cases",description="Cases")
public class CasesControllerImpl implements CasesController {

  private static final Logger log = LogManager.getLogger(CasesControllerImpl.class);

  @Autowired
  private CasesService casesService;
  
  @Override
  @TriggerBPMN(entityName = "Cases", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Cases create(Cases cases) {
	  log.info("inside @class CasesControllerImpl @method create");
    return casesService.create(cases);
  }

  @Override
  public Long count(String filter) {
    return casesService.count(filter);
  }

  @Override
  public List<Cases> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return casesService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Cases update(Cases cases) {
    return casesService.update(cases);
  }

  @Override
  public Cases findById(Integer id) {
    return casesService.findById(id);
  }

  @Override
  public List<Cases> findAllById(List<Integer> id) {
    return casesService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    casesService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    casesService.bulkDelete(list);
  }


		
   
   
   
   
}
