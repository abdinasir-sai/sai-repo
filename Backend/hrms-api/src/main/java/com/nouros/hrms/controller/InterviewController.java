package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enttribe.connectx.dto.ResponseDto;
import com.enttribe.connectx.meeting.dto.AvailableMeetingSlotsDto;
import com.nouros.hrms.model.Interview;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.InterviewAndFeedbackWrapper;
import com.nouros.hrms.wrapper.InterviewDto;
import com.nouros.hrms.wrapper.InterviewWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface InterviewController defines several endpoints that can be
 * accessed using the Spring Web's FeignClient to interact with the service
 * related to Interview.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {Interview.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new Interview with
 * the given request body and returns the created Interview.
 * count(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter) : returns the count of Interview with the RSQL query in the filter
 * parameter. search(@RequestParam(name = APIConstants.QUERY, required = false)
 * String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required =
 * true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required
 * = true) Integer size, @RequestParam(name = APIConstants.SORT, required =
 * false) String sort): searches for the list of Interview based on the RSQL
 * filter and returns the list of Interview matching the criteria with the
 * pagination and sorting information. findById(@PathVariable("id") Long id) :
 * searches for the Interview by ID and returns the Interview if found.
 * update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) :
 * updates the existing Interview with the given id with the request body and
 * returns the updated Interview. delete(@PathVariable("id") Long id) : deletes
 * the Interview with the given id and returns the deleted Interview. It also
 * includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "InterviewController", url = "${hrms.url}", path = "/Interview", primary = false)
public interface InterviewController {

	@Operation(summary = "Creates a new Interview", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	Interview create(@Valid @RequestBody Interview interview);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of Interview with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<Interview> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given Interview", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	Interview update(@Valid @RequestBody Interview interview);

	@Operation(summary = "To delete the given Interview by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get Interview by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	Interview findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all Interview by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<Interview> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@Operation(summary = "To get Interview and Interview Feedback by applicantId and jobOpeningId", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findInterviewDetails")
	InterviewAndFeedbackWrapper findInterviewDetails(
			@Valid @RequestParam(name = "applicantId", required = true) Integer applicantId,
			@Valid @RequestParam(name = "jobOpeningId", required = true) Integer jobOpeningId);

	@Operation(summary = "To update the given InterviewFeedbacks based on Interview", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateInterviewFeedback", consumes = MediaType.APPLICATION_JSON_VALUE)
	InterviewAndFeedbackWrapper updateInterviewFeedback(
			@Valid @RequestBody InterviewAndFeedbackWrapper interviewAndFeedbackWrapper);

	@PostMapping(path = "/getAvailableSlotsAndConflict")
	@Operation(summary = "getAvailbaleSlotsAndConflict", tags = "integration", description = ".")
	@ApiResponse(responseCode = "400", description = "Application was unable to complete the request because it was invalid. The request should not be retried without modification.")
	@ApiResponse(responseCode = "401", description = "Client could not be authenticated.")
	@ApiResponse(responseCode = "403", description = "Client is not authorized to make this request.")
	@ApiResponse(responseCode = "404", description = "The specified resource could not be found.")
	@ApiResponse(responseCode = "409", description = "The request was valid but was not in the appropriate state to process it. Retrying the same request later may be successful.")
	public ResponseDto getAvailableSlotsAndConflict(@RequestBody AvailableMeetingSlotsDto availableMeetingSlotsDto);

	@Operation(summary = "To update Interview work flow stage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_INTERVIEW_UPDATE_WORK_FLOW_STAGE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateInterviewWorkflowStage")
	Interview updateInterviewWorkflowStage(@RequestBody InterviewDto interviewDto);

	@Operation(summary = "To update Interview TimeSlot", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateInterviewTimeSlot")
	String updateInterviewTimeSlot(@RequestParam("processInstanceId") String processInstanceId,
			@RequestBody InterviewWrapper interviewWrapper);

	@Operation(summary = "To update Interview Schedule Status", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateInterviewScheduleStatus")
	Interview updateInterviewScheduleStatus(@RequestBody InterviewWrapper interviewWrapper);

	@Operation(summary = "Creates a new Interview for first round", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "interviewCreateForFirstRound")
	Interview interviewCreateForFirstRound(@RequestParam(name = "jobAppId", required = false) Integer jobAppId,
			@RequestParam(name = "jobApplicationId", required = false) String jobApplicationId);

	@Operation(summary = "Creates a new Interview for Second round", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "interviewCreateForSecondRound")
	Interview interviewCreateForSecondRound(@RequestParam(name = "interviewId", required = false) Integer interviewId,
			@RequestParam(name = "processInstanceId", required = false) String processInstanceId);

	@Operation(summary = "Creates a new Interview for Second round", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "secondRoundInterviewCreate")
	InterviewDto secondRoundInterviewCreate(@RequestParam(name = "interviewId", required = false) Integer interviewId,
			@RequestParam(name = "processInstanceId", required = false) String processInstanceId,
			@RequestParam(name = "interviewStage", required = false) String interviewStage);

	@Operation(summary = "SendMeetingLink for Interview", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "sendMeetingLink")
	ResponseDto sendMeetingLink(@RequestParam(name = "interviewId", required = true) Integer interviewId);

	@Operation(summary = "Update Applicant/Application Status for Interview", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "updateJobApplicationStatusForInterview")
	String updateJobApplicationStatusForInterview(
			@RequestParam(name = "interviewId", required = false) Integer interviewId,
			@RequestParam(name = "interviewStatus", required = false) String interviewStatus);
	
	
	@Operation(summary = "Get Interviewermanager WorkEmailAddress", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_INTERVIEW_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getInterviewerManagerWorkEmailAddress")
	String getInterviewerManagerWorkEmailAddress(
			@RequestParam(name = "interviewId", required = false) Integer interviewId);

}
