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

import com.nouros.hrms.model.BusinessPlan;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface BusinessPlanController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to BusinessPlan.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {BusinessPlan.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new BusinessPlan with the given request body and returns the created BusinessPlan.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of BusinessPlan with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of BusinessPlan based on the RSQL filter and returns the list of BusinessPlan matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the BusinessPlan by ID and returns the BusinessPlan if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing BusinessPlan with the given id with the request body and returns the updated BusinessPlan.
delete(@PathVariable("id") Long id) : deletes the BusinessPlan with the given id and returns the deleted BusinessPlan.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "BusinessPlanController", url = "${hrms.url}", path = "/BusinessPlan", primary = false)
public interface BusinessPlanController {

  @Operation(summary = "Creates a new BusinessPlan",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  BusinessPlan create(@Valid @RequestBody BusinessPlan businessPlan);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of BusinessPlan with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<BusinessPlan> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given BusinessPlan",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  BusinessPlan update(@Valid @RequestBody BusinessPlan businessPlan);

  @Operation(summary = "To delete the given BusinessPlan by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  @Operation(summary = "To delete all the BusinessPlan",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "deleteAll")
  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

  @Operation(summary = "To get BusinessPlan by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  BusinessPlan findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all BusinessPlan by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_BUSINESSPLAN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<BusinessPlan> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);



   
  @GetMapping(path = "findByTitleAndMonth") 
  @Operation(summary = "Find BusinessPlan by title and month", tags = "CompensationStructure", description = ".")
  @ApiResponse(responseCode = "400", description = "Application was unable to complete the request because it was invalid. The request should not be retried without modification.")
  @ApiResponse(responseCode = "401", description = "Client could not be authenticated.")
  @ApiResponse(responseCode = "403", description = "Client is not authorized to make this request.")
  @ApiResponse(responseCode = "404", description = "The specified resource could not be found.")
  @ApiResponse(responseCode = "409", description = "The request was valid but was not in the appropriate state to process it. Retrying the same request later may be successful.")
  public BusinessPlan findByTitleAndMonth(@Parameter(name = "title",description = "The name of title",required = true) @RequestParam(name = "title") String title,@Parameter(name = "planDate",description = "plan date",required = true) @RequestParam(name = "planDate") String planDate);
     
     
   
   
   
   
     
}
