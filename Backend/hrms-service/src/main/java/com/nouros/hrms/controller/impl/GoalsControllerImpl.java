package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.GoalsController;
import com.nouros.hrms.model.Goals;
import com.nouros.hrms.service.GoalsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the GoalsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the GoalsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the GoalsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Goals Goals): creates an Goals and returns the newly created Goals.
count(String filter): returns the number of Goals that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Goals that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Goals Goals): updates an Goals and returns the updated Goals.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Goals with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Goals")
//@Tag(name="/Goals",tags="Goals",description="Goals")
public class GoalsControllerImpl implements GoalsController {

  private static final Logger log = LogManager.getLogger(GoalsControllerImpl.class);

  @Autowired
  private GoalsService goalsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Goals create(Goals goals) {
	  log.info("inside @class GoalsControllerImpl @method create");
    return goalsService.create(goals);
  }

  @Override
  public Long count(String filter) {
    return goalsService.count(filter);
  }

  @Override
  public List<Goals> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return goalsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Goals update(Goals goals) {
    return goalsService.update(goals);
  }

  @Override
  public Goals findById(Integer id) {
    return goalsService.findById(id);
  }

  @Override
  public List<Goals> findAllById(List<Integer> id) {
    return goalsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    goalsService.deleteById(id);
  }
  

		
   
   
   
   
}
