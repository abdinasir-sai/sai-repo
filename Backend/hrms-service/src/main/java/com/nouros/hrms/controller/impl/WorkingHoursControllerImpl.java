package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.WorkingHoursController;
import com.nouros.hrms.model.WorkingHours;
import com.nouros.hrms.service.WorkingHoursService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the WorkingHoursController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the WorkingHoursController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the WorkingHoursService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(WorkingHours WorkingHours): creates an WorkingHours and returns the newly created WorkingHours.
count(String filter): returns the number of WorkingHours that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of WorkingHours that match the specified filter, sorted according to the specified orderBy
and orderType.
update(WorkingHours WorkingHours): updates an WorkingHours and returns the updated WorkingHours.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of WorkingHours with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/WorkingHours")
//@Tag(name="/WorkingHours",tags="WorkingHours",description="WorkingHours")
public class WorkingHoursControllerImpl implements WorkingHoursController {

  private static final Logger log = LogManager.getLogger(WorkingHoursControllerImpl.class);

  @Autowired
  private WorkingHoursService workingHoursService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public WorkingHours create(WorkingHours workingHours) {
	  log.info("inside @class WorkingHoursControllerImpl @method create");
    return workingHoursService.create(workingHours);
  }

  @Override
  public Long count(String filter) {
    return workingHoursService.count(filter);
  }

  @Override
  public List<WorkingHours> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return workingHoursService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public WorkingHours update(WorkingHours workingHours) {
    return workingHoursService.update(workingHours);
  }

  @Override
  public WorkingHours findById(Integer id) {
    return workingHoursService.findById(id);
  }

  @Override
  public List<WorkingHours> findAllById(List<Integer> id) {
    return workingHoursService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    workingHoursService.deleteById(id);
  }
  
 

		
   
   
   
   
}
