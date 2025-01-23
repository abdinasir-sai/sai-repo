package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.InterviewFeedbackController;
import com.nouros.hrms.model.InterviewFeedback;
import com.nouros.hrms.service.InterviewFeedbackService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/InterviewFeedback")
public class InterviewFeedbackControllerImpl implements InterviewFeedbackController {
	
	private static final Logger log = LogManager.getLogger(InterviewFeedbackControllerImpl.class);

	  @Autowired
	  private InterviewFeedbackService interviewFeedbackService;
	  		
	  @Override
      @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	  public InterviewFeedback create(InterviewFeedback interviewFeedback) {
		  log.info("inside @class InterviewFeedbackControllerImpl @method create");
	    return interviewFeedbackService.create(interviewFeedback);
	  }

	  @Override
	  public Long count(String filter) {
	    return interviewFeedbackService.count(filter);
	  }

	  @Override
	  public List<InterviewFeedback> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return interviewFeedbackService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	  public InterviewFeedback update(InterviewFeedback interviewFeedback) {
	    return interviewFeedbackService.update(interviewFeedback);
	  }

	  @Override
	  public InterviewFeedback findById(Integer id) {
	    return interviewFeedbackService.findById(id);
	  }

	  @Override
	  public List<InterviewFeedback> findAllById(List<Integer> id) {
	    return interviewFeedbackService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void deleteById(Integer id) {
		  interviewFeedbackService.softDelete(id);
	  }
	  
	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void bulkDelete(List<Integer> list) {
		  interviewFeedbackService.softBulkDelete(list);
	  }

	    @Override
		  public List<InterviewFeedback> findAllByInterviewId(Integer interviewId) {
		    return interviewFeedbackService.findAllByInterviewId(interviewId);
		  }

		@Override
		public InterviewFeedback setAverageInterviewScore(Integer interviewFeedbackId,@Valid List<Integer> scoreList) {
			return interviewFeedbackService.setAverageInterviewScore(interviewFeedbackId,scoreList);
		}
	    
}
