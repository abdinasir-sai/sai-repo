package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantEducationController;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.service.ApplicantEducationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ApplicantEducationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ApplicantEducationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ApplicantEducationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(ApplicantEducation ApplicantEducation): creates an ApplicantEducation and returns the newly created ApplicantEducation.
count(String filter): returns the number of ApplicantEducation that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of ApplicantEducation that match the specified filter, sorted according to the specified orderBy
and orderType.
update(ApplicantEducation ApplicantEducation): updates an ApplicantEducation and returns the updated ApplicantEducation.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of ApplicantEducation with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/ApplicantEducation")

//@Tag(name="/ApplicantEducation",tags="ApplicantEducation",description="ApplicantEducation")
public class ApplicantEducationControllerImpl implements ApplicantEducationController {

  private static final Logger log = LogManager.getLogger(ApplicantEducationControllerImpl.class);

  @Autowired
  private ApplicantEducationService applicantEducationService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public ApplicantEducation create(ApplicantEducation applicantEducation) {
	  log.info("inside @class ApplicantEducationControllerImpl @method create");
    return applicantEducationService.create(applicantEducation);
  }

  @Override
  public Long count(String filter) {
    return applicantEducationService.count(filter);
  }

  @Override
  public List<ApplicantEducation> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return applicantEducationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public ApplicantEducation update(ApplicantEducation applicantEducation) {
    return applicantEducationService.update(applicantEducation);
  }

  @Override
  public ApplicantEducation findById(Integer id) {
    return applicantEducationService.findById(id);
  }

  @Override
  public List<ApplicantEducation> findAllById(List<Integer> id) {
    return applicantEducationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    applicantEducationService.deleteById(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    applicantEducationService.bulkDelete(list);
  }

   
   
   
   
}
