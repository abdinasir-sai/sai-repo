package com.nouros.hrms.integration.service;

import org.springframework.http.ResponseEntity;

public interface OracleIntegrationService{

	public String getAccessTokenForOracleApi();
	
	public String OracleLedgerJson(Integer payrollId);

	public String updateFinalResponse(Double processId, String status,String requestId);
	
	public String getFirstOCILedgerDataEntry(Integer payrollId);
}
