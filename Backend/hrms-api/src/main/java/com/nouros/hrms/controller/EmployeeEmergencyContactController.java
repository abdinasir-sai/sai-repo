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

import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@ResponseBody
@FeignClient(name = "EmployeeEmergencyContactController", url = "${hrms.url}", path = "/EmployeeEmergencyContact", primary = false)
public interface EmployeeEmergencyContactController {


	@Operation(summary = "Creates a new EmployeeEmergencyContact", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeEmergencyContact create(@Valid @RequestBody EmployeeEmergencyContact employeeEmergencyContact);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of EmployeeEmergencyContact with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<EmployeeEmergencyContact> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given EmployeeEmergencyContact", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeEmergencyContact update(@Valid @RequestBody EmployeeEmergencyContact employeeEmergencyContact);

	@Operation(summary = "To delete the given EmployeeEmergencyContact by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get EmployeeEmergencyContact by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	EmployeeEmergencyContact findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all EmployeeEmergencyContact by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<EmployeeEmergencyContact> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


	@Operation(summary = "To get Employee Emergency Contact by primary Id or userId of Employee", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEEDEPENDENTDETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getSelfEmployeeEmergencyContact")
	EmployeeEmergencyContact getSelfEmployeeEmergencyContact(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id,
			@Valid @RequestParam(name = "userId", required = false) Integer userId);
	
 	  @Operation(summary = "Update Self Employee Emergency Contact ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    "ROLE_API_EMPLOYEE_EMERGENCY_CONTACT_WRITE"})})
  @PostMapping(path = "updateEmployeeEmergencyContact")
 	 EmployeeEmergencyContact updateEmployeeEmergencyContact(@RequestBody EmployeeEmergencyContact employeeEmergencyContact);
	
}
