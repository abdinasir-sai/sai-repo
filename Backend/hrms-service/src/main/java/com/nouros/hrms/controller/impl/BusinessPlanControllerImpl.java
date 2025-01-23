package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.BusinessPlanController;
import com.nouros.hrms.model.BusinessPlan;
import com.nouros.hrms.service.BusinessPlanService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the BusinessPlanController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the BusinessPlanController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the BusinessPlanService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(BusinessPlan BusinessPlan): creates an BusinessPlan and returns the newly created BusinessPlan.
count(String filter): returns the number of BusinessPlan that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of BusinessPlan that match the specified filter, sorted according to the specified orderBy
and orderType.
update(BusinessPlan BusinessPlan): updates an BusinessPlan and returns the updated BusinessPlan.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of BusinessPlan with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/BusinessPlan")

//@Tag(name="/BusinessPlan",tags="BusinessPlan",description="BusinessPlan")
public class BusinessPlanControllerImpl implements BusinessPlanController {

  private static final Logger log = LogManager.getLogger(BusinessPlanControllerImpl.class);

  @Autowired
  private BusinessPlanService businessPlanService;
  
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public BusinessPlan create(BusinessPlan businessPlan) {
	  log.info("inside @class BusinessPlanControllerImpl @method create");
    return businessPlanService.create(businessPlan);
  }

  @Override
  public Long count(String filter) {
    return businessPlanService.count(filter);
  }

  @Override
  public List<BusinessPlan> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return businessPlanService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public BusinessPlan update(BusinessPlan businessPlan) {
    return businessPlanService.update(businessPlan);
  }

  @Override
  public BusinessPlan findById(Integer id) {
    return businessPlanService.findById(id);
  }

  @Override
  public List<BusinessPlan> findAllById(List<Integer> id) {
    return businessPlanService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    businessPlanService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    businessPlanService.bulkDelete(list);
  }


	@Override
	public BusinessPlan findByTitleAndMonth(String title,String planDate) {
		return businessPlanService.findByTitleAndMonth(title,planDate);
	}

		
   
   
   
   
}
