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

import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface CompensationStructureController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to CompensationStructure.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {CompensationStructure.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new CompensationStructure with the given request body and returns the created CompensationStructure.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of CompensationStructure with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of CompensationStructure based on the RSQL filter and returns the list of CompensationStructure matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the CompensationStructure by ID and returns the CompensationStructure if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing CompensationStructure with the given id with the request body and returns the updated CompensationStructure.
delete(@PathVariable("id") Long id) : deletes the CompensationStructure with the given id and returns the deleted CompensationStructure.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "CompensationStructureController", url = "${hrms.url}", path = "/CompensationStructure", primary = false)
public interface CompensationStructureController {

  @Operation(summary = "Creates a new CompensationStructure",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  CompensationStructure create(@Valid @RequestBody CompensationStructure compensationStructure);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of CompensationStructure with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<CompensationStructure> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given CompensationStructure",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  CompensationStructure update(@Valid @RequestBody CompensationStructure compensationStructure);

  @Operation(summary = "To delete the given CompensationStructure by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  

  @Operation(summary = "To get CompensationStructure by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  CompensationStructure findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all CompensationStructure by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_COMPENSATIONSTRUCTURE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<CompensationStructure> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


	

  @GetMapping(path = "findByTitle") 
  @Operation(summary = "Find CompensationStructure by title", tags = "CompensationStructure", description = ".")
  @ApiResponse(responseCode = "400", description = "Application was unable to complete the request because it was invalid. The request should not be retried without modification.")
  @ApiResponse(responseCode = "401", description = "Client could not be authenticated.")
  @ApiResponse(responseCode = "403", description = "Client is not authorized to make this request.")
  @ApiResponse(responseCode = "404", description = "The specified resource could not be found.")
  @ApiResponse(responseCode = "409", description = "The request was valid but was not in the appropriate state to process it. Retrying the same request later may be successful.")
  public CompensationStructure findByTitle(@Parameter(name = "title",description = "The name of title",required = true) @RequestParam(name = "title") String title);
  
     
   
   
   
   
     
}
