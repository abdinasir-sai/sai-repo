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

import com.nouros.hrms.model.Department;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface DepartmentController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to Department.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {Department.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new Department with the given request body and returns the created Department.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of Department with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of Department based on the RSQL filter and returns the list of Department matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the Department by ID and returns the Department if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing Department with the given id with the request body and returns the updated Department.
delete(@PathVariable("id") Long id) : deletes the Department with the given id and returns the deleted Department.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "DepartmentController", url = "${hrms.url}", path = "/Department", primary = false)
public interface DepartmentController {

  @Operation(summary = "Creates a new Department",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  Department create(@Valid @RequestBody Department department);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of Department with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<Department> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given Department",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  Department update(@Valid @RequestBody Department department);

  @Operation(summary = "To delete the given Department by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get Department by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  Department findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  
  @Operation(summary = "To get Department by Name",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_DEPARTMENT_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "getAllChildDepartments")
  List<DepartmentDetailsWrapper> getAllChildDepartments(@Valid @RequestParam(name = "name", required = true) String name);

  @Operation(summary = "To get all Department by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_DEPARTMENT_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<Department> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


  @Operation(summary = "To send notification to all department leads ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_SEND_DEPARTMENTLEADS_NOTIFICATION"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "sendNotificationtToDepartmentLeads")
	  Integer sendNotificationtToDepartmentLeads();
   
     
     
   
   
   
   
     
}
