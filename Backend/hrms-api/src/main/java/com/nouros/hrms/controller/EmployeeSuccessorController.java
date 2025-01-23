package com.nouros.hrms.controller;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

 import com.nouros.hrms.model.EmployeeSuccessor;
import com.nouros.hrms.model.Successor;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.EmployeeSuccessorWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "EmployeeSuccessorController", url = "${hrms.url}", path = "/EmployeeSuccessor", primary = false)
public interface EmployeeSuccessorController {

	  @Operation(summary = "Creates a new Employee Successor",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
		  EmployeeSuccessor create(@Valid @RequestBody EmployeeSuccessor employeeSuccessor);

		  @Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

		  @Operation(summary = "To get the list of Employee Successor with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<EmployeeSuccessor> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

		  @Operation(summary = "To update the given Employee Successor",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
		  EmployeeSuccessor update(@Valid @RequestBody EmployeeSuccessor employeeSuccessor);

		  @Operation(summary = "To delete the given Employee Successor by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteById")
		  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
		  

		  @Operation(summary = "To get Employee Successor by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findById")
		  EmployeeSuccessor findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

		  @Operation(summary = "To get all Employee Successor by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_EMPLOYEESUCCESSOR_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<EmployeeSuccessor> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

		  @Operation(summary = "To get potential candidate by list",  security = {
			      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
			          "ROLE_API_EMPLOYEESUCCESSOR_READ"})
			      })
			  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
			  @GetMapping(path = "getPotentialCandidateList")
		  List<EmployeeSuccessorWrapper> getPotentialCandidateList(@RequestParam("designationId") Integer designationId);
}
