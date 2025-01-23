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

import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;
import com.nouros.hrms.wrapper.SelfAssessmentWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeReviewController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to EmployeeReview.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {EmployeeReview.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new EmployeeReview with the given request body and returns the created EmployeeReview.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of EmployeeReview with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of EmployeeReview based on the RSQL filter and returns the list of EmployeeReview matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the EmployeeReview by ID and returns the EmployeeReview if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing EmployeeReview with the given id with the request body and returns the updated EmployeeReview.
delete(@PathVariable("id") Long id) : deletes the EmployeeReview with the given id and returns the deleted EmployeeReview.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeReviewController", url = "${hrms.url}", path = "/EmployeeReview", primary = false)
public interface EmployeeReviewController {

  @Operation(summary = "Creates a new EmployeeReview",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeReview create(@Valid @RequestBody EmployeeReview employeeReview);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of EmployeeReview with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<EmployeeReview> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given EmployeeReview",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeReview update(@Valid @RequestBody EmployeeReview employeeReview);

  @Operation(summary = "To delete the given EmployeeReview by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get EmployeeReview by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  EmployeeReview findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all EmployeeReview by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEREVIEW_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<EmployeeReview> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


	
  @Operation(summary = "To create all Entry of EmployeeReview for Employee ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_CREATE_EMPLOYEEREVIEW"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "createEmployeeReview")
	  EmployeePerformanceReviewCycle createEmployeeReview(@Valid @RequestBody EmployeePerformanceReviewCycle employeePerformanceReviewCycle);

  
  @Operation(summary = "To get Competency and goal for Employee ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_GET_EMPLOYEEGOALCOMPETENCY"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "getEmployeeSelfAssessmentData")
	  SelfAssessmentWrapper getEmployeeSelfAssessmentData(@RequestParam("employeeReviewId")Integer employeeReviewId);
  
  
  @Operation(summary = "To update Competency and goal for Employee ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_UPDATE_EMPLOYEEGOALCOMPETENCY"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "updateEmployeePerfomanceData")
	  String updateEmployeePerfomanceData(@RequestBody SelfAssessmentWrapper selfAssessmentWrapper);

  
  @Operation(summary = "To update Competency and goal for Manager ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_UPDATE_EMPLOYEEGOALCOMPETENCY"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "updateManagerPerfomanceData")
	  String updateManagerPerfomanceData(@RequestBody SelfAssessmentWrapper selfAssessmentWrapper);

  @Operation(summary = "To Competency and goal for Self assessment ",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_UPDATE_EMPLOYEEGOALCOMPETENCY"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "optionalUpdatePerfomanceData")
	  String optionalUpdatePerfomanceData(@RequestBody SelfAssessmentWrapper selfAssessmentWrapper);

      
   
   
     
}
