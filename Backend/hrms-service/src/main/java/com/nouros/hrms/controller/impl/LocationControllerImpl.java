package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.LocationController;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.service.LocationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the LocationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the LocationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the LocationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Location Location): creates an Location and returns the newly created Location.
count(String filter): returns the number of Location that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Location that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Location Location): updates an Location and returns the updated Location.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Location with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Location")
//@Tag(name="/Location",tags="Location",description="Location")
public class LocationControllerImpl implements LocationController {

  private static final Logger log = LogManager.getLogger(LocationControllerImpl.class);

  @Autowired
  private LocationService locationService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE LOCATION")
  public Location create(Location location) {
	  log.info("inside @class LocationControllerImpl @method create");
    return locationService.create(location);
  }

  @Override
  public Long count(String filter) {
    return locationService.count(filter);
  }

  @Override
  public List<Location> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return locationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE LOCATION")
  public Location update(Location location) {
    return locationService.update(location);
  }

  @Override
  public Location findById(Integer id) {
    return locationService.findById(id);
  }

  @Override
  public List<Location> findAllById(List<Integer> id) {
    return locationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE LOCATION BY ID")
  public void deleteById(Integer id) {
    locationService.deleteById(id);
  }
  
 

   
   
}
