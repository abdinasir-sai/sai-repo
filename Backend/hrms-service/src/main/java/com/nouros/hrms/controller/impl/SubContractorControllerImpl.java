package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.SubContractorController;
import com.nouros.hrms.model.SubContractor;
import com.nouros.hrms.service.SubContractorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the SubContractorController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the SubContractorController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the SubContractorService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(SubContractor SubContractor): creates an SubContractor and returns the newly created SubContractor.
count(String filter): returns the number of SubContractor that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of SubContractor that match the specified filter, sorted according to the specified orderBy
and orderType.
update(SubContractor SubContractor): updates an SubContractor and returns the updated SubContractor.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of SubContractor with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/SubContractor")
//@Tag(name="/SubContractor",tags="SubContractor",description="SubContractor")
public class SubContractorControllerImpl implements SubContractorController {

  private static final Logger log = LogManager.getLogger(SubContractorControllerImpl.class);

  @Autowired
  private SubContractorService subContractorService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public SubContractor create(SubContractor subContractor) {
	  log.info("inside @class SubContractorControllerImpl @method create");
    return subContractorService.create(subContractor);
  }

  @Override
  public Long count(String filter) {
    return subContractorService.count(filter);
  }

  @Override
  public List<SubContractor> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return subContractorService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public SubContractor update(SubContractor subContractor) {
    return subContractorService.update(subContractor);
  }

  @Override
  public SubContractor findById(Integer id) {
    return subContractorService.findById(id);
  }

  @Override
  public List<SubContractor> findAllById(List<Integer> id) {
    return subContractorService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    subContractorService.deleteById(id);
  }
  
  
   
}
