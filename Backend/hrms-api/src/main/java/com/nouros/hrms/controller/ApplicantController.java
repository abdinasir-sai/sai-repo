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
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.ApplicantPromptWrapper;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.EmployeePopulateDto;
import com.nouros.hrms.wrapper.ProfessionalSummaryWrapper;
import com.nouros.hrms.wrapper.WorkExperienceSummaryWrapper;

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
 * The interface ApplicantController defines several endpoints that can be
 * accessed using the Spring Web's FeignClient to interact with the service
 * related to Applicant.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {Applicant.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new Applicant with
 * the given request body and returns the created Applicant.
 * count(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter) : returns the count of Applicant with the RSQL query in the filter
 * parameter. search(@RequestParam(name = APIConstants.QUERY, required = false)
 * String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required =
 * true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE,
 * required = true) Integer size, @RequestParam(name = APIConstants.SORT,
 * required = false) String sort): searches for the list of Applicant based on
 * the RSQL filter and returns the list of Applicant matching the criteria with
 * the pagination and sorting information. findById(@PathVariable("id") Long id)
 * : searches for the Applicant by ID and returns the Applicant if found.
 * update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) :
 * updates the existing Applicant with the given id with the request body and
 * returns the updated Applicant. delete(@PathVariable("id") Long id) : deletes
 * the Applicant with the given id and returns the deleted Applicant. It also
 * includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "ApplicantController", url = "${hrms.url}", path = "/Applicant", primary = false)
public interface ApplicantController {

