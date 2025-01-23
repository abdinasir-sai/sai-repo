package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CityController;
import com.nouros.hrms.model.City;
import com.nouros.hrms.service.CityService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CityController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CityController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CityService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(City City): creates an City and returns the newly created City.
count(String filter): returns the number of City that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of City that match the specified filter, sorted according to the specified orderBy
and orderType.
update(City City): updates an City and returns the updated City.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of City with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/City")

//@Tag(name="/City",tags="City",description="City")
public class CityControllerImpl implements CityController {

  private static final Logger log = LogManager.getLogger(CityControllerImpl.class);

  @Autowired
  private CityService cityService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public City create(City city) {
	  log.info("inside @class CityControllerImpl @method create");
    return cityService.create(city);
  }

  @Override
  public Long count(String filter) {
    return cityService.count(filter);
  }

  @Override
  public List<City> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return cityService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public City update(City city) {
    return cityService.update(city);
  }

  @Override
  public City findById(Integer id) {
    return cityService.findById(id);
  }

  @Override
  public List<City> findAllById(List<Integer> id) {
    return cityService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    cityService.deleteById(id);
  }
  
  

   
}
