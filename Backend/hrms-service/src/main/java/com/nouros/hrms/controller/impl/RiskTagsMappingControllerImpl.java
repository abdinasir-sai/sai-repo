package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RiskTagsMappingController;
import com.nouros.hrms.model.RiskTagsMapping;
import com.nouros.hrms.service.RiskTagsMappingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the RiskTagsMappingController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskTagsMappingController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskTagsMappingService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(RiskTagsMapping RiskTagsMapping): creates an RiskTagsMapping and returns the newly created RiskTagsMapping.
count(String filter): returns the number of RiskTagsMapping that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of RiskTagsMapping that match the specified filter, sorted according to the specified orderBy
and orderType.
update(RiskTagsMapping RiskTagsMapping): updates an RiskTagsMapping and returns the updated RiskTagsMapping.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of RiskTagsMapping with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/RiskTagsMapping")
//@Tag(name="/RiskTagsMapping",tags="RiskTagsMapping",description="RiskTagsMapping")
public class RiskTagsMappingControllerImpl implements RiskTagsMappingController {


  private static final Logger log = LogManager.getLogger(RiskTagsMappingControllerImpl.class);
  @Autowired
  private RiskTagsMappingService riskTagsMappingService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public RiskTagsMapping create(RiskTagsMapping riskTagsMapping) {
	  log.info("inside @class RiskTagsMappingControllerImpl @method create");
    return riskTagsMappingService.create(riskTagsMapping);
  }

  @Override
  public Long count(String filter) {
    return riskTagsMappingService.count(filter);
  }

  @Override
  public List<RiskTagsMapping> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskTagsMappingService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public RiskTagsMapping update(RiskTagsMapping riskTagsMapping) {
    return riskTagsMappingService.update(riskTagsMapping);
  }

  @Override
  public RiskTagsMapping findById(Integer id) {
    return riskTagsMappingService.findById(id);
  }

  @Override
  public List<RiskTagsMapping> findAllById(List<Integer> id) {
    return riskTagsMappingService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    riskTagsMappingService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    riskTagsMappingService.softBulkDelete(list);
  }


		
   
   
   
   
}
