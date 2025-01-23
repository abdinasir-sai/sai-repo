package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.HrLetterController;
import com.nouros.hrms.model.HrLetter;
import com.nouros.hrms.service.HrLetterService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the HrLetterController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the HrLetterController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the HrLetterService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(HrLetter HrLetter): creates an HrLetter and returns the newly created HrLetter.
count(String filter): returns the number of HrLetter that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of HrLetter that match the specified filter, sorted according to the specified orderBy
and orderType.
update(HrLetter HrLetter): updates an HrLetter and returns the updated HrLetter.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of HrLetter with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/HrLetter")
//@Tag(name="/HrLetter",tags="HrLetter",description="HrLetter")
public class HrLetterControllerImpl implements HrLetterController {

  private static final Logger log = LogManager.getLogger(HrLetterControllerImpl.class);

  @Autowired
  private HrLetterService hrLetterService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public HrLetter create(HrLetter hrLetter) {
	  log.info("inside @class HrLetterControllerImpl @method create");
    return hrLetterService.create(hrLetter);
  }

  @Override
  public Long count(String filter) {
    return hrLetterService.count(filter);
  }

  @Override
  public List<HrLetter> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return hrLetterService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public HrLetter update(HrLetter hrLetter) {
    return hrLetterService.update(hrLetter);
  }

  @Override
  public HrLetter findById(Integer id) {
    return hrLetterService.findById(id);
  }

  @Override
  public List<HrLetter> findAllById(List<Integer> id) {
    return hrLetterService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    hrLetterService.deleteById(id);
  }
  
 		
   
   
   
   
}
