package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.LeaveTypeController;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.service.LeaveTypeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the LeaveTypeController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the LeaveTypeController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the LeaveTypeService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(LeaveType LeaveType): creates an LeaveType and returns the newly created LeaveType.
count(String filter): returns the number of LeaveType that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of LeaveType that match the specified filter, sorted according to the specified orderBy
and orderType.
update(LeaveType LeaveType): updates an LeaveType and returns the updated LeaveType.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of LeaveType with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/LeaveType")
//@Tag(name="/LeaveType",tags="LeaveType",description="LeaveType")
public class LeaveTypeControllerImpl implements LeaveTypeController {

  private static final Logger log = LogManager.getLogger(LeaveTypeControllerImpl.class);

  @Autowired
  private LeaveTypeService leaveTypeService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public LeaveType create(LeaveType leaveType) {
	  log.info("inside @class LeaveTypeControllerImpl @method create");
    return leaveTypeService.create(leaveType);
  }

  @Override
  public Long count(String filter) {
    return leaveTypeService.count(filter);
  }

  @Override
  public List<LeaveType> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return leaveTypeService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public LeaveType update(LeaveType leaveType) {
    return leaveTypeService.update(leaveType);
  }

  @Override
  public LeaveType findById(Integer id) {
    return leaveTypeService.findById(id);
  }

  @Override
  public List<LeaveType> findAllById(List<Integer> id) {
    return leaveTypeService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    leaveTypeService.deleteById(id);
  }
  
 
		
   
   
   
   
}
