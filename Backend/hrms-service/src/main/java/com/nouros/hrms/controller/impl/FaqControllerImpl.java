package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.platform.customannotation.annotation.GenericAnnotation;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.FaqController;
import com.nouros.hrms.model.Faq;
import com.nouros.hrms.service.FaqService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**

This class represents the implementation of the FaqController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the FaqController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the FaqService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Faq Faq): creates an Faq and returns the newly created Faq.
count(String filter): returns the number of Faq that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Faq that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Faq Faq): updates an Faq and returns the updated Faq.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Faq with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Faq")
//@Tag(name="/Faq",tags="Faq",description="Faq")
public class FaqControllerImpl implements FaqController {

  private static final Logger log = LogManager.getLogger(FaqControllerImpl.class);

  @Autowired
  private FaqService faqService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
 @GenericAnnotation(actionType="CREATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Faq",globalSearchData="question",searchTitle="tags")

  public Faq create(Faq faq) {
	  log.info("inside @class FaqControllerImpl @method create");
    return faqService.create(faq);
  }

  @Override
  public Long count(String filter) {
    return faqService.count(filter);
  }

  @Override
  public List<Faq> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return faqService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
 @GenericAnnotation(actionType="UPDATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Faq",globalSearchData="question",searchTitle="tags")
  public Faq update(Faq faq) {
    return faqService.update(faq);
  }

  @Override
  public Faq findById(Integer id) {
    return faqService.findById(id);
  }

  @Override
  public List<Faq> findAllById(List<Integer> id) {
    return faqService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    faqService.deleteById(id);
  }
  
  
   
   
   
   
}
