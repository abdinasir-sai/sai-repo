package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.DepartmentController;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.service.DepartmentService;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the DepartmentController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the DepartmentController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the DepartmentService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Department Department): creates an Department and returns the newly created Department.
count(String filter): returns the number of Department that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Department that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Department Department): updates an Department and returns the updated Department.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Department with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Department")
//@Tag(name="/Department",tags="Department",description="Department")
public class DepartmentControllerImpl implements DepartmentController {

  private static final Logger log = LogManager.getLogger(DepartmentControllerImpl.class);

  @Autowired
  private DepartmentService departmentService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Department create(Department department) {
	  log.info("inside @class DepartmentControllerImpl @method create");
    return departmentService.create(department);
  }

  @Override
  public Long count(String filter) {
    return departmentService.count(filter);
  }

  @Override
  public List<Department> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return departmentService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Department update(Department department) {
    return departmentService.update(department);
  }

  @Override
  public Department findById(Integer id) {
    return departmentService.findById(id);
  }
  
  @Override
  public  List<DepartmentDetailsWrapper> getAllChildDepartments(String name) {
    return departmentService.getAllChildDepartments(name);
  }

  @Override
  public List<Department> findAllById(List<Integer> id) {
    return departmentService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    departmentService.deleteById(id);
  }
  

  @Override
  public Integer sendNotificationtToDepartmentLeads()
  {
	 return departmentService.sendNotificationToSpecificDepartment();
	 
  }

		
   
   
   
   
}
