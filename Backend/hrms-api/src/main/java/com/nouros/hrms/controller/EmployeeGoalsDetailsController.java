
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

import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.GoalWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeGoalsMappingController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to EmployeeGoalsMapping.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {EmployeeGoalsMapping.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new EmployeeGoalsMapping with the given request body and returns the created EmployeeGoalsMapping.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of EmployeeGoalsMapping with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of EmployeeGoalsMapping based on the RSQL filter and returns the list of EmployeeGoalsMapping matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the EmployeeGoalsMapping by ID and returns the EmployeeGoalsMapping if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing EmployeeGoalsMapping with the given id with the request body and returns the updated EmployeeGoalsMapping.
delete(@PathVariable("id") Long id) : deletes the EmployeeGoalsMapping with the given id and returns the deleted EmployeeGoalsMapping.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeGoalsDetailsController", url = "${hrms.url}", path = "/EmployeeGoalsDetails", primary = false)
public interface EmployeeGoalsDetailsController {

  @Operation(summary = "Creates a new EmployeeGoalsDetails",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeGoalsDetails create(@Valid @RequestBody EmployeeGoalsDetails employeeGoalsDetails);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of EmployeeGoalsMapping with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<EmployeeGoalsDetails> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given EmployeeGoalsMapping",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeGoalsDetails update(@Valid @RequestBody EmployeeGoalsDetails employeeGoalsDetails);

  @Operation(summary = "To delete the given EmployeeGoalsMapping by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  @Operation(summary = "To get EmployeeGoalsMapping by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path= "findById")
  EmployeeGoalsDetails findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all EmployeeGoalsMapping by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEEGOALSMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<EmployeeGoalsDetails> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

 
	
  @Operation(summary = "To create all Goal of Employee",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEEGOALSDETAILS_CREATE"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "createEmployeeGoals")
	  List<EmployeeGoalsDetails> createEmployeeGoals(@RequestBody GoalWrapper goalWrapper);

	   
  @Operation(summary = "To delete Goal of Employee by Id",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEEGOALSDETAILS_DELETE"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "deleteEmployeeGoalDetailById")
  public String deleteEmployeeGoalDetailById(@RequestParam("employeeGoalId")Integer employeeGoalId);
     
     
  @Operation(summary = "To Get Goal of Employee by Employee Review Id",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEEGOALSDETAILS_GET"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "showEmployeeGoalDetailByEmployeeReviewId")
  public List<EmployeeGoalsDetails> showEmployeeGoalDetailByEmployeeReviewId(@RequestParam("employeeReviewId")Integer employeeReviewId);
    

  @Operation(summary = "To Update Goal of Employee by Employee Review Id",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEEGOALSDETAILS_UPDATE"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @PostMapping(path = "updateEmployeeGoalDetailList")
  public String updateEmployeeGoalDetailList(@RequestBody List<EmployeeGoalsDetails> employeeGoalsDetailsList);
  
   
   
     
}
