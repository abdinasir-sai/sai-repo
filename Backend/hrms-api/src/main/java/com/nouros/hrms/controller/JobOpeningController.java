package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ProductApiResponses;
import com.enttribe.document.model.Document;
import com.enttribe.document.model.SubFolder;
import com.enttribe.document.utils.Views;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonView;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.JobOpeningAndApplicantReferralDto;
import com.nouros.hrms.wrapper.JobOpeningDto;
import com.nouros.hrms.wrapper.JobOpeningIdDto;
import com.nouros.hrms.wrapper.JobOpeningSchedulerDto;
import com.nouros.hrms.wrapper.JobOpeningWrapper;
import com.nouros.hrms.wrapper.JobOpeningsDto;
import com.nouros.hrms.wrapper.LeavesDto;

import feign.Headers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * The interface JobOpeningController defines several endpoints that can be
 * accessed using the Spring Web's FeignClient to interact with the service
 * related to JobOpening.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {JobOpening.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new JobOpening with
 * the given request body and returns the created JobOpening.
 * count(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter) : returns the count of JobOpening with the RSQL query in the filter
 * parameter. search(@RequestParam(name = APIConstants.QUERY, required = false)
 * String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required =
 * true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required
 * = true) Integer size, @RequestParam(name = APIConstants.SORT, required =
 * false) String sort): searches for the list of JobOpening based on the RSQL
 * filter and returns the list of JobOpening matching the criteria with the
 * pagination and sorting information. findById(@PathVariable("id") Long id) :
 * searches for the JobOpening by ID and returns the JobOpening if found.
 * update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) :
 * updates the existing JobOpening with the given id with the request body and
 * returns the updated JobOpening. delete(@PathVariable("id") Long id) : deletes
 * the JobOpening with the given id and returns the deleted JobOpening. It also
 * includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "JobOpeningController", url = "${hrms.url}", path = "/JobOpening", primary = false)
public interface JobOpeningController {

