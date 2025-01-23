package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.TimeLogs;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.TimeSheetWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface TimeLogsController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to TimeLogs.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {TimeLogs.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new TimeLogs with the given request body and returns the created TimeLogs.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of TimeLogs with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of TimeLogs based on the RSQL filter and returns the list of TimeLogs matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the TimeLogs by ID and returns the TimeLogs if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing TimeLogs with the given id with the request body and returns the updated TimeLogs.
delete(@PathVariable("id") Long id) : deletes the TimeLogs with the given id and returns the deleted TimeLogs.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "TimeLogsController", url = "${hrms.url}", path = "/TimeLogs", primary = false)
public interface TimeLogsController {

  @Operation(summary = "Creates a new TimeLogs",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  TimeLogs create(@Valid @RequestBody TimeLogs timeLogs);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of TimeLogs with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<TimeLogs> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given TimeLogs",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  TimeLogs update(@Valid @RequestBody TimeLogs timeLogs);

  @Operation(summary = "To delete the given TimeLogs by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get TimeLogs by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  TimeLogs findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all TimeLogs by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_TIMELOGS_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<TimeLogs> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);




  @GetMapping("/timeLogsId")
  ResponseEntity<Double> calculateHours(@RequestParam Integer timeLogsId);
  
   
  @Operation(summary = "Get TimeLogs by Employee Id and Week Number",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_TIMELOGS_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping("/getTimeLogsByEmpIdAndWeekNumber")
  List<TimeLogs> getTimeLogsByEmpIdAndWeekNumber(@RequestParam(name = "empId", required = true) Integer empId,@RequestParam(name = "weekNumber", required = true) Integer weekNumber);

  
  @Operation(summary = "Get TimeLogs and TimeSheet Details",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_TIMELOGS_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping("/getTimeLogsDetails")
  ResponseEntity<String> getTimeLogsDetails(@RequestParam(name = "empId", required = false) Integer empId,@RequestParam(name = "weekNumber", required = true) Integer weekNumber);
  
  @Operation(summary = "Get TimeLogs and TimeSheet Details",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_TIMELOGS_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping("/getTimeLogsDetailsByTimeSheet")
  ResponseEntity<String> getTimeLogsDetailsByTimeSheet(@RequestParam(name = "timeSheetId", required = true) Integer timeSheetId);
  
  @Operation(summary = "Creates or updates new TimeLogs and timesheet",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_TIMELOGS_WRITE"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "createOrUpdateTimeLogs", consumes = MediaType.APPLICATION_JSON_VALUE)
	  List<TimeLogs> createOrUpdateTimeLogs(@Valid @RequestBody TimeSheetWrapper timeSheetWrapper);
  
}
