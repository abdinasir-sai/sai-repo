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

import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.BusinessExpenseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "BusinessExpenseController", url = "${hrms.url}", path = "/BusinessExpense", primary = false)
public interface BusinessExpenseController {

	@Operation(summary = "Creates a new BusinessExpense", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	BusinessExpense create(@Valid @RequestBody BusinessExpense businessExpense);
	
	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
	
	@Operation(summary = "To get the list of BusinessExpense with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<BusinessExpense> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
	
	@Operation(summary = "To update the given BusinessExpense", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	BusinessExpense update(@Valid @RequestBody  BusinessExpense businesssExpense);

	@Operation(summary = "To delete the given BusinessExpense by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the BusinessExpense",  security = {
		@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_BUSINESSEXPENSE_WRITE"})
		      })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "deleteAll")
	void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);
	
	
	@Operation(summary = "To get BusinessExpense by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	BusinessExpense findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all BusinessExpense by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESSEXPENSE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<BusinessExpense> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@GetMapping(path = "createWpsTxtFile")
	   ResponseEntity<byte[]> createWpsTxtFileForBusinessExpense(@RequestParam("processInstanceId") String processInstanceId);
		
	   @GetMapping(path = "getWpsTxtFile")
	   ResponseEntity<byte[]> downloadWpsFile(@RequestParam("processInstanceId") String businessExpenseProcessInstanceId);
	   
	   @Operation(summary = "To update BusinessExpense work flow stage", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_BUSINESSEXPENSE_WRITE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "updateBusinessExpenseWorkflowStage")
	   BusinessExpense updateBusinessExpenseWorkflowStage(@RequestBody BusinessExpenseDto businessExpenseDto);
	

	   @Operation(summary = "To create wps for BusinessExpense  ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_WPS_BUSINESSEXPENSE_CREATE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping("createCommonFile")
	   ResponseEntity<byte[]> createCommonWpsForBusinessExpense();
	   
	   @Operation(summary = "To download wps for BusinessExpense  ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_WPS_BUSINESSEXPENSE_GET" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path = "getAllRecordsWps")
	   ResponseEntity<byte[]> getAllRecordsWps(@RequestParam("weekNum") Integer weekNum);
	   
}
