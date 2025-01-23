package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RiskCustomFieldController;
import com.nouros.hrms.model.RiskCustomField;
import com.nouros.hrms.service.RiskCustomFieldService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the RiskCustomFieldController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskCustomFieldController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskCustomFieldService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(RiskCustomField RiskCustomField): creates an RiskCustomField and returns the newly created RiskCustomField.
count(String filter): returns the number of RiskCustomField that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of RiskCustomField that match the specified filter, sorted according to the specified orderBy
and orderType.
update(RiskCustomField RiskCustomField): updates an RiskCustomField and returns the updated RiskCustomField.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of RiskCustomField with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/RiskCustomField")
//@Tag(name="/RiskCustomField",tags="RiskCustomField",description="RiskCustomField")
public class RiskCustomFieldControllerImpl implements RiskCustomFieldController {

  private static final Logger log = LogManager.getLogger(RiskCustomFieldControllerImpl.class);
  @Autowired
  private RiskCustomFieldService riskCustomFieldService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public RiskCustomField create(RiskCustomField riskCustomField) {
	  log.info("inside @class RiskCustomFieldControllerImpl @method create");
    return riskCustomFieldService.create(riskCustomField);
  }

  @Override
  public Long count(String filter) {
    return riskCustomFieldService.count(filter);
  }

  @Override
  public List<RiskCustomField> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskCustomFieldService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public RiskCustomField update(RiskCustomField riskCustomField) {
    return riskCustomFieldService.update(riskCustomField);
  }

  @Override
  public RiskCustomField findById(Integer id) {
    return riskCustomFieldService.findById(id);
  }

  @Override
  public List<RiskCustomField> findAllById(List<Integer> id) {
    return riskCustomFieldService.findAllById(id);
  }

 		
   
   	 //done done RiskCustomField
	@Override
    public List<RiskCustomField>  findAllByEntityId(Integer id)
    {
      return riskCustomFieldService.findAllByEntityId(id);
    } 
   
   
   
}
