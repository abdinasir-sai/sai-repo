package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.NewHireBenefitDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "NewHireBenefitController", url = "${hrms.url}", path = "/NewHireBenefit", primary = false)
public interface NewHireBenefitController {

	@Operation(summary = "Creates a new NewHireBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	NewHireBenefit create(@Valid @RequestBody NewHireBenefit newHireBenefit);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of NewHireBenefit with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<NewHireBenefit> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given NewHireBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	NewHireBenefit update(@Valid @RequestBody NewHireBenefit newHireBenefit);

	@Operation(summary = "To delete the given NewHireBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get NewHireBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	NewHireBenefit findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all NewHireBenefit by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_NEW_HIRE_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<NewHireBenefit> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	
	@GetMapping(path = "createWpsTxtFile")
	   ResponseEntity<byte[]> createWpsTxtFileForNewHireBenefit(@RequestParam("processInstanceId") String processInstanceId);
		
	   @GetMapping(path = "getWpsTxtFile")
	   ResponseEntity<byte[]> downloadWpsFile(@RequestParam("processInstanceId") String educationBenefitProcessInstanceId);
	   
	   @Operation(summary = "To update NewHireBenefit work flow stage", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_NEW_HIRE_BENEFIT_WRITE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "updateNewHireBenefitWorkflowStage")
	   NewHireBenefit updateNewHireBenefitWorkflowStage(@RequestBody NewHireBenefitDto newHireBenefitDto);

	   @Operation(summary = "To create Common wps for New Hire Benefit ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_GET_NEW_HIRE_BENEFIT_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path = "createCommonFile")
	   ResponseEntity<byte[]> createCommonFileForNewHireBenefit();
	   
	   @Operation(summary = "To get Common wps for New Hire Benefit ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_GET_NEW_HIRE_BENEFIT_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path = "getAllRecordsWps")
	   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum);
}
