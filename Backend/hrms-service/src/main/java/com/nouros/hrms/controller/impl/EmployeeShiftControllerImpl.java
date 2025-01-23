package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeShiftController;
import com.nouros.hrms.model.EmployeeShift;
import com.nouros.hrms.service.EmployeeShiftService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeShiftController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeShiftController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeShiftService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeShift EmployeeShift): creates an EmployeeShift and returns the newly created EmployeeShift.
count(String filter): returns the number of EmployeeShift that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeShift that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeShift EmployeeShift): updates an EmployeeShift and returns the updated EmployeeShift.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeShift with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeShift")
//@Tag(name="/EmployeeShift",tags="EmployeeShift",description="EmployeeShift")
public class EmployeeShiftControllerImpl implements EmployeeShiftController {

  private static final Logger log = LogManager.getLogger(EmployeeShiftControllerImpl.class);

  @Autowired
  private EmployeeShiftService employeeShiftService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeShift create(EmployeeShift employeeShift) {
	  log.info("inside @class EmployeeShiftControllerImpl @method create");
    return employeeShiftService.create(employeeShift);
  }

  @Override
  public Long count(String filter) {
    return employeeShiftService.count(filter);
  }

  @Override
  public List<EmployeeShift> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeShiftService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeShift update(EmployeeShift employeeShift) {
    return employeeShiftService.update(employeeShift);
  }

  @Override
  public EmployeeShift findById(Integer id) {
    return employeeShiftService.findById(id);
  }

  @Override
  public List<EmployeeShift> findAllById(List<Integer> id) {
    return employeeShiftService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeShiftService.deleteById(id);
  }
  


		
   
   
   
   
}
