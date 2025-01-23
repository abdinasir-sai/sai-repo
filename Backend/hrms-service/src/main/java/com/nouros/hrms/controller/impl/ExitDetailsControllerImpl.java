package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ExitDetailsController;
import com.nouros.hrms.model.ExitDetails;
import com.nouros.hrms.service.ExitDetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ExitDetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ExitDetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ExitDetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(ExitDetails ExitDetails): creates an ExitDetails and returns the newly created ExitDetails.
count(String filter): returns the number of ExitDetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of ExitDetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(ExitDetails ExitDetails): updates an ExitDetails and returns the updated ExitDetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of ExitDetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/ExitDetails")
//@Tag(name="/ExitDetails",tags="ExitDetails",description="ExitDetails")
public class ExitDetailsControllerImpl implements ExitDetailsController {

  private static final Logger log = LogManager.getLogger(ExitDetailsControllerImpl.class);

  @Autowired
  private ExitDetailsService exitDetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public ExitDetails create(ExitDetails exitDetails) {
	  log.info("inside @class ExitDetailsControllerImpl @method create");
    return exitDetailsService.create(exitDetails);
  }

  @Override
  public Long count(String filter) {
    return exitDetailsService.count(filter);
  }

  @Override
  public List<ExitDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return exitDetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public ExitDetails update(ExitDetails exitDetails) {
    return exitDetailsService.update(exitDetails);
  }

  @Override
  public ExitDetails findById(Integer id) {
    return exitDetailsService.findById(id);
  }

  @Override
  public List<ExitDetails> findAllById(List<Integer> id) {
    return exitDetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    exitDetailsService.deleteById(id);
  }
  
 

		
   
   
   
   
}
