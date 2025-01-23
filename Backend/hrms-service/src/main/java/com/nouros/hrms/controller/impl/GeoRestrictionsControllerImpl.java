package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.GeoRestrictionsController;
import com.nouros.hrms.model.GeoRestrictions;
import com.nouros.hrms.service.GeoRestrictionsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the GeoRestrictionsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the GeoRestrictionsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the GeoRestrictionsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(GeoRestrictions GeoRestrictions): creates an GeoRestrictions and returns the newly created GeoRestrictions.
count(String filter): returns the number of GeoRestrictions that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of GeoRestrictions that match the specified filter, sorted according to the specified orderBy
and orderType.
update(GeoRestrictions GeoRestrictions): updates an GeoRestrictions and returns the updated GeoRestrictions.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of GeoRestrictions with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/GeoRestrictions")
//@Tag(name="/GeoRestrictions",tags="GeoRestrictions",description="GeoRestrictions")
public class GeoRestrictionsControllerImpl implements GeoRestrictionsController {

  private static final Logger log = LogManager.getLogger(GeoRestrictionsControllerImpl.class);

  @Autowired
  private GeoRestrictionsService geoRestrictionsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public GeoRestrictions create(GeoRestrictions geoRestrictions) {
	  log.info("inside @class GeoRestrictionsControllerImpl @method create");
    return geoRestrictionsService.create(geoRestrictions);
  }

  @Override
  public Long count(String filter) {
    return geoRestrictionsService.count(filter);
  }

  @Override
  public List<GeoRestrictions> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return geoRestrictionsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public GeoRestrictions update(GeoRestrictions geoRestrictions) {
    return geoRestrictionsService.update(geoRestrictions);
  }

  @Override
  public GeoRestrictions findById(Integer id) {
    return geoRestrictionsService.findById(id);
  }

  @Override
  public List<GeoRestrictions> findAllById(List<Integer> id) {
    return geoRestrictionsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    geoRestrictionsService.deleteById(id);
  }
  
 

   
   
   
   
}
