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

import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.BenefitWrapper;
import com.nouros.hrms.wrapper.HrBenefitsDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "HrBenefitsController", url = "${hrms.url}", path = "/HrBenefits", primary = false)
public interface HrBenefitsController {

	@Operation(summary = "Creates a new HrBenefits", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	HrBenefits create(@Valid @RequestBody HrBenefits hrBenefits);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of HrBenefits with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<HrBenefits> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given HrBenefits", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	HrBenefits update(@Valid @RequestBody HrBenefits hrBenefits);

	@Operation(summary = "To delete the given HrBenefits by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get HrBenefits by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	HrBenefits findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all HrBenefits by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_HR_BENEFITS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<HrBenefits> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

//	@Operation(summary = "Api to validate benefit", security = {
//			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_BENEFIT_TYPE_READ" }) })
//	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
//	@PostMapping(path = "benefitValidate")
//	String validateBenefit(@RequestBody BenefitWrapper benefitWrapper);
	
	@Operation(summary = "Api to validate benefit", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_BENEFIT_TYPE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "benefitValidate")
	String validateBenefit(@RequestBody Object benefitString);

	
	@Operation(summary = "To update HrBenefits work flow stage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_HR_BENEFITS_UPDATE_WORK_FLOW_STAGE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateHrBenefitsWorkflowStage")
	HrBenefits updateHrBenefitsWorkflowStage(@RequestBody HrBenefitsDto hrBenefitsDto);
	
	
	   @Operation(summary = "To create Common wps for Benefits ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_CREATE_BENEFIT_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path="createCommonFile")
	   ResponseEntity<byte[]> createCommonFileForBenefits(@RequestParam("benefitName")String benefitName);
	   
	   @Operation(summary = "To get Common wps for HR Benefit ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_GET_HR_BENEFIT_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path = "getAllRecordsWps")
	   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum,@RequestParam("benefitName")String benfitName);
	 
	   @Operation(summary = "To Delete Hrbenfits and its References By Id", security = {
		        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		            "ROLE_API_HR_BENEFIT_READ"})
		    })
		    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		    @GetMapping(path = "deleteHrBenefitsAndItsReferences")
		    public String deleteHrBenefitsAndItsReferences(@RequestParam("id") Integer id);
	
}
