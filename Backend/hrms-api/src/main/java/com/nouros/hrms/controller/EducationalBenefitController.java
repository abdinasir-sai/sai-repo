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

import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.EducationalBenefitDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "EducationalBenefitController", url = "${hrms.url}", path = "/EducationalBenefit", primary = false)
public interface EducationalBenefitController {

	@Operation(summary = "Creates a new EducationalBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	EducationalBenefit create(@Valid @RequestBody EducationalBenefit educationalBenefit);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of EducationalBenefit with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<EducationalBenefit> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given EducationalBenefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	EducationalBenefit update(@Valid @RequestBody EducationalBenefit educationalBenefit);

	@Operation(summary = "To delete the given EducationalBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get EducationalBenefit by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	EducationalBenefit findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all EducationalBenefit by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EDUCATIONAL_BENEFIT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<EducationalBenefit> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	
	@GetMapping(path = "createWpsTxtFile")
   ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(@RequestParam("processInstanceId") String processInstanceId);
	
   @GetMapping(path = "getWpsTxtFile")
   ResponseEntity<byte[]> downloadWpsFile(@RequestParam("processInstanceId") String educationBenefitProcessInstanceId);
   
   @Operation(summary = "To update EducationalBenefit work flow stage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EDUCATIONAL_BENEFIT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateEducationalBenefitWorkflowStage")
   EducationalBenefit updateEducationalBenefitWorkflowStage(@RequestBody EducationalBenefitDto educationalBenefitDto);

   @Operation(summary = "To create Common wps for Educational Benefit ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_CREATE_EDUCATIONAL_BENEFIT_COMMON_WPS" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   @GetMapping(path="createCommonFile")
   ResponseEntity<byte[]> createCommonFileForEducationBenefit();

   @Operation(summary = "To get Common wps for Educational Benefit ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_GET_EDUCATIONAL_BENEFIT_COMMON_WPS" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   @GetMapping(path = "getAllRecordsWps")
   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum);
   
}
