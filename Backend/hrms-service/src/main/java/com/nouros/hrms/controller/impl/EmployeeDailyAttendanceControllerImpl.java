package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeDailyAttendanceController;
import com.nouros.hrms.model.EmployeeDailyAttendance;
import com.nouros.hrms.service.EmployeeDailyAttendanceService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeDailyAttendanceController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeDailyAttendanceController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeDailyAttendanceService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeDailyAttendance EmployeeDailyAttendance): creates an EmployeeDailyAttendance and returns the newly created EmployeeDailyAttendance.
count(String filter): returns the number of EmployeeDailyAttendance that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeDailyAttendance that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeDailyAttendance EmployeeDailyAttendance): updates an EmployeeDailyAttendance and returns the updated EmployeeDailyAttendance.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeDailyAttendance with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeDailyAttendance")
//@Tag(name="/EmployeeDailyAttendance",tags="EmployeeDailyAttendance",description="EmployeeDailyAttendance")
public class EmployeeDailyAttendanceControllerImpl implements EmployeeDailyAttendanceController {

  private static final Logger log = LogManager.getLogger(EmployeeDailyAttendanceControllerImpl.class);

  @Autowired
  private EmployeeDailyAttendanceService employeeDailyAttendanceService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeDailyAttendance create(EmployeeDailyAttendance employeeDailyAttendance) {
	  log.info("inside create method ");
    return employeeDailyAttendanceService.create(employeeDailyAttendance);
  }

  @Override
  public Long count(String filter) {
    return employeeDailyAttendanceService.count(filter);
  }

  @Override
  public List<EmployeeDailyAttendance> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeDailyAttendanceService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeDailyAttendance update(EmployeeDailyAttendance employeeDailyAttendance) {
    return employeeDailyAttendanceService.update(employeeDailyAttendance);
  }

  @Override
  public EmployeeDailyAttendance findById(Integer id) {
    return employeeDailyAttendanceService.findById(id);
  }

  @Override
  public List<EmployeeDailyAttendance> findAllById(List<Integer> id) {
    return employeeDailyAttendanceService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeDailyAttendanceService.deleteById(id);
  }
   
  @Override
   public Boolean addEmployeeLeaveByProcessInstanceId(String processInstanceId)
   {
	  return employeeDailyAttendanceService.addLeaveforEmployee(processInstanceId);
   }
  
}
