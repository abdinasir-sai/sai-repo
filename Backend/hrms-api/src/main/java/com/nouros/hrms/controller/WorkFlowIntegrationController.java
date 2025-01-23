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
@FeignClient(name = "OracleIntegrationController", path = "/WorkFlowIntegration",url = "${hrms.url}", primary = false)
public interface WorkFlowIntegrationController {

    @Operation(summary = "To get Update the work flow actions",  security = {
  	       @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
  	           "UPDATING_WORK_FLOW_ACTIONS"})
  	       })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
 	@GetMapping("updateActions")
 	public String  updateActions(@RequestParam("processInstanceId")String processInstanceId);
}
