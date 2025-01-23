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

import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeCompetenciesDetailsController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to EmployeeCompetenciesDetails.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {EmployeeCompetenciesDetails.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new EmployeeCompetenciesDetails with the given request body and returns the created EmployeeCompetenciesDetails.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of EmployeeCompetenciesDetails with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of EmployeeCompetenciesDetails based on the RSQL filter and returns the list of EmployeeCompetenciesDetails matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the EmployeeCompetenciesDetails by ID and returns the EmployeeCompetenciesDetails if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing EmployeeCompetenciesDetails with the given id with the request body and returns the updated EmployeeCompetenciesDetails.
delete(@PathVariable("id") Long id) : deletes the EmployeeCompetenciesDetails with the given id and returns the deleted EmployeeCompetenciesDetails.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeCompetenciesDetailsController", url = "${hrms.url}", path = "/EmployeeCompetenciesDetails", primary = false)
public interface EmployeeCompetenciesDetailsController {

  @Operation(summary = "Creates a new EmployeeCompetenciesDetails",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeCompetenciesDetails create(@Valid @RequestBody EmployeeCompetenciesDetails employeeCompetenciesDetails);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of EmployeeCompetenciesDetails with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<EmployeeCompetenciesDetails> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given EmployeeCompetenciesDetails",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeCompetenciesDetails update(@Valid @RequestBody EmployeeCompetenciesDetails employeeCompetenciesDetails);

  @Operation(summary = "To delete the given EmployeeCompetenciesDetails by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get EmployeeCompetenciesDetails by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  EmployeeCompetenciesDetails findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all EmployeeCompetenciesDetails by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEECOMPETENCIESMAPPING_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<EmployeeCompetenciesDetails> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


     
  @Operation(summary = "To delete the given EmployeeCompetenciesDetails by Employee Review Id",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEECOMPETENCIESDETAILS_GET"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "showEmployeeCompetenciesDetailsByEmployeeReviewId")
	  List<EmployeeCompetenciesDetails> showEmployeeCompetenciesDetailsByEmployeeReviewId(@RequestParam("employeeReviewId")Integer employeeReviewId);
     
}
