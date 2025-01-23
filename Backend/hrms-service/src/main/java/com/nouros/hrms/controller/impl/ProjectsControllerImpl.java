package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ProjectsController;
import com.nouros.hrms.model.Projects;
import com.nouros.hrms.service.ProjectsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ProjectsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ProjectsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ProjectsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Projects Projects): creates an Projects and returns the newly created Projects.
count(String filter): returns the number of Projects that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Projects that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Projects Projects): updates an Projects and returns the updated Projects.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Projects with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Projects")
//@Tag(name="/Projects",tags="Projects",description="Projects")
public class ProjectsControllerImpl implements ProjectsController {

  private static final Logger log = LogManager.getLogger(ProjectsControllerImpl.class);
  @Autowired
  private ProjectsService projectsService;
  

	
  @Override
  @TriggerBPMN(entityName = "Projects", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Projects create(Projects projects) {
	  log.info("inside @class ProjectsControllerImpl @method create");
    return projectsService.create(projects);
  }

  @Override
  public Long count(String filter) {
    return projectsService.count(filter);
  }

  @Override
  public List<Projects> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return projectsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Projects update(Projects projects) {
    return projectsService.update(projects);
  }

  @Override
  public Projects findById(Integer id) {
    return projectsService.findById(id);
  }

  @Override
  public List<Projects> findAllById(List<Integer> id) {
    return projectsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    projectsService.deleteById(id);
  }
  
  

}
