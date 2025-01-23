package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ShiftsController;
import com.nouros.hrms.model.Shifts;
import com.nouros.hrms.service.ShiftsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ShiftsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ShiftsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ShiftsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Shifts Shifts): creates an Shifts and returns the newly created Shifts.
count(String filter): returns the number of Shifts that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Shifts that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Shifts Shifts): updates an Shifts and returns the updated Shifts.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Shifts with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Shifts")
//@Tag(name="/Shifts",tags="Shifts",description="Shifts")
public class ShiftsControllerImpl implements ShiftsController {

  private static final Logger log = LogManager.getLogger(ShiftsControllerImpl.class);
  @Autowired
  private ShiftsService shiftsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Shifts create(Shifts shifts) {
	  log.info("inside @class ShiftsControllerImpl @method create");
    return shiftsService.create(shifts);
  }

  @Override
  public Long count(String filter) {
    return shiftsService.count(filter);
  }

  @Override
  public List<Shifts> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return shiftsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Shifts update(Shifts shifts) {
    return shiftsService.update(shifts);
  }

  @Override
  public Shifts findById(Integer id) {
    return shiftsService.findById(id);
  }

  @Override
  public List<Shifts> findAllById(List<Integer> id) {
    return shiftsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    shiftsService.deleteById(id);
  }
  
    
   
   
   
}
