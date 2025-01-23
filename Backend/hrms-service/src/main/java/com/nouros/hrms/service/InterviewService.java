package com.nouros.hrms.service;


import com.enttribe.connectx.dto.ResponseDto;
import com.enttribe.connectx.meeting.dto.AvailableMeetingSlotsDto;
import com.nouros.hrms.model.Interview;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.InterviewAndFeedbackWrapper;
import com.nouros.hrms.wrapper.InterviewDto;
import com.nouros.hrms.wrapper.InterviewWrapper;

/**
 * 
 * InterviewService interface is a service layer interface which handles all the
 * business logic related to Interview model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Interview
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface InterviewService extends GenericService<Integer,Interview> {

	Interview findByProcessInstanceId(String processInstanceId);

	String meetingCreationForInterview(Interview interview);

	InterviewAndFeedbackWrapper findInterviewDetails(Integer applicantId, Integer jobOpeningId);

	InterviewAndFeedbackWrapper updateInterviewFeedback(InterviewAndFeedbackWrapper interviewAndFeedbackWrapper);

	ResponseDto getAvailableSlotsAndConflict(AvailableMeetingSlotsDto availableMeetingSlotsDto);
	
    Interview create(Interview interview);
    
    Interview updateInterviewWorkflowStage(InterviewDto interviewDto);

    String updateInterviewTimeSlot(String processInstanceId, InterviewWrapper interviewWrapper);
	
	Interview update(Interview interview);
	
	Interview updateInterviewScheduleStatus(InterviewWrapper interviewWrapper);

	Interview interviewCreateForFirstRound(Integer jobAppId, String jobApplicationId);

	Interview interviewCreateForSecondRound(Integer interviewId, String processInstanceId);

	ResponseDto sendMeetingLink(Integer interviewId);

	String updateJobApplicationStatusForInterview(Integer interviewId, String interviewStatus);

	InterviewDto secondRoundInterviewCreate(Integer interviewId, String processInstanceId, String interviewStage);

	String getInterviewerManagerWorkEmailAddress(Integer interviewId);

}
