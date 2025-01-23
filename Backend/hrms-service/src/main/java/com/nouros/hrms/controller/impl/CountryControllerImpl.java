package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CountryController;
import com.nouros.hrms.model.Country;
import com.nouros.hrms.service.CountryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CountryController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CountryController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CountryService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Country Country): creates an Country and returns the newly created Country.
count(String filter): returns the number of Country that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Country that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Country Country): updates an Country and returns the updated Country.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Country with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Country")
//@Tag(name="/Country",tags="Country",description="Country")
public class CountryControllerImpl implements CountryController {

  private static final Logger log = LogManager.getLogger(CountryControllerImpl.class);

  @Autowired
  private CountryService countryService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Country create(Country country) {
	  log.info("inside @class CountryControllerImpl @method create");
    return countryService.create(country);
  }

  @Override
  public Long count(String filter) {
    return countryService.count(filter);
  }

  @Override
  public List<Country> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return countryService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Country update(Country country) {
    return countryService.update(country);
  }

  @Override
  public Country findById(Integer id) {
    return countryService.findById(id);
  }

  @Override
  public List<Country> findAllById(List<Integer> id) {
    return countryService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    countryService.deleteById(id);
  }
  
 

		
   
   
   
   
}
