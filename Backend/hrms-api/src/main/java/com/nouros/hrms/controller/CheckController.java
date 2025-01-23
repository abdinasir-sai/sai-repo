package com.nouros.hrms.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ResponseBody
@FeignClient(name = "CheckController", url = "${hrms.url}", path = "/Check", primary = false)
public interface CheckController {

	@Operation(summary = "To check board of directors present or not", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_CHECK_BOARD_OF_DIRECTOR" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("checkBOD")
	Boolean checkBoardOfDirectors(@RequestParam("processInstanceId") String processInstanceId);

	@Operation(summary = "To get the value on basis of User id and processinstanceId", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_GET_VALUE_FOR_DESIGNATION" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("getDesignation")
	Integer getValueForDesignation(@RequestParam("processInstanceId") String processInstanceId,@RequestParam("userId")Integer userId);
	
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("getJobLevel")
	String getJobLevelForEmployee();
	
	
	@Operation(summary = " Get Value Employment Type Check by Employee Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_GET_VALUE_FOR_DESIGNATION" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("getEmploymentTypeCheck")
	Integer getEmploymentTypeCheck(@RequestParam("employeeNationalIdentificationType") String employeeNationalIdentificationType);

}