	@Operation(summary = "Creates a new JobOpening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	JobOpening create(@Valid @RequestBody JobOpening jobOpening);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of JobOpening with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<JobOpening> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given JobOpening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	JobOpening update(@Valid @RequestBody JobOpening jobOpening);

	@Operation(summary = "To delete the given JobOpening by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the JobOpening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "deleteAll")
	void bulkDelete(
			@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	@Operation(summary = "To get JobOpening by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	JobOpening findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all JobOpening by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_JOBOPENING_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<JobOpening> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@PostMapping(path = "uploadDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Headers("Content-Type: multipart/form-data")
	@ProductApiResponses
	Document uploadDocument(@RequestParam("filedata") MultipartFile file,
			@RequestParam(required = false, name = "filename") String fileName,
			@RequestParam(required = false, name = "referenceId") String referenceId);

	@GetMapping(value = "deleteFromUploadWindow/{id}")
	@Operation(summary = "Delete Document By DocumentId", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_JOBOPENING_DOCUMENT_WRITE" }) })
	@ProductApiResponses
	String deleteDocument(@PathVariable(name = "id") @NotNull(message = "Id can not be null") Integer primaryKey);

	@GetMapping(value = "/fileDownload"/* , consumes = "application/octet-stream" */)
	@Operation(summary = "Download Single Document Common api", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_JOBOPENING_DOCUMENT_READ" }) })
	@ProductApiResponses
	ResponseEntity<Document> fileDownload(
			@RequestParam(required = false, name = "documentId") @NotNull(message = "documentId can not be null") @Min(value = 0, message = "documentId must be greater than 0") Integer documentId);

	@Operation(summary = "Get Total Count Of My Documents Folder And Files", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_JOBOPENING_DOCUMENT_READ" }) })
	@ProductApiResponses
	@GetMapping(value = "countOfMyDocuments")
	Integer countOfMyDocuments(@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(required = false, name = "searchText") String searchText);

	@Operation(summary = "Get My Documents Folder And Files", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_JOBOPENING_DOCUMENT_READ" }) })
	@ProductApiResponses
	@JsonView(value = { Views.NoView.class })
	@GetMapping(value = "getMyDocuments", produces = "application/json")
	List<Document> getMyDocuments(@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(required = false, name = "upperLimit") Integer upperLimit,
			@RequestParam(required = false, name = "lowerLimit") Integer lowerLimit,
			@RequestParam(required = false, name = "modifiedTimeType") String modifiedTimeType,
			@RequestParam(required = false, name = "modificationtime") Long modificationtime,
			@RequestParam(required = false, name = "searchText") String searchText);

			
			 @GetMapping(value = "getSubFolderByReferenceValueAndType")
	  @Operation(summary = "get folder by reference value and/or reference type",  security = {
	      @SecurityRequirement(name  = "default", scopes = {
	          "ROLE_API_JOBOPENING_DOCUMENT_READ"})
	  })
	  @ProductApiResponses
	  SubFolder getSubFolderByReferenceValueAndType(@RequestParam(name = "referenceType") String referenceType,
	      @RequestParam(name = "referenceValue") String referenceValue);
   
   
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "getActions")
		  List<WorkflowActions> getActions(
		      @Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
     
   	@Operation(summary = "To get List of Applicants by  job Opening Id",  security = {
     	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
     	          "ROLE_API_JOBOPENING_FIND_SUITABLE_APPLICANT"})
     	      })
     	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
     	  @GetMapping(path = "findSuitableApplicantByJobOpeningId")
     	  List<Applicant> findSuitableApplicantByJobOpeningId(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
   
   	@Operation(summary = "To get List of JobOpening by user login",  security = {
   	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
   	          "ROLE_API_JOBOPENING_FIND_SUITABLE_APPLICANT"})
   	      })
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   	  @GetMapping(path = "findJobOpeningByUserLogin")
   	  JobOpening findJobOpeningByUserLogin();
   	
	@Operation(summary = "To get List of JobOpening by workflow stage",  security = {
	   	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	   	          "ROLE_API_JOBOPENING_FIND_SUITABLE_APPLICANT"})
	   	      })
	   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   	  @GetMapping(path = "getJobOpeningByActiveWorkFlowStage")
	   	  List<JobOpeningDto> getJobOpeningByActiveWorkFlowStage();
	
	  @Operation(summary = "To refer Applicant By JOBOPENING", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_JOBOPENING_REFER_APPLICANT_FOR_JOBOPENING" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "referApplicantForJobopening")
	  String referApplicantForJobopening(@RequestBody JobOpeningWrapper jobOpeningWrapper);
	  
		@Operation(summary = "To get List of Active JobOpening Through Scheduler",  security = {
		   	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		   	          "ROLE_API_JOBOPENING_FIND_ACTIVE_JOBOPENING"})
		   	      })
		   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		   	  @GetMapping(path = "getActiveJobOpening")
		   	  JobOpeningSchedulerDto getActiveJobOpening();
		
		@Operation(summary = "To get List of Active JobOpening by Department Id",  security = {
		   	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		   	          "ROLE_API_JOBOPENING_FIND_ACTIVE_JOBOPENING_BY_DEPARTMENT"})
		   	      })
		   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		   	  @GetMapping(path = "getActiveJobOpeningByDepartment")
		   	  JobOpeningSchedulerDto getActiveJobOpeningByDepartment(Integer departmentId);
				
		  @Operation(summary = "Creates a new JobOpening And ApplicantReferral",  security = {
			      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
			          "ROLE_API_JOB_OPENING_AND_APPLICANT_REFERRAL_WRITE"})
			      })
			  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
			  @PostMapping(path = "createJobOpening", consumes = MediaType.APPLICATION_JSON_VALUE)
			  JobOpening createJobOpening(@RequestBody JobOpeningAndApplicantReferralDto jobOpeningAndApplicantReferralDto);
		  
		  @Operation(summary = "To get JobOpeningIdDto by jobId",  security = {
		   	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		   	          "ROLE_API_JOBOPENING_GET_JOBOPENINGIDDTO"})
		   	      })
		   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		   	  @GetMapping(path = "getJobOpeningDetailByJobId")
		JobOpeningIdDto getJobOpeningDetailByJobId(@RequestParam(name = "jobId") String jobId);


	@Operation(summary = "To get Applicant Count by Active JobOpening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_JOBOPENING_FIND_APPLICANT_BY_ACTIVE_JOBOPENING" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getApplicantCountByActiveJobOpening")
	JobOpeningsDto getApplicantCountByActiveJobOpening(
			@RequestParam(name = "isCritical", required = false) String isCritical,
			@RequestParam(name = "departmentName", required = false) String departmentName,
			@RequestParam(name = "postingTitleName", required = false) String postingTitleName,
			@RequestParam(name = "postingTitleJobLevel", required = false) String postingTitleJobLevel);


}
