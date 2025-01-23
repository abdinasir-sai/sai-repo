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

import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "ApplicantResumeController", url = "${hrms.url}", path = "/ApplicantResume", primary = false)
public interface ApplicantResumeController {

	@Operation(summary = "Creates a new Applicant Resume ",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	      ApplicantResume create(@Valid @RequestBody ApplicantResume applicantResume);

		  @Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

		  @Operation(summary = "To get the list of Applicant Resumes with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<ApplicantResume> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

		  @Operation(summary = "To update the given Applicant Resume",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
		  ApplicantResume update(@Valid @RequestBody ApplicantResume applicantResume);

		  @Operation(summary = "To delete the given Applicant Resume by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteById")
		  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
		  
		  @Operation(summary = "To delete all the Applicant Resume",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "deleteAll")
		  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

		  @Operation(summary = "To get Applicant Resume by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findById")
		  ApplicantResume findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

		  @Operation(summary = "To get all Applicant Resume by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_APPLICANT_RESUME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<ApplicantResume> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

		
	
		  @Operation(summary = "To get all Applicant Resume Objects by applicant Id",  security = {
			      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
			          "ROLE_API_APPLICANT_RESUME_READ"})
			      })
			  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
			  @GetMapping(path = "findAllApplicantResumeByApplicantId")
			  List<ApplicantResume> findAllApplicantResumeByApplicantId(@Valid @RequestParam(name = "applicantId", required = true) Integer applicantId);
		  
}
