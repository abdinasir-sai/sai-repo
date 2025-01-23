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

import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeComplianceLegalController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to EmployeeComplianceLegal.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {EmployeeComplianceLegal.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new EmployeeComplianceLegal with the given request body and returns the created EmployeeComplianceLegal.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of EmployeeComplianceLegal with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of EmployeeComplianceLegal based on the RSQL filter and returns the list of EmployeeComplianceLegal matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the EmployeeComplianceLegal by ID and returns the EmployeeComplianceLegal if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing EmployeeComplianceLegal with the given id with the request body and returns the updated EmployeeComplianceLegal.
delete(@PathVariable("id") Long id) : deletes the EmployeeComplianceLegal with the given id and returns the deleted EmployeeComplianceLegal.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeComplianceLegalController", url = "${hrms.url}", path = "/EmployeeComplianceLegal", primary = false)
public interface EmployeeComplianceLegalController {

  @Operation(summary = "Creates a new EmployeeComplianceLegal",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeComplianceLegal create(@Valid @RequestBody EmployeeComplianceLegal employeeComplianceLegal);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of EmployeeComplianceLegal with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<EmployeeComplianceLegal> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given EmployeeComplianceLegal",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeComplianceLegal update(@Valid @RequestBody EmployeeComplianceLegal employeeComplianceLegal);

  @Operation(summary = "To delete the given EmployeeComplianceLegal by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  @Operation(summary = "To delete all the EmployeeComplianceLegal",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "deleteAll")
  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

  @Operation(summary = "To get EmployeeComplianceLegal by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  EmployeeComplianceLegal findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all EmployeeComplianceLegal by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPLIANCELEGAL_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<EmployeeComplianceLegal> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

  
  @Operation(summary = "To get Employee Compliance Legal by primary Id or userId of Employee", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEECOMPLIANCELEGAL_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getSelfEmployeeComplianceLegal")
  EmployeeComplianceLegal getSelfEmployeeComplianceLegal(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id,
			@Valid @RequestParam(name = "userId", required = false) Integer userId);
	
	  @Operation(summary = "Update Self Employee Compliance Legal ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
  "ROLE_API_EMPLOYEECOMPLIANCELEGAL_WRITE"})})
@PostMapping(path = "updateEmployeeComplianceLegal")
	  EmployeeComplianceLegal updateEmployeeComplianceLegal(@RequestBody EmployeeComplianceLegal employeeComplianceLegal);
        
}
