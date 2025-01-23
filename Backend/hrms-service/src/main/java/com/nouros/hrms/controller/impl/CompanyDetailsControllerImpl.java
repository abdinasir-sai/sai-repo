package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CompanyDetailsController;
import com.nouros.hrms.model.CompanyDetails;
import com.nouros.hrms.service.CompanyDetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CompanyDetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CompanyDetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CompanyDetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(CompanyDetails CompanyDetails): creates an CompanyDetails and returns the newly created CompanyDetails.
count(String filter): returns the number of CompanyDetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of CompanyDetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(CompanyDetails CompanyDetails): updates an CompanyDetails and returns the updated CompanyDetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of CompanyDetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/CompanyDetails")

//@Tag(name="/CompanyDetails",tags="CompanyDetails",description="CompanyDetails")
public class CompanyDetailsControllerImpl implements CompanyDetailsController {

  private static final Logger log = LogManager.getLogger(CompanyDetailsControllerImpl.class);

  @Autowired
  private CompanyDetailsService companyDetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public CompanyDetails create(CompanyDetails companyDetails) {
	  log.info("inside @class CompanyDetailsControllerImpl @method create");
    return companyDetailsService.create(companyDetails);
  }

  @Override
  public Long count(String filter) {
    return companyDetailsService.count(filter);
  }

  @Override
  public List<CompanyDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return companyDetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public CompanyDetails update(CompanyDetails companyDetails) {
    return companyDetailsService.update(companyDetails);
  }

  @Override
  public CompanyDetails findById(Integer id) {
    return companyDetailsService.findById(id);
  }

  @Override
  public List<CompanyDetails> findAllById(List<Integer> id) {
    return companyDetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    companyDetailsService.deleteById(id);
  }
  
 
   
   
}
