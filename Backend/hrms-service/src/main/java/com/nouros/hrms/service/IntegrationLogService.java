package com.nouros.hrms.service;

import com.nouros.hrms.model.IntegrationLog;

public interface IntegrationLogService {

	public IntegrationLog createIntegrationRequest(IntegrationLog httpRequest);

	
	public Integer getBatchName();
	
	public Integer updateResponse(Double processId,String finalStatus,String requestId);
	public Integer getPayRollRun(Double processId,String requestId);
}
