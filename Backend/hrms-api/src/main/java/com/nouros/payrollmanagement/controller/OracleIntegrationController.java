package com.nouros.payrollmanagement.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ResponseBody
@FeignClient(name = "OracleIntegrationController", path = "/OracleIntegration",url = "${hrms.url}", primary = false)
public interface OracleIntegrationController {

	

    @Operation(summary = "To get token of oracle api",  security = {
       @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
           "ROLE_API_ORACLE_INTEGRATION_TOKEN"})
       })
   @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("getAccessTokenForOracleApi")
	public String getAccessTokenForOracleApi();
	
    
    @Operation(summary = "To get JSON of Payroll",  security = {
    	       @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    	           "SENDING_JSON_OF_PAYROLL"})
    	       })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("updateOCILedgerData")
	public String  updateOCILedgerData(@RequestParam("id")Integer id);
    
    @Operation(summary = "To get Update the final reponse ",  security = {
 	       @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
 	           "UPDATING_FINAL_RESPONSE"})
 	       })
 @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("updateFinalReponse")
	public String  updateFinalResponse(@RequestParam("processId")Double processId , @RequestParam("status")String status,@RequestParam("requestId")String requestId);
    
    
    @Operation(summary = "To get JSON of Payroll for First Entry",  security = {
 	       @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
 	           "SENDING_JSON_OF_PAYROLL"})
 	       })
 @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping("getFirstOCILedgerDataEntry")
	public String  getFirstOCILedgerDataEntry(@RequestParam("payrollId")Integer payrollId);

 
}
