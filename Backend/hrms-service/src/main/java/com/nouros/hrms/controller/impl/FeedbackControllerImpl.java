package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.FeedbackController;
import com.nouros.hrms.model.Feedback;
import com.nouros.hrms.service.FeedbackService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the FeedbackController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the FeedbackController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the FeedbackService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Feedback Feedback): creates an Feedback and returns the newly created Feedback.
count(String filter): returns the number of Feedback that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Feedback that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Feedback Feedback): updates an Feedback and returns the updated Feedback.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Feedback with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Feedback")
//@Tag(name="/Feedback",tags="Feedback",description="Feedback")
public class FeedbackControllerImpl implements FeedbackController {

  private static final Logger log = LogManager.getLogger(FeedbackControllerImpl.class);

  @Autowired
  private FeedbackService feedbackService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Feedback create(Feedback feedback) {
	  log.info("inside @class FeedbackControllerImpl @method create");
    return feedbackService.create(feedback);
  }

  @Override
  public Long count(String filter) {
    return feedbackService.count(filter);
  }

  @Override
  public List<Feedback> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return feedbackService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Feedback update(Feedback feedback) {
    return feedbackService.update(feedback);
  }

  @Override
  public Feedback findById(Integer id) {
    return feedbackService.findById(id);
  }

  @Override
  public List<Feedback> findAllById(List<Integer> id) {
    return feedbackService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    feedbackService.deleteById(id);
  }
  
  
		
   
   
   
   
}
