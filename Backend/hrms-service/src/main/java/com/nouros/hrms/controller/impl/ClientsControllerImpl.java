package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ClientsController;
import com.nouros.hrms.model.Clients;
import com.nouros.hrms.service.ClientsService;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**

This class represents the implementation of the ClientsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ClientsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ClientsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Clients Clients): creates an Clients and returns the newly created Clients.
count(String filter): returns the number of Clients that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Clients that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Clients Clients): updates an Clients and returns the updated Clients.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Clients with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Clients")

//@Tag(name="/Clients",tags="Clients",description="Clients")
public class ClientsControllerImpl implements ClientsController {

  private static final Logger log = LogManager.getLogger(ClientsControllerImpl.class);

  @Autowired
  private ClientsService clientsService;
  

	
  @TriggerBPMN(entityName = "Clients", appName = "HRMS_APP_NAME")
@Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Clients create(Clients clients) {
	  log.info("inside @class ClientsControllerImpl @method create");
    return clientsService.create(clients);
  }

  @Override
  public Long count(String filter) {
    return clientsService.count(filter);
  }

  @Override
  public List<Clients> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return clientsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Clients update(Clients clients) {
    return clientsService.update(clients);
  }

  @Override
  public Clients findById(Integer id) {
    return clientsService.findById(id);
  }

  @Override
  public List<Clients> findAllById(List<Integer> id) {
    return clientsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    clientsService.deleteById(id);
  }
  
 
			@Override
			public List<WorkflowActions> getActions(@Valid Integer id) {
				 return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id,"Clients");
			}
   
   
   
   
   
}
