package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantExperienceController;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.service.ApplicantExperienceService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ApplicantExperienceController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ApplicantExperienceController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ApplicantExperienceService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(ApplicantExperience ApplicantExperience): creates an ApplicantExperience and returns the newly created ApplicantExperience.
count(String filter): returns the number of ApplicantExperience that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of ApplicantExperience that match the specified filter, sorted according to the specified orderBy
and orderType.
update(ApplicantExperience ApplicantExperience): updates an ApplicantExperience and returns the updated ApplicantExperience.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of ApplicantExperience with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/ApplicantExperience")

//@Tag(name="/ApplicantExperience",tags="ApplicantExperience",description="ApplicantExperience")
public class ApplicantExperienceControllerImpl implements ApplicantExperienceController {

  private static final Logger log = LogManager.getLogger(ApplicantExperienceControllerImpl.class);

  @Autowired
  private ApplicantExperienceService applicantExperienceService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public ApplicantExperience create(ApplicantExperience applicantExperience) {
	  log.info("inside @class ApplicantExperienceControllerImpl @method create");
    return applicantExperienceService.create(applicantExperience);
  }

  @Override
  public Long count(String filter) {
    return applicantExperienceService.count(filter);
  }

  @Override
  public List<ApplicantExperience> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return applicantExperienceService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public ApplicantExperience update(ApplicantExperience applicantExperience) {
    return applicantExperienceService.update(applicantExperience);
  }

  @Override
  public ApplicantExperience findById(Integer id) {
    return applicantExperienceService.findById(id);
  }

  @Override
  public List<ApplicantExperience> findAllById(List<Integer> id) {
    return applicantExperienceService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    applicantExperienceService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    applicantExperienceService.bulkDelete(list);
  }

   
   
   
}
