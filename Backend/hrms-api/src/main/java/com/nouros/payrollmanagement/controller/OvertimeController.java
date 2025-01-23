package com.nouros.payrollmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@ResponseBody
@FeignClient(name = "OvertimeController", url = "${hrms.url}", path = "/Overtime", primary = false)
public interface OvertimeController {

	
	 @Operation(summary = "Creates a new Overtime",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_WRITE"})
		      })
     @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	 @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	     Overtime create( @Valid @RequestBody Overtime overtime);
	
	 
	 @Operation(summary = "To get Overtime by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_READ"})
		      })
	 @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	 @GetMapping(path = "findById")
	  Overtime findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
	 @Operation(summary = "To get all Overtime by given Ids",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "findAllById")
		  List<Overtime> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	 
	
	
	@Operation(summary = "To delete the given Overtime by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_WRITE"})
	})
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To delete all the Overtime",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "deleteAll")
		  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);
	
	
	@Operation(summary = "To get the count with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "count")
		  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
	
	@Operation(summary = "To get the list of Overtime with RSQL supported filter",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "search")
		  List<Overtime> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
		      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
		      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
		      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
		      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	
	  @Operation(summary = "To update the given Overtime",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	    Overtime update(@Valid @RequestBody Overtime overtime);
	  
	  
	  @Operation(summary = "Get Overtime Details",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_READ"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	      @GetMapping("getOvertimeDetails")
	      ResponseEntity<OvertimeDto> getOvertimeDetails(@RequestParam(name = "empId", required = false) Integer empId, @RequestParam(name = "overtimeId", required = false) Integer overtimeId, @RequestParam(name = "monthNumber", required = false) Integer monthNumber, @RequestParam(name = "year", required = false) Integer year);
	  
	  
	  @Operation(summary = "To delete OvertimeLogs associated with  Overtime by Id",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_OVERTIME_WRITE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "deleteOvertimeLogsAndOvertimeById")
		  String deleteOvertimeLogsAndOvertimeById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	 
	  
	  

}
