package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeeducationdetailsController;
import com.nouros.hrms.model.Employeeeducationdetails;
import com.nouros.hrms.service.EmployeeeducationdetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeeducationdetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeeducationdetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeeducationdetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Employeeeducationdetails Employeeeducationdetails): creates an Employeeeducationdetails and returns the newly created Employeeeducationdetails.
count(String filter): returns the number of Employeeeducationdetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Employeeeducationdetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Employeeeducationdetails Employeeeducationdetails): updates an Employeeeducationdetails and returns the updated Employeeeducationdetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Employeeeducationdetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Employeeeducationdetails")
//@Tag(name="/Employeeeducationdetails",tags="Employeeeducationdetails",description="Employeeeducationdetails")
public class EmployeeeducationdetailsControllerImpl implements EmployeeeducationdetailsController {

  private static final Logger log = LogManager.getLogger(EmployeeeducationdetailsControllerImpl.class);

  @Autowired
  private EmployeeeducationdetailsService employeeeducationdetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Employeeeducationdetails create(Employeeeducationdetails employeeeducationdetails) {
	  log.info("inside @class EmployeeeducationdetailsControllerImpl @method create");
    return employeeeducationdetailsService.create(employeeeducationdetails);
  }

  @Override
  public Long count(String filter) {
    return employeeeducationdetailsService.count(filter);
  }

  @Override
  public List<Employeeeducationdetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeeducationdetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Employeeeducationdetails update(Employeeeducationdetails employeeeducationdetails) {
    return employeeeducationdetailsService.update(employeeeducationdetails);
  }

  @Override
  public Employeeeducationdetails findById(Integer id) {
    return employeeeducationdetailsService.findById(id);
  }

  @Override
  public List<Employeeeducationdetails> findAllById(List<Integer> id) {
    return employeeeducationdetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeeducationdetailsService.deleteById(id);
  }
  
 
}
