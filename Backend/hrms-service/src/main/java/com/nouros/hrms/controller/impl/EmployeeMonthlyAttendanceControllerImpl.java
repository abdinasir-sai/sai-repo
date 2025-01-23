package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeMonthlyAttendanceController;
import com.nouros.hrms.model.EmployeeMonthlyAttendance;
import com.nouros.hrms.service.EmployeeMonthlyAttendanceService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeMonthlyAttendanceController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeMonthlyAttendanceController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeMonthlyAttendanceService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeMonthlyAttendance EmployeeMonthlyAttendance): creates an EmployeeMonthlyAttendance and returns the newly created EmployeeMonthlyAttendance.
count(String filter): returns the number of EmployeeMonthlyAttendance that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeMonthlyAttendance that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeMonthlyAttendance EmployeeMonthlyAttendance): updates an EmployeeMonthlyAttendance and returns the updated EmployeeMonthlyAttendance.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeMonthlyAttendance with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeMonthlyAttendance")
//@Tag(name="/EmployeeMonthlyAttendance",tags="EmployeeMonthlyAttendance",description="EmployeeMonthlyAttendance")
public class EmployeeMonthlyAttendanceControllerImpl implements EmployeeMonthlyAttendanceController {

  private static final Logger log = LogManager.getLogger(EmployeeMonthlyAttendanceControllerImpl.class);

  @Autowired
  private EmployeeMonthlyAttendanceService employeeMonthlyAttendanceService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeMonthlyAttendance create(EmployeeMonthlyAttendance employeeMonthlyAttendance) {
	  log.info("inside @class EmployeeMonthlyAttendanceControllerImpl @method create");
    return employeeMonthlyAttendanceService.create(employeeMonthlyAttendance);
  }

  @Override
  public Long count(String filter) {
    return employeeMonthlyAttendanceService.count(filter);
  }

  @Override
  public List<EmployeeMonthlyAttendance> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeMonthlyAttendanceService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeMonthlyAttendance update(EmployeeMonthlyAttendance employeeMonthlyAttendance) {
    return employeeMonthlyAttendanceService.update(employeeMonthlyAttendance);
  }

  @Override
  public EmployeeMonthlyAttendance findById(Integer id) {
    return employeeMonthlyAttendanceService.findById(id);
  }

  @Override
  public List<EmployeeMonthlyAttendance> findAllById(List<Integer> id) {
    return employeeMonthlyAttendanceService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeMonthlyAttendanceService.deleteById(id);
  }
  
 

		
   
   
   
   
}
