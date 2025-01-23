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
import com.nouros.hrms.controller.TimeSheetController;
import com.nouros.hrms.model.TimeSheet;
import com.nouros.hrms.service.TimeSheetService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the TimeSheetController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the TimeSheetController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the TimeSheetService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(TimeSheet TimeSheet): creates an TimeSheet and returns the newly created TimeSheet.
count(String filter): returns the number of TimeSheet that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of TimeSheet that match the specified filter, sorted according to the specified orderBy
and orderType.
update(TimeSheet TimeSheet): updates an TimeSheet and returns the updated TimeSheet.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of TimeSheet with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/TimeSheet")
//@Tag(name="/TimeSheet",tags="TimeSheet",description="TimeSheet")
public class TimeSheetControllerImpl implements TimeSheetController {

  private static final Logger log = LogManager.getLogger(TimeSheetControllerImpl.class);

  @Autowired
  private TimeSheetService timeSheetService;
  

	
  @Override
  @TriggerBPMN(entityName = "TimeSheet", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE TIME SHEET")
  public TimeSheet create(TimeSheet timeSheet) {
	  log.info("inside @class TimeSheetControllerImpl @method create");
    return timeSheetService.create(timeSheet);
  }

  @Override
  public Long count(String filter) {
    return timeSheetService.count(filter);
  }

  @Override
  public List<TimeSheet> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return timeSheetService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE TIME SHEET")
  public TimeSheet update(TimeSheet timeSheet) {
    return timeSheetService.update(timeSheet);
  }

  @Override
  public TimeSheet findById(Integer id) {
    return timeSheetService.findById(id);
  }

  @Override
  public List<TimeSheet> findAllById(List<Integer> id) {
    return timeSheetService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE TIME SHEET BY ID")
  public void deleteById(Integer id) {
    timeSheetService.deleteById(id);
  }
  

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE TIME SHEET & TIME LOGS BY ID")
  public String deleteTimeLogsAndTimeSheetById(Integer id) {
    return timeSheetService.deleteTimeLogsAndTimeSheetById(id);
  }

		
   
   
   
   
}
