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

import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeDependentDetailsController defines several endpoints
 * that can be accessed using the Spring Web's FeignClient to interact with the
 * service related to EmployeeDependentDetails.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {EmployeeDependentDetails.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new
 * EmployeeDependentDetails with the given request body and returns the created
 * EmployeeDependentDetails. count(@RequestParam(name = APIConstants.QUERY,
 * required = false) String filter) : returns the count of
 * EmployeeDependentDetails with the RSQL query in the filter parameter.
 * search(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true)
 * Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required =
 * true) Integer size, @RequestParam(name = APIConstants.SORT, required = false)
 * String sort): searches for the list of EmployeeDependentDetails based on the
 * RSQL filter and returns the list of EmployeeDependentDetails matching the
 * criteria with the pagination and sorting information.
 * findById(@PathVariable("id") Long id) : searches for the
 * EmployeeDependentDetails by ID and returns the EmployeeDependentDetails if
 * found. update(@PathVariable("id") Long id, @Valid @RequestBody Employee
 * employee) : updates the existing EmployeeDependentDetails with the given id
 * with the request body and returns the updated EmployeeDependentDetails.
 * delete(@PathVariable("id") Long id) : deletes the EmployeeDependentDetails
 * with the given id and returns the deleted EmployeeDependentDetails. It also
 * includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "EmployeeDependentDetailsController", url = "${hrms.url}", path = "/EmployeeDependentDetails", primary = false)
public interface EmployeeDependentDetailsController {


	@Operation(summary = "Creates a new EmployeeDependentDetails", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeDependentDetails create(@Valid @RequestBody EmployeeDependentDetails employeeDependentDetails);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of EmployeeDependentDetails with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<EmployeeDependentDetails> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given EmployeeDependentDetails", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeDependentDetails update(@Valid @RequestBody EmployeeDependentDetails employeeDependentDetails);

	@Operation(summary = "To delete the given EmployeeDependentDetails by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get EmployeeDependentDetails by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	EmployeeDependentDetails findById(
			@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all EmployeeDependentDetails by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<EmployeeDependentDetails> findAllById(
			@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@Operation(summary = "To get Employee Dependent Details  by primary Id or userId of Employee", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getSelfEmployeeDependentDetails")
	List<EmployeeDependentDetails> getSelfEmployeeDependentDetails(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id,
			@Valid @RequestParam(name = "userId", required = false) Integer userId);
	
 	  @Operation(summary = "Update Self Employee Dependent Details ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    "ROLE_API_EMPLOYEEDEPENDENTDETAILS_WRITE"})})
  @PostMapping(path = "updateSelfEmployeeDependentDetails")
 	List<EmployeeDependentDetails> updateSelfEmployeeDependentDetails(@RequestBody List<EmployeeDependentDetails> employeeDependentDetailsList);

}
