package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "ApplicantLanguageController", url = "${hrms.url}", path = "/ApplicantLanguage", primary = false)
public interface ApplicantLanguageController {

	@Operation(summary = "Creates a new ApplicantLanguage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create")
	ApplicantLanguage create(@RequestBody ApplicantLanguage applicantLanguage);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of ApplicantLanguage with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<ApplicantLanguage> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given ApplicantLanguage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update")
	ApplicantLanguage update(@RequestBody ApplicantLanguage applicantLanguage);

	@Operation(summary = "To delete the given ApplicantLanguage by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get ApplicantLanguage by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	ApplicantLanguage findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all ApplicantLanguage by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_APPLICANT_LANGUAGE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<ApplicantLanguage> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	
	
	
	
}
