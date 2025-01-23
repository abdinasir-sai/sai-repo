package com.nouros.payrollmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "OtherSalaryComponentController", url = "${hrms.url}", path = "/OtherSalaryComponent", primary = false)
public interface OtherSalaryComponentController {

	@Operation(summary = "Creates a new OtherSalaryComponent",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	OtherSalaryComponent create(@Valid @RequestBody OtherSalaryComponent employeeUnstructuredSalary);

	@Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
	
	@Operation(summary = "To get the list of OtherSalaryComponent with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<OtherSalaryComponent> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
	
	@Operation(summary = "To update the given OtherSalaryComponent",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	OtherSalaryComponent update(@Valid @RequestBody OtherSalaryComponent employeeUnstructuredSalary);
	
	@Operation(summary = "To delete the given OtherSalaryComponent by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteById")
		  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	  @Operation(summary = "To delete all the OtherSalaryComponent",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "deleteAll")
		  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	
	@Operation(summary = "To get OtherSalaryComponent by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findById")
		  OtherSalaryComponent findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To get all OtherSalaryComponent by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_COMPONENT_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<OtherSalaryComponent> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);



		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "getActions")
		  List<WorkflowActions> getActions(
		      @Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
     
		  @Operation(summary = "To upload the OtherSalaryComponent files",  security = {
			      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
			          "ROLE_API_OTHER_SALARY_COMPONENT_WRITE"})
			      })
			  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
			  @PostMapping(path = "uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		OtherSalaryComponent updateFile(@RequestParam(name="file") MultipartFile file, @RequestParam(required = false, name = "id") Integer id);
		  
}
