package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeNationalIdentificationController;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.service.EmployeeNationalIdentificationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeNationalIdentificationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeNationalIdentificationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeNationalIdentificationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeNationalIdentification EmployeeNationalIdentification): creates an EmployeeNationalIdentification and returns the newly created EmployeeNationalIdentification.
count(String filter): returns the number of EmployeeNationalIdentification that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeNationalIdentification that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeNationalIdentification EmployeeNationalIdentification): updates an EmployeeNationalIdentification and returns the updated EmployeeNationalIdentification.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeNationalIdentification with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeNationalIdentification")
//@Tag(name="/EmployeeNationalIdentification",tags="EmployeeNationalIdentification",description="EmployeeNationalIdentification")
public class EmployeeNationalIdentificationControllerImpl implements EmployeeNationalIdentificationController {

  private static final Logger log = LogManager.getLogger(EmployeeNationalIdentificationControllerImpl.class);

  @Autowired
  private EmployeeNationalIdentificationService employeeNationalIdentificationService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE NATIONAL IDENTIFICATION")
  public EmployeeNationalIdentification create(EmployeeNationalIdentification employeeNationalIdentification) {
	  log.info("inside @class EmployeeNationalIdentificationControllerImpl @method create");
    return employeeNationalIdentificationService.create(employeeNationalIdentification);
  }

  @Override
  public Long count(String filter) {
    return employeeNationalIdentificationService.count(filter);
  }

  @Override
  public List<EmployeeNationalIdentification> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeNationalIdentificationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE NATIONAL IDENTIFICATION")
  public EmployeeNationalIdentification update(EmployeeNationalIdentification employeeNationalIdentification) {
    return employeeNationalIdentificationService.update(employeeNationalIdentification);
  }

  @Override
  public EmployeeNationalIdentification findById(Integer id) {
    return employeeNationalIdentificationService.findById(id);
  }

  @Override
  public List<EmployeeNationalIdentification> findAllById(List<Integer> id) {
    return employeeNationalIdentificationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE NATIONAL IDENTIFICATION")
  public void deleteById(Integer id) {
    employeeNationalIdentificationService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "BULK DELETE EMPLOYEE NATIONAL IDENTIFICATION")
  public void bulkDelete(List<Integer> list) {
    employeeNationalIdentificationService.softBulkDelete(list);
  }

@Override
public List<EmployeeNationalIdentification> getSelfEmployeeNationalIdentification(Integer id,Integer userId) {
	return employeeNationalIdentificationService.getSelfEmployeeNationalIdentification(id,userId);
}

@Override
@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE NATIONAL IDENTIFICATION")
public List<EmployeeNationalIdentification> updateEmployeeNationalIdentification(
		List<EmployeeNationalIdentification> employeeNationalIdentificationList) {
	return employeeNationalIdentificationService.updateEmployeeNationalIdentification(employeeNationalIdentificationList);
}

		
   
   
   
   
}
