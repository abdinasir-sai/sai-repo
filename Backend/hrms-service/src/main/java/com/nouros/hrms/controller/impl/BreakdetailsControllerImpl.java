package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.BreakdetailsController;
import com.nouros.hrms.model.Breakdetails;
import com.nouros.hrms.service.BreakdetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the BreakdetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the BreakdetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the BreakdetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Breakdetails Breakdetails): creates an Breakdetails and returns the newly created Breakdetails.
count(String filter): returns the number of Breakdetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Breakdetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Breakdetails Breakdetails): updates an Breakdetails and returns the updated Breakdetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Breakdetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Breakdetails")

//@Tag(name="/Breakdetails",tags="Breakdetails",description="Breakdetails")
public class BreakdetailsControllerImpl implements BreakdetailsController {

  private static final Logger log = LogManager.getLogger(BreakdetailsControllerImpl.class);

  @Autowired
  private BreakdetailsService breakdetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Breakdetails create(Breakdetails breakdetails) {
	  log.info("inside @class BreakdetailsControllerImpl @method create");
    return breakdetailsService.create(breakdetails);
  }

  @Override
  public Long count(String filter) {
    return breakdetailsService.count(filter);
  }

  @Override
  public List<Breakdetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return breakdetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Breakdetails update(Breakdetails breakdetails) {
    return breakdetailsService.update(breakdetails);
  }

  @Override
  public Breakdetails findById(Integer id) {
    return breakdetailsService.findById(id);
  }

  @Override
  public List<Breakdetails> findAllById(List<Integer> id) {
    return breakdetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    breakdetailsService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    breakdetailsService.bulkDelete(list);
  }

		
   
   
   
   
}
