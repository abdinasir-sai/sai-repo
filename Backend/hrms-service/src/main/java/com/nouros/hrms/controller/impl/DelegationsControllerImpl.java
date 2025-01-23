package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.DelegationsController;
import com.nouros.hrms.model.Delegations;
import com.nouros.hrms.service.DelegationsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the DelegationsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the DelegationsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the DelegationsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Delegations Delegations): creates an Delegations and returns the newly created Delegations.
count(String filter): returns the number of Delegations that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Delegations that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Delegations Delegations): updates an Delegations and returns the updated Delegations.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Delegations with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Delegations")
//@Tag(name="/Delegations",tags="Delegations",description="Delegations")
public class DelegationsControllerImpl implements DelegationsController {

  private static final Logger log = LogManager.getLogger(DelegationsControllerImpl.class);

  @Autowired
  private DelegationsService delegationsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Delegations create(Delegations delegations) {
	  log.info("inside @class DelegationsControllerImpl @method create");
    return delegationsService.create(delegations);
  }

  @Override
  public Long count(String filter) {
    return delegationsService.count(filter);
  }

  @Override
  public List<Delegations> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return delegationsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Delegations update(Delegations delegations) {
    return delegationsService.update(delegations);
  }

  @Override
  public Delegations findById(Integer id) {
    return delegationsService.findById(id);
  }

  @Override
  public List<Delegations> findAllById(List<Integer> id) {
    return delegationsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    delegationsService.deleteById(id);
  }
  

		
   
   
   
   
}
