package com.nouros.payrollmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@ResponseBody
@FeignClient(name = "OtherSalaryPayrollMappingController", url = "${hrms.url}", path = "/OtherSalaryPayrollMapping", primary = false)
public interface OtherSalaryPayrollMappingController {
	@Operation(summary = "Creates a new OtherSalaryPayrollMapping",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	OtherSalaryPayrollMapping create(@Valid @RequestBody OtherSalaryPayrollMapping otherSalaryPayrollMapping);

	@Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
	
	@Operation(summary = "To get the list of OtherSalaryPayrollMapping with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<OtherSalaryPayrollMapping> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
	
	@Operation(summary = "To update the given OtherSalaryPayrollMapping",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	OtherSalaryPayrollMapping update(@Valid @RequestBody OtherSalaryPayrollMapping otherSalaryPayrollMapping);
	
	@Operation(summary = "To delete the given OtherSalaryPayrollMapping by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteById")
		  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To get OtherSalaryPayrollMapping by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findById")
		  OtherSalaryPayrollMapping findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To get all OtherSalaryPayrollMapping by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OTHER_SALARY_PAYROLL_MAPPING_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<OtherSalaryPayrollMapping> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

}
