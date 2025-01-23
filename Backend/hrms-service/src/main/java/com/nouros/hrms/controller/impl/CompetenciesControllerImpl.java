package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CompetenciesController;
import com.nouros.hrms.model.Competencies;
import com.nouros.hrms.service.CompetenciesService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CompetenciesController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CompetenciesController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CompetenciesService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Competencies Competencies): creates an Competencies and returns the newly created Competencies.
count(String filter): returns the number of Competencies that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Competencies that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Competencies Competencies): updates an Competencies and returns the updated Competencies.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Competencies with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Competencies")

//@Tag(name="/Competencies",tags="Competencies",description="Competencies")
public class CompetenciesControllerImpl implements CompetenciesController {

  private static final Logger log = LogManager.getLogger(CompetenciesControllerImpl.class);

  @Autowired
  private CompetenciesService competenciesService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Competencies create(Competencies competencies) {
	  log.info("inside @class CompetenciesControllerImpl @method create");
    return competenciesService.create(competencies);
  }

  @Override
  public Long count(String filter) {
    return competenciesService.count(filter);
  }

  @Override
  public List<Competencies> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return competenciesService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Competencies update(Competencies competencies) {
    return competenciesService.update(competencies);
  }

  @Override
  public Competencies findById(Integer id) {
    return competenciesService.findById(id);
  }

  @Override
  public List<Competencies> findAllById(List<Integer> id) {
    return competenciesService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    competenciesService.deleteById(id);
  }
  
 

}
