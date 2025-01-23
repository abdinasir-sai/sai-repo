package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RiskTypeController;
import com.nouros.hrms.model.RiskType;
import com.nouros.hrms.service.RiskTypeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the RiskTypeController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskTypeController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskTypeService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(RiskType RiskType): creates an RiskType and returns the newly created RiskType.
count(String filter): returns the number of RiskType that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of RiskType that match the specified filter, sorted according to the specified orderBy
and orderType.
update(RiskType RiskType): updates an RiskType and returns the updated RiskType.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of RiskType with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/RiskType")
//@Tag(name="/RiskType",tags="RiskType",description="RiskType")
public class RiskTypeControllerImpl implements RiskTypeController {

  private static final Logger log = LogManager.getLogger(RiskTypeControllerImpl.class);
  @Autowired
  private RiskTypeService riskTypeService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public RiskType create(RiskType riskType) {
	  log.info("inside @class RiskTypeControllerImpl @method create");
    return riskTypeService.create(riskType);
  }

  @Override
  public Long count(String filter) {
    return riskTypeService.count(filter);
  }

  @Override
  public List<RiskType> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskTypeService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public RiskType update(RiskType riskType) {
    return riskTypeService.update(riskType);
  }

  @Override
  public RiskType findById(Integer id) {
    return riskTypeService.findById(id);
  }

  @Override
  public List<RiskType> findAllById(List<Integer> id) {
    return riskTypeService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    riskTypeService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    riskTypeService.softBulkDelete(list);
  }

		
   
   
   
   
}
