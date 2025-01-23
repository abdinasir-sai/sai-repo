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

import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.ChildEducationBenefitDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "ChildEducationBenefit", url = "${hrms.url}", path = "/ChildEducationBenefit", primary = false)
public interface ChildEducationBenefitController {

	@Operation(summary = "Creates a new ChildEducationBenefit",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
		  ChildEducationBenefit create(@Valid @RequestBody ChildEducationBenefit childEducationBenefit);

		  @Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

		  @Operation(summary = "To get the list of ChildEducationBenefit with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<ChildEducationBenefit> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

		  @Operation(summary = "To update the given ChildEducationBenefit",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
		  ChildEducationBenefit update(@Valid @RequestBody ChildEducationBenefit childEducationBenefit);

		  @Operation(summary = "To delete the given ChildEducationBenefit by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteById")
		  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
		  
		 
		  @Operation(summary = "To get ChildEducationBenefit by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findById")
		  ChildEducationBenefit findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

		  @Operation(summary = "To get all ChildEducationBenefit by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_CHILD_EDUCATION_BENEFIT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<ChildEducationBenefit> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

		  @GetMapping(path = "getWpsTxtFile")
		ResponseEntity<byte[]> downloadWpsFile(@RequestParam("processInstanceId")String processInstanceId);

		@GetMapping(path = "createWpsTxtFile")
		ResponseEntity<byte[]> createWpsTxtFileForChildEducationalBenefit(@RequestParam("processInstanceId")String processInstanceId);
		
		@Operation(summary = "To update ChildEducationBenefit work flow stage", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_CHILD_EDUCATION_BENEFIT_WRITE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "updateChildEducationBenefitWorkflowStage")
		ChildEducationBenefit updateChildEducationBenefitWorkflowStage(@RequestBody ChildEducationBenefitDto childEducationBenefitDto);
	
		   @Operation(summary = "To create Common wps for Child Education Benefit ", security = {
					@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
							"ROLE_API_CREATE_CHILD_EDUCATION_BENEFIT_COMMON_WPS" }) })
			@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@GetMapping(path="createCommonFile")
		   ResponseEntity<byte[]> createCommonWpsForAllChildEducationBenefit();
		   
		   @Operation(summary = "To get Common wps for Child Education Benefit ", security = {
					@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
							"ROLE_API_GET_Child_EDUCATION_BENEFIT_COMMON_WPS" }) })
			@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		   @GetMapping(path = "getAllRecordsWps")
		   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum);
		
}
