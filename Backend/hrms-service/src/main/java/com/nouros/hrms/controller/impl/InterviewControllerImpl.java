package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.connectx.dto.ResponseDto;
import com.enttribe.connectx.meeting.dto.AvailableMeetingSlotsDto;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.InterviewController;
import com.nouros.hrms.model.Interview;
import com.nouros.hrms.service.InterviewService;
import com.nouros.hrms.wrapper.InterviewAndFeedbackWrapper;
import com.nouros.hrms.wrapper.InterviewDto;
import com.nouros.hrms.wrapper.InterviewWrapper;


/**

This class represents the implementation of the InterviewController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the InterviewController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the InterviewService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Interview Interview): creates an Interview and returns the newly created Interview.
count(String filter): returns the number of Interview that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Interview that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Interview Interview): updates an Interview and returns the updated Interview.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Interview with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Interview")
//@Tag(name="/Interview",tags="Interview",description="Interview")
public class InterviewControllerImpl implements InterviewController {

  private static final Logger log = LogManager.getLogger(InterviewControllerImpl.class);

  @Autowired
  private InterviewService interviewService;
  

	
  @Override
 @TriggerBPMN(entityName = "Interview", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Interview create(Interview interview) {
	  log.info("inside @class InterviewControllerImpl @method create");
    return interviewService.create(interview);
  }

  @Override
  public Long count(String filter) {
    return interviewService.count(filter);
  }

  @Override
  public List<Interview> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return interviewService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @TriggerBPMN(entityName = "Interview", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Interview update(Interview interview) {
    return interviewService.update(interview);
  }

  @Override
  public Interview findById(Integer id) {
    return interviewService.findById(id);
  }

  @Override
  public List<Interview> findAllById(List<Integer> id) {
    return interviewService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    interviewService.deleteById(id);
  }
  
 		
    @Override
    public InterviewAndFeedbackWrapper findInterviewDetails(Integer applicantId,Integer jobOpeningId) {
      return interviewService.findInterviewDetails(applicantId,jobOpeningId);
    }
   
    @Override
    @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE_INTERVIEW_FEEDBACK")
    public InterviewAndFeedbackWrapper updateInterviewFeedback(InterviewAndFeedbackWrapper interviewAndFeedbackWrapper) {
      return interviewService.updateInterviewFeedback(interviewAndFeedbackWrapper);
    }

	@Override
	public ResponseDto getAvailableSlotsAndConflict(AvailableMeetingSlotsDto availableMeetingSlotsDto) {
		return  interviewService.getAvailableSlotsAndConflict(availableMeetingSlotsDto);
	}

	@Override
	public Interview updateInterviewWorkflowStage(InterviewDto interviewDto) {
		return interviewService.updateInterviewWorkflowStage(interviewDto);
	}

	@Override
	public String updateInterviewTimeSlot(String processInstanceId,InterviewWrapper interviewWrapper) {
		return interviewService.updateInterviewTimeSlot(processInstanceId,interviewWrapper);
	}
	
	@Override
	public Interview updateInterviewScheduleStatus(InterviewWrapper interviewWrapper){
		return interviewService.updateInterviewScheduleStatus(interviewWrapper);
	}

	@Override
	@TriggerBPMN(entityName = "Interview", appName = "HRMS_APP_NAME")
	public Interview interviewCreateForFirstRound(Integer jobAppId, String jobApplicationId) {
		return interviewService.interviewCreateForFirstRound(jobAppId,jobApplicationId);
	}

	@Override
	public Interview interviewCreateForSecondRound(Integer interviewId,String processInstanceId) {
		return interviewService.interviewCreateForSecondRound(interviewId,processInstanceId);
	}

	@Override
	public ResponseDto sendMeetingLink(Integer interviewId) {
		return interviewService.sendMeetingLink(interviewId);
	}

	@Override
	public String updateJobApplicationStatusForInterview(Integer interviewId,String interviewStatus) {
		return interviewService.updateJobApplicationStatusForInterview(interviewId,interviewStatus);
	}

	@Override
	@TriggerBPMN(entityName = "Interview", appName = "HRMS_APP_NAME")
	public InterviewDto secondRoundInterviewCreate(Integer interviewId, String processInstanceId,String interviewStage) {
		return interviewService.secondRoundInterviewCreate(interviewId,processInstanceId,interviewStage);
	}

	@Override
	public String getInterviewerManagerWorkEmailAddress(Integer interviewId) {
		return interviewService.getInterviewerManagerWorkEmailAddress(interviewId);
	}
	
   
}
