package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CompensatoryRequestSchedulerController;
import com.nouros.hrms.model.CompensatoryRequestScheduler;
import com.nouros.hrms.service.CompensatoryRequestSchedulerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CompensatoryRequestSchedulerController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CompensatoryRequestSchedulerController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CompensatoryRequestSchedulerService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(CompensatoryRequestScheduler CompensatoryRequestScheduler): creates an CompensatoryRequestScheduler and returns the newly created CompensatoryRequestScheduler.
count(String filter): returns the number of CompensatoryRequestScheduler that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of CompensatoryRequestScheduler that match the specified filter, sorted according to the specified orderBy
and orderType.
update(CompensatoryRequestScheduler CompensatoryRequestScheduler): updates an CompensatoryRequestScheduler and returns the updated CompensatoryRequestScheduler.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of CompensatoryRequestScheduler with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/CompensatoryRequestScheduler")

//@Tag(name="/CompensatoryRequestScheduler",tags="CompensatoryRequestScheduler",description="CompensatoryRequestScheduler")
public class CompensatoryRequestSchedulerControllerImpl implements CompensatoryRequestSchedulerController {

  private static final Logger log = LogManager.getLogger(CompensatoryRequestSchedulerControllerImpl.class);

  @Autowired
  private CompensatoryRequestSchedulerService compensatoryRequestSchedulerService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public CompensatoryRequestScheduler create(CompensatoryRequestScheduler compensatoryRequestScheduler) {
	  log.info("inside @class CompensatoryRequestSchedulerControllerImpl @method create");
    return compensatoryRequestSchedulerService.create(compensatoryRequestScheduler);
  }

  @Override
  public Long count(String filter) {
    return compensatoryRequestSchedulerService.count(filter);
  }

  @Override
  public List<CompensatoryRequestScheduler> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return compensatoryRequestSchedulerService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public CompensatoryRequestScheduler update(CompensatoryRequestScheduler compensatoryRequestScheduler) {
    return compensatoryRequestSchedulerService.update(compensatoryRequestScheduler);
  }

  @Override
  public CompensatoryRequestScheduler findById(Integer id) {
    return compensatoryRequestSchedulerService.findById(id);
  }

  @Override
  public List<CompensatoryRequestScheduler> findAllById(List<Integer> id) {
    return compensatoryRequestSchedulerService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    compensatoryRequestSchedulerService.deleteById(id);
  }
  
  

		
   
   
   
   
}
