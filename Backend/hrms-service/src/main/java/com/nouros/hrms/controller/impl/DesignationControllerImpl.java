package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.DesignationController;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.service.DesignationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


/**

This class represents the implementation of the DesignationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the DesignationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the DesignationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Designation Designation): creates an Designation and returns the newly created Designation.
count(String filter): returns the number of Designation that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Designation that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Designation Designation): updates an Designation and returns the updated Designation.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Designation with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Designation")
//@Tag(name="/Designation",tags="Designation",description="Designation")
public class DesignationControllerImpl implements DesignationController {

  private static final Logger log = LogManager.getLogger(DesignationControllerImpl.class);

  @Autowired
  private DesignationService designationService;
  

	
  @Override
  @TriggerBPMN(entityName = "Designation", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE DESIGNATION")
  public Designation create(Designation designation) {
	  log.info("inside @class DesignationControllerImpl @method create");
    return designationService.create(designation);
  }

  @Override
  public Long count(String filter) {
    return designationService.count(filter);
  }

  @Override
  public List<Designation> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return designationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE DESIGNATION")
  public Designation update(Designation designation) {
    return designationService.update(designation);
  }

  @Override
  public Designation findById(Integer id) {
    return designationService.findById(id);
  }

  @Override
  public List<Designation> findAllById(List<Integer> id) {
    return designationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE DESIGNATION BY ID")
  public void deleteById(Integer id) {
    designationService.deleteById(id);
  }
  
 
  public List<Designation> getDesignationForSuccession()
  {
	  return designationService.getDesignationForSuccession();
  }
	
  @Override
public JSONObject getPromptAnswer(String designationName)
  {
	 return designationService.getJsonObjectForDesignation(designationName); 
  }

@Override
public Designation setJobLevelForDesignationRecursively(String designationName) {
	return designationService.setJobLevelForDesignationRecursively(designationName);
}
   
}
