package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RiskSpecialComponentController;
import com.nouros.hrms.model.RiskSpecialComponent;
import com.nouros.hrms.service.RiskSpecialComponentService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the RiskSpecialComponentController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskSpecialComponentController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskSpecialComponentService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(RiskSpecialComponent RiskSpecialComponent): creates an RiskSpecialComponent and returns the newly created RiskSpecialComponent.
count(String filter): returns the number of RiskSpecialComponent that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of RiskSpecialComponent that match the specified filter, sorted according to the specified orderBy
and orderType.
update(RiskSpecialComponent RiskSpecialComponent): updates an RiskSpecialComponent and returns the updated RiskSpecialComponent.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of RiskSpecialComponent with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/RiskSpecialComponent")
//@Tag(name="/RiskSpecialComponent",tags="RiskSpecialComponent",description="RiskSpecialComponent")
public class RiskSpecialComponentControllerImpl implements RiskSpecialComponentController {


  private static final Logger log = LogManager.getLogger(RiskSpecialComponentControllerImpl.class);
  @Autowired
  private RiskSpecialComponentService riskSpecialComponentService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public RiskSpecialComponent create(RiskSpecialComponent riskSpecialComponent) {
	  log.info("inside @class RiskSpecialComponentControllerImpl @method create");
    return riskSpecialComponentService.create(riskSpecialComponent);
  }

  @Override
  public Long count(String filter) {
    return riskSpecialComponentService.count(filter);
  }

  @Override
  public List<RiskSpecialComponent> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskSpecialComponentService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public RiskSpecialComponent update(RiskSpecialComponent riskSpecialComponent) {
    return riskSpecialComponentService.update(riskSpecialComponent);
  }

  @Override
  public RiskSpecialComponent findById(Integer id) {
    return riskSpecialComponentService.findById(id);
  }

  @Override
  public List<RiskSpecialComponent> findAllById(List<Integer> id) {
    return riskSpecialComponentService.findAllById(id);
  }

  
   
   
}
