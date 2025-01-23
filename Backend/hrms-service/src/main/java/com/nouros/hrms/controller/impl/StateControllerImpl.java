package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.StateController;
import com.nouros.hrms.model.State;
import com.nouros.hrms.service.StateService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the StateController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the StateController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the StateService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(State State): creates an State and returns the newly created State.
count(String filter): returns the number of State that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of State that match the specified filter, sorted according to the specified orderBy
and orderType.
update(State State): updates an State and returns the updated State.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of State with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/State")
//@Tag(name="/State",tags="State",description="State")
public class StateControllerImpl implements StateController {

  private static final Logger log = LogManager.getLogger(StateControllerImpl.class);

  @Autowired
  private StateService stateService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public State create(State state) {
	  log.info("inside @class StateControllerImpl @method create");
    return stateService.create(state);
  }

  @Override
  public Long count(String filter) {
    return stateService.count(filter);
  }

  @Override
  public List<State> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return stateService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public State update(State state) {
    return stateService.update(state);
  }

  @Override
  public State findById(Integer id) {
    return stateService.findById(id);
  }

  @Override
  public List<State> findAllById(List<Integer> id) {
    return stateService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    stateService.deleteById(id);
  }
  
  
		
   
   
   
   
}
