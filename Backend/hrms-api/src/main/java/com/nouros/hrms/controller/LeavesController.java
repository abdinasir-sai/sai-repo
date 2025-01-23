package com.nouros.hrms.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.LeavesDto;
import com.nouros.hrms.wrapper.UnpaidLeaveWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface LeavesController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to Leaves.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {Leaves.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new Leaves with the given request body and returns the created Leaves.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of Leaves with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of Leaves based on the RSQL filter and returns the list of Leaves matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the Leaves by ID and returns the Leaves if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing Leaves with the given id with the request body and returns the updated Leaves.
delete(@PathVariable("id") Long id) : deletes the Leaves with the given id and returns the deleted Leaves.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "LeavesController", url = "${hrms.url}", path = "/Leaves", primary = false)
public interface LeavesController {

  @Operation(summary = "Creates a new Leaves",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  Leaves create(@Valid @RequestBody Leaves leaves);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of Leaves with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<Leaves> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given Leaves",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  Leaves update(@Valid @RequestBody Leaves leaves);

  @Operation(summary = "To delete the given Leaves by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get Leaves by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  Leaves findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all Leaves by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_LEAVES_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<Leaves> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


	

   
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "getUnpaidLeaveCount")
  List<UnpaidLeaveWrapper> getUnpaidLeaveCount(
		  @RequestParam(name = "fromDate", required = true) 
		  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
		  @RequestParam(name = "toDate", required = true) 
		  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
       @RequestParam(name = "employeeId", required = false) Integer employeeId);
     
     
   
  @Operation(summary = "To get Leaves by processInstanceId",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_LEAVES_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "updateLeaveBalanceByProcessInstanceId")
	  String updateLeaveBalanceProcessInstanceId(@Valid @RequestParam(name = "processInstanceId", required = true) String processInstanceId);
   
  @Operation(summary = "To update leaves work flow stage", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_LEAVES_UPDATE_WORK_FLOW_STAGE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "updateLeavesWorkflowStage")
  Leaves updateLeavesWorkflowStage(@RequestBody LeavesDto leavesDto);
  
  @Operation(summary = "To update leaves after cancel", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_LEAVES_UPDATE_WORK_FLOW_STAGE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "cancelLeaves ")
String cancelLeaves(@RequestParam("processInstanceId")String processInstanceId);
  
  @Operation(summary = "To Delete leave  ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_LEAVES_DELETE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteLeaveByLeaveId ")
 String deleteLeaveByLeaveId(@RequestParam("leaveId")Integer leaveId);  
     
}
