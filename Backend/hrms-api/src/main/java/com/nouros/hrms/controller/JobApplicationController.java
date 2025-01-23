package com.nouros.hrms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.document.constant.ValidationMessageConstant;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.ApplicantDto;
import com.nouros.hrms.wrapper.JobApplicationDto;
import com.nouros.hrms.wrapper.JobOpeningsDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * The interface JobApplicationController defines several endpoints that can be
 * accessed using the Spring Web's FeignClient to interact with the service
 * related to JobApplication.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {JobApplication.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new JobApplication
 * with the given request body and returns the created JobApplication.
 * count(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter) : returns the count of JobApplication with the RSQL query in the
 * filter parameter. search(@RequestParam(name = APIConstants.QUERY, required =
 * false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET,
 * required = true) Integer offset, @Valid @RequestParam(name =
 * APIConstants.SIZE, required = true) Integer size, @RequestParam(name =
 * APIConstants.SORT, required = false) String sort): searches for the list of
 * JobApplication based on the RSQL filter and returns the list of
 * JobApplication matching the criteria with the pagination and sorting
 * information. findById(@PathVariable("id") Long id) : searches for the
 * JobApplication by ID and returns the JobApplication if found.
 * update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) :
 * updates the existing JobApplication with the given id with the request body
 * and returns the updated JobApplication. delete(@PathVariable("id") Long id) :
 * deletes the JobApplication with the given id and returns the deleted
 * JobApplication. It also includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "JobApplicationController", url = "${hrms.url}", path = "/JobApplication", primary = false)
public interface JobApplicationController {

	@Operation(summary = "Creates a new JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	JobApplication create(@Valid @RequestBody JobApplication jobApplication);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of JobApplication with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<JobApplication> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	JobApplication update(@Valid @RequestBody JobApplication jobApplication);

	@Operation(summary = "To delete the given JobApplication by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get JobApplication by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	JobApplication findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all JobApplication by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<JobApplication> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@Operation(summary = "To Summarise Resume Of Applicant for specific Job Opening  for the given JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "summariseResumeByJobOpeningForJobApplication", consumes = MediaType.APPLICATION_JSON_VALUE)
	String summariseResumeByJobOpeningForJobApplication(@Valid @RequestBody JobApplication jobApplication);

	@Operation(summary = "To Summarise Resume Of Applicant for specific Job Opening  for the given JobApplication with input Resume File", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "summariseResumeByJobOpeningWithInputFile")
	String summariseResumeByJobOpeningWithInputFile(
			@RequestPart(required = true, name = "file") @NotNull(message = ValidationMessageConstant.FILE_CAN_NOT_BE_NULL) MultipartFile file,
			@Valid @RequestParam(name = "jobApplicationId", required = true) Integer jobApplicationId);

	@Operation(summary = "To get all JobApplication if applicant present", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getJobApplicantByUserContext")
	JobApplication findJobApplicantByUserContext();

	@Operation(summary = "To calculate overall score for JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_CALCULATE_OVERALL_SCORE_FOR_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "calculateOverallScoreForJobApplication")
	JobApplication calculateOverallScoreForJobApplication(@RequestBody JobApplication jobApplication);

	@Operation(summary = "To get top ranked,refferal applicant by posting title", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_TOP_RANKED_REFFERAL_APPLICANT_BY_POSTING_TITLe" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getApplicantCountByActiveJobOpening")
	JobOpeningsDto getApplicantCountByActiveJobOpening();

	@Operation(summary = "To get top ranked,refferal applicant by posting title", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_TOP_RANKED_REFFERAL_APPLICANT_BY_POSTING_TITLE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getTopRankedAndTopReferralApplicantByJobId")
	public Map<String, Object> getTopRankedAndTopReferralApplicantByJobId(
			@Valid @RequestParam(name = "jobId", required = true) String jobId);

	@Operation(summary = "To Delete Job Application Configuration Score By Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteConfigurationScoreByJobApplicationId")
	public String deleteJobApplicationConfigurationScoreById(@RequestParam("id") Integer id);

	@Operation(summary = "To Get Job Application list by id ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_GET" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "setRankingForJobAppicationsById")
	public List<JobApplication> setRankingForJobAppicationsById(@RequestParam("id") Integer id);

	@Operation(summary = "To set Overallscore and ranking for JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "setOverallScoresAndRankingForJobApplication")
	public String setOverallScoresAndRankingForJobApplication();
	
	@Operation(summary = "To set Overallscore and ranking for JobApplication", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "setTopRankedAndTopReferralApplicantByPostingTitle")
	public Map<String, Object> setTopRankedAndTopReferralApplicantByJobId(@RequestParam("jobId") String jobId , @RequestBody String jSon);
	
	
	
	@Operation(summary = "To update Batch for JobApplications", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateBatchForJobApplication")
	public List<JobApplication> updateBatchForJobApplication(@RequestBody JobApplicationDto jobApplicationDto);
	
	@Operation(summary = "To update Application Status  for JobApplications", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateJobApplicationStatus")
	public List<JobApplication> updateJobApplicationStatus(@RequestBody JobApplicationDto jobApplicationDto);
	
	@Operation(summary = "To Set Ranking on regular basis ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_GET" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "setRankingForJobAppicationsOnRegularBasis")
	public String setRankingForJobAppicationsOnRegularBasis();
	
	@Operation(summary = "To Set Ranking post two days ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBAPPLICATION_GET" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "setRankingForJobAppicationsPostTwoDays")
	public String setRankingForJobAppicationsPostTwoDays();

}
