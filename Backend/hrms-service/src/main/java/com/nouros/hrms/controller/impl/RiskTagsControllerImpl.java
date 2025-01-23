package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RiskTagsController;
import com.nouros.hrms.model.RiskTags;
import com.nouros.hrms.service.RiskTagsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the RiskTagsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskTagsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskTagsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(RiskTags RiskTags): creates an RiskTags and returns the newly created RiskTags.
count(String filter): returns the number of RiskTags that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of RiskTags that match the specified filter, sorted according to the specified orderBy
and orderType.
update(RiskTags RiskTags): updates an RiskTags and returns the updated RiskTags.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of RiskTags with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/RiskTags")
//@Tag(name="/RiskTags",tags="RiskTags",description="RiskTags")
public class RiskTagsControllerImpl implements RiskTagsController {

  private static final Logger log = LogManager.getLogger(RiskTagsControllerImpl.class);
  @Autowired
  private RiskTagsService riskTagsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public RiskTags create(RiskTags riskTags) {
	  log.info("inside @class RiskTagsControllerImpl @method create");
    return riskTagsService.create(riskTags);
  }

  @Override
  public Long count(String filter) {
    return riskTagsService.count(filter);
  }

  @Override
  public List<RiskTags> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskTagsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public RiskTags update(RiskTags riskTags) {
    return riskTagsService.update(riskTags);
  }

  @Override
  public RiskTags findById(Integer id) {
    return riskTagsService.findById(id);
  }

  @Override
  public List<RiskTags> findAllById(List<Integer> id) {
    return riskTagsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    riskTagsService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    riskTagsService.softBulkDelete(list);
  }

   
   
}
