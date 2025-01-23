package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.GeneralSettingsLeaveController;
import com.nouros.hrms.model.GeneralSettingsLeave;
import com.nouros.hrms.service.GeneralSettingsLeaveService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the GeneralSettingsLeaveController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the GeneralSettingsLeaveController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the GeneralSettingsLeaveService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(GeneralSettingsLeave GeneralSettingsLeave): creates an GeneralSettingsLeave and returns the newly created GeneralSettingsLeave.
count(String filter): returns the number of GeneralSettingsLeave that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of GeneralSettingsLeave that match the specified filter, sorted according to the specified orderBy
and orderType.
update(GeneralSettingsLeave GeneralSettingsLeave): updates an GeneralSettingsLeave and returns the updated GeneralSettingsLeave.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of GeneralSettingsLeave with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/GeneralSettingsLeave")
//@Tag(name="/GeneralSettingsLeave",tags="GeneralSettingsLeave",description="GeneralSettingsLeave")
public class GeneralSettingsLeaveControllerImpl implements GeneralSettingsLeaveController {

  private static final Logger log = LogManager.getLogger(GeneralSettingsLeaveControllerImpl.class);

  @Autowired
  private GeneralSettingsLeaveService generalSettingsLeaveService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public GeneralSettingsLeave create(GeneralSettingsLeave generalSettingsLeave) {
	  log.info("inside @class GeneralSettingsLeaveControllerImpl @method create");
    return generalSettingsLeaveService.create(generalSettingsLeave);
  }

  @Override
  public Long count(String filter) {
    return generalSettingsLeaveService.count(filter);
  }

  @Override
  public List<GeneralSettingsLeave> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return generalSettingsLeaveService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public GeneralSettingsLeave update(GeneralSettingsLeave generalSettingsLeave) {
    return generalSettingsLeaveService.update(generalSettingsLeave);
  }

  @Override
  public GeneralSettingsLeave findById(Integer id) {
    return generalSettingsLeaveService.findById(id);
  }

  @Override
  public List<GeneralSettingsLeave> findAllById(List<Integer> id) {
    return generalSettingsLeaveService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    generalSettingsLeaveService.deleteById(id);
  }
  
  

		
   
   
   
   
}
