package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CaseCategoryController;
import com.nouros.hrms.model.CaseCategory;
import com.nouros.hrms.service.CaseCategoryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CaseCategoryController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CaseCategoryController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CaseCategoryService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(CaseCategory CaseCategory): creates an CaseCategory and returns the newly created CaseCategory.
count(String filter): returns the number of CaseCategory that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of CaseCategory that match the specified filter, sorted according to the specified orderBy
and orderType.
update(CaseCategory CaseCategory): updates an CaseCategory and returns the updated CaseCategory.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of CaseCategory with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/CaseCategory")

//@Tag(name="/CaseCategory",tags="CaseCategory",description="CaseCategory")
public class CaseCategoryControllerImpl implements CaseCategoryController {

  private static final Logger log = LogManager.getLogger(CaseCategoryControllerImpl.class);

  @Autowired
  private CaseCategoryService caseCategoryService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public CaseCategory create(CaseCategory caseCategory) {
	  log.info("inside @class CaseCategoryControllerImpl @method create");
    return caseCategoryService.create(caseCategory);
  }

  @Override
  public Long count(String filter) {
    return caseCategoryService.count(filter);
  }

  @Override
  public List<CaseCategory> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return caseCategoryService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public CaseCategory update(CaseCategory caseCategory) {
    return caseCategoryService.update(caseCategory);
  }

  @Override
  public CaseCategory findById(Integer id) {
    return caseCategoryService.findById(id);
  }

  @Override
  public List<CaseCategory> findAllById(List<Integer> id) {
    return caseCategoryService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    caseCategoryService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    caseCategoryService.bulkDelete(list);
  }


		
   
   
   
   
}
