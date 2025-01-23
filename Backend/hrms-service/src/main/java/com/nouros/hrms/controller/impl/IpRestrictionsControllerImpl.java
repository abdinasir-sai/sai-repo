package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.IpRestrictionsController;
import com.nouros.hrms.model.IpRestrictions;
import com.nouros.hrms.service.IpRestrictionsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the IpRestrictionsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the IpRestrictionsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the IpRestrictionsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(IpRestrictions IpRestrictions): creates an IpRestrictions and returns the newly created IpRestrictions.
count(String filter): returns the number of IpRestrictions that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of IpRestrictions that match the specified filter, sorted according to the specified orderBy
and orderType.
update(IpRestrictions IpRestrictions): updates an IpRestrictions and returns the updated IpRestrictions.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of IpRestrictions with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/IpRestrictions")
//@Tag(name="/IpRestrictions",tags="IpRestrictions",description="IpRestrictions")
public class IpRestrictionsControllerImpl implements IpRestrictionsController {

  private static final Logger log = LogManager.getLogger(IpRestrictionsControllerImpl.class);

  @Autowired
  private IpRestrictionsService ipRestrictionsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public IpRestrictions create(IpRestrictions ipRestrictions) {
	  log.info("inside @class IpRestrictionsControllerImpl @method create");
    return ipRestrictionsService.create(ipRestrictions);
  }

  @Override
  public Long count(String filter) {
    return ipRestrictionsService.count(filter);
  }

  @Override
  public List<IpRestrictions> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return ipRestrictionsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public IpRestrictions update(IpRestrictions ipRestrictions) {
    return ipRestrictionsService.update(ipRestrictions);
  }

  @Override
  public IpRestrictions findById(Integer id) {
    return ipRestrictionsService.findById(id);
  }

  @Override
  public List<IpRestrictions> findAllById(List<Integer> id) {
    return ipRestrictionsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    ipRestrictionsService.deleteById(id);
  }
  
 

		
   
   
   
   
}