	@Operation(summary = "Creates a new Applicant", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	Applicant create(@Valid @RequestBody Applicant applicant);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of Applicant with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<Applicant> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given Applicant", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	Applicant update(@Valid @RequestBody Applicant applicant);

	@Operation(summary = "To delete the given Applicant by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the Applicant", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "deleteAll")
	void bulkDelete(
			@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	@Operation(summary = "To get Applicant by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	Applicant findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all Applicant by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<Applicant> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	

	@PostMapping(path = "uploadDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Headers("Content-Type: multipart/form-data")
	@ProductApiResponses
	Document uploadDocument(@RequestParam("filedata") MultipartFile file,
			@RequestParam(required = false, name = "filename") String fileName,
			@RequestParam(required = false, name = "referenceId") String referenceId);

	@GetMapping(value = "deleteFromUploadWindow/{id}")
	@Operation(summary = "Delete Document By DocumentId", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_APPLICANT_DOCUMENT_WRITE" }) })
	@ProductApiResponses
	String deleteDocument(@PathVariable(name = "id") @NotNull(message = "Id can not be null") Integer primaryKey);

	@GetMapping(value = "/fileDownload"/* , consumes = "application/octet-stream" */)
	@Operation(summary = "Download Single Document Common api", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_APPLICANT_DOCUMENT_READ" }) })
	@ProductApiResponses
	ResponseEntity<Document> fileDownload(
			@RequestParam(required = false, name = "documentId") @NotNull(message = "documentId can not be null") @Min(value = 0, message = "documentId must be greater than 0") Integer documentId);

	@Operation(summary = "Get Total Count Of My Documents Folder And Files", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_APPLICANT_DOCUMENT_READ" }) })
	@ProductApiResponses
	@GetMapping(value = "countOfMyDocuments")
	Integer countOfMyDocuments(@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(required = false, name = "searchText") String searchText);

	@Operation(summary = "Get My Documents Folder And Files", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_APPLICANT_DOCUMENT_READ" }) })
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
	@Operation(summary = "get folder by reference value and/or reference type", security = {
			@SecurityRequirement(name = "default", scopes = { "ROLE_API_APPLICANT_DOCUMENT_READ" }) })
	@ProductApiResponses
	SubFolder getSubFolderByReferenceValueAndType(@RequestParam(name = "referenceType") String referenceType,
			@RequestParam(name = "referenceValue") String referenceValue);

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getActions")
	List<WorkflowActions> getActions(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "Compares Two Applicants for job Opening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_COMPARES" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "compareApplicantsForJobOpening")
	String compareApplicantsForJobOpening(
			@Valid @RequestParam(name = "applicant1_Id", required = true) Integer applicant1Id,
			@Valid @RequestParam(name = "applicant2_Id", required = true) Integer applicant2Id,
			@Valid @RequestParam(name = "jobOpeningId", required = true) Integer jobOpeningId);

	@Operation(summary = "Reccomend Applicant for Specific  Job Opening", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_RECOMMENDATION" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "ApplicantRecommendationForJobOpening")
	String applicantRecommendationForJobOpening(
			@Valid @RequestParam(name = "applicantId", required = true) Integer applicantId,
			@Valid @RequestParam(name = "jobOpeningId", required = true) Integer jobOpeningId);

	@Operation(summary = "To get List of Job Openings by  applicant Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_FIND_SUITABLE_JOB_OPENING_BY_APPLICANT_ID" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findSuitableJobOpeningByApplicantId")
	List<JobOpening> findSuitableJobOpeningByApplicantId(
			@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get List of Job Openings by  applicant Wrapper Details", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_FIND_SUITABLE_JOB_OPENING_BY_APPLICANT_WRAPPER" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "findSuitableJobOpeningByApplicantObject")
	List<JobOpening> findSuitableJobOpeningByApplicantObject(
			@RequestBody ApplicantWrapper applicantwrapper);
	
	@Operation(summary = "To review salary", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_FIND_SUITABLE_JOB_OPENING_BY_APPLICANT_ID" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "/reviewSalary")
	List<Object[]> reviewTheSalaryForJobOfferPositionAndCandidate();

	@Operation(summary = "To review job offer", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_FIND_SUITABLE_JOB_OPENING_BY_APPLICANT_ID" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "/reviewJobOffer")
	List<Object[]> reviewJobOfferForPositionAndCandidate();

	@Operation(summary = "To get List of applicant Id by created date", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_FIND_SUITABLE_JOB_OPENING_BY_APPLICANT_ID" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "/getApplicantIdByCreatedDate")
	List<Object[]> getApplicantIdByCreatedDate();
	
	
	@Operation(summary = "To create the applicant by its basic details", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_CREATE_BY_BASIC_DETAILS" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "createApplicant")
    Applicant createApplicant(@RequestBody ApplicantWrapper applicantWrapper);
	
	@Operation(summary = "To update the applicant with resume", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_UPDATE_APPLICANT_WITH_RESUME" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "updateApplicantWithResume")
	ApplicantPromptWrapper updateApplicant(@RequestParam(name="documentId",required=true) Integer documentId,
			@RequestParam(name="jobId",required = false) String jobId);

	@Operation(summary = "To update the applicant with resume in a folder", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_UPDATE_APPLICANT_WITH_RESUME" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "updateApplicantFromResumeFolder")
	ApplicantPromptWrapper updateApplicantFromResumeFolder();
    
	@Operation(summary = "To update applicant after resume details set", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_UPDATE_AFTER_RESUME_DETAILS_SET" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateApplicantAfterResumeDetailsSet")
	ApplicantPromptWrapper updateApplicantAfterResumeDetailsSet(@RequestBody ApplicantPromptWrapper applicantPromptWrapper);

	@Operation(summary = "To regenerate ProfessionalSummary based on Feed given by user", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_REGENERATE_SUMMARY" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "regenerateProfessionalSummary")
	String regenerateProfessionalSummary(@RequestBody ProfessionalSummaryWrapper professionalSummaryWrapper);
	
	@Operation(summary = "To regenerate WorkExperienceSummary based on Feed given by user", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_APPLICANT_REGENERATE_SUMMARY" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "regenerateWorkExperienceSummary")
	String regenerateWorkExperienceSummary(@RequestBody WorkExperienceSummaryWrapper workExperienceSummaryWrapper);
	
	@Operation(summary = "To populate data of applicant to employee", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_POPULATE_APPLICANT_TO_EMPLOYEE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "populateApplicantToEmployee")
	EmployeePopulateDto populateApplicantToEmployee(@RequestParam(name = "processInstanceId", required = false) String processInstanceId,
			@RequestParam(name = "applicantId", required = false) Integer applicantId);
	
	@Operation(summary = "To delete Applicant and its corresponding data",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_DELETE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteApplicantCorrespondingData")
	  String deleteApplicantCorrespondingData(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	
}
