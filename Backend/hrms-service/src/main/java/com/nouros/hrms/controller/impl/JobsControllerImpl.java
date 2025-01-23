package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.JobsController;
import com.nouros.hrms.model.Jobs;
import com.nouros.hrms.service.JobsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the JobsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the JobsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the JobsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Jobs Jobs): creates an Jobs and returns the newly created Jobs.
count(String filter): returns the number of Jobs that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Jobs that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Jobs Jobs): updates an Jobs and returns the updated Jobs.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Jobs with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Jobs")
//@Tag(name="/Jobs",tags="Jobs",description="Jobs")
public class JobsControllerImpl implements JobsController {

  private static final Logger log = LogManager.getLogger(JobsControllerImpl.class);

  @Autowired
  private JobsService jobsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Jobs create(Jobs jobs) {
	  log.info("inside @class JobsControllerImpl @method create");
    return jobsService.create(jobs);
  }

  @Override
  public Long count(String filter) {
    return jobsService.count(filter);
  }

  @Override
  public List<Jobs> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return jobsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Jobs update(Jobs jobs) {
    return jobsService.update(jobs);
  }

  @Override
  public Jobs findById(Integer id) {
    return jobsService.findById(id);
  }

  @Override
  public List<Jobs> findAllById(List<Integer> id) {
    return jobsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    jobsService.deleteById(id);
  }
  

   
   
   
   
}
