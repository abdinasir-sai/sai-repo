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

import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.HealthClubBenefitDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "HealthClubBenefitController", url = "${hrms.url}", path = "/HealthClubBenefit", primary = false)
public interface HealthClubBenefitController {
	@Operation(summary = "Creates a new HealthClubBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	HealthClubBenefit create(@Valid @RequestBody HealthClubBenefit healthClubBenefit);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of HealthClubBenefit with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<HealthClubBenefit> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given HealthClubBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	HealthClubBenefit update(@Valid @RequestBody HealthClubBenefit healthClubBenefit);

	@Operation(summary = "To delete the given HealthClubBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get HealthClubBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	HealthClubBenefit findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all HealthClubBenefit by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HEALTH_CLUB_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<HealthClubBenefit> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	
	@GetMapping(path = "createWpsTxtFile")
	   ResponseEntity<byte[]> createWpsTxtFileForHealthClubBenefit(@RequestParam("processInstanceId") String processInstanceId);
		
	   @GetMapping(path = "getWpsTxtFile")
	   ResponseEntity<byte[]> downloadWpsFile(@RequestParam("processInstanceId") String healthClubBenefitProcessInstanceId);
	   
	   @Operation(summary = "To update healthclubbenefit work flow stage", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_HEALTH_CLUB_BENEFIT_UPDATE_WORK_FLOW_STAGE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "updateHealthClubBenefitWorkflowStage")
	   HealthClubBenefit updateHealthClubBenefitWorkflowStage(@RequestBody HealthClubBenefitDto healthClubBenefitDto);

	   @Operation(summary = "To create Common wps for Health club Benefit ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_CREATE_HEALTH_CULB_BENEFIT_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@GetMapping(path="createCommonFile")
		   ResponseEntity<byte[]> createCommonWpsForAllHealthClubBenefit();
		   
		   @Operation(summary = "To get Common wps for Health Club Benefit ", security = {
					@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
							"ROLE_API_GET_HEALTH_CLUB_BENEFIT_COMMON_WPS" }) })
			@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		   @GetMapping(path = "getAllRecordsWps")
		   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum);
}
