package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.TimeLogsController;
import com.nouros.hrms.model.TimeLogs;
import com.nouros.hrms.service.TimeLogsService;
import com.nouros.hrms.wrapper.TimeSheetWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the TimeLogsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the TimeLogsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the TimeLogsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(TimeLogs TimeLogs): creates an TimeLogs and returns the newly created TimeLogs.
count(String filter): returns the number of TimeLogs that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of TimeLogs that match the specified filter, sorted according to the specified orderBy
and orderType.
update(TimeLogs TimeLogs): updates an TimeLogs and returns the updated TimeLogs.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of TimeLogs with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/TimeLogs")

//@Tag(name="/TimeLogs",tags="TimeLogs",description="TimeLogs")
public class TimeLogsControllerImpl implements TimeLogsController {

  private static final Logger log = LogManager.getLogger(TimeLogsControllerImpl.class);

  @Autowired
  private TimeLogsService timeLogsService; 
  

	
  @Override
  @TriggerBPMN(entityName = "TimeLogs", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE TIME LOGS")
  public TimeLogs create(TimeLogs timeLogs) {
	  log.info("inside @class TimeLogsControllerImpl @method create");
    return timeLogsService.create(timeLogs);
  }

  @Override
  public Long count(String filter) {
    return timeLogsService.count(filter);
  }

  @Override
  public List<TimeLogs> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return timeLogsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE TIME LOGS")
  public TimeLogs update(TimeLogs timeLogs) {
    return timeLogsService.update(timeLogs);
  }

  @Override
  public TimeLogs findById(Integer id) {
    return timeLogsService.findById(id);
  }

  @Override
  public List<TimeLogs> findAllById(List<Integer> id) {
    return timeLogsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE TIME LOGS BY ID")
  public void deleteById(Integer id) {
    timeLogsService.deleteById(id);
  }

    
    @Override
    public ResponseEntity<Double> calculateHours(@PathVariable Integer timeLogsId) {
            Double hours = timeLogsService.calculateHours(timeLogsId);
            return ResponseEntity.ok(hours);
       }
    
    @Override 
    public List<TimeLogs> getTimeLogsByEmpIdAndWeekNumber(Integer empId,Integer weeks){
    	return timeLogsService.getTimeLogsByEmpIdAndWeekNumber(empId,weeks);
    }
    
    @Override 
    public ResponseEntity<String>  getTimeLogsDetails(Integer empId,Integer weeks){
    	return  ResponseEntity.ok(timeLogsService.getTimeLogsDetails(empId,weeks));
    }
    
    @Override 
    public ResponseEntity<String>  getTimeLogsDetailsByTimeSheet(Integer timeSheetId){
    	return  ResponseEntity.ok(timeLogsService.getTimeLogsDetailsByTimeSheet(timeSheetId));
    }
    
    @Override 
    public List<TimeLogs> createOrUpdateTimeLogs(TimeSheetWrapper timeSheetWrapper){
    	return timeLogsService.createOrUpdateTimeLogs(timeSheetWrapper);
    }
    
    }
   

