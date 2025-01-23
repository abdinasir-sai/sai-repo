package com.nouros.payrollmanagement.controller.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.integration.service.OracleIntegrationService;
import com.nouros.hrms.integration.service.impl.OracleIntegrationServiceImpl;
import com.nouros.payrollmanagement.controller.OracleIntegrationController;

@RestController
@RequestMapping("OracleIntegration")
public class OracleIntegrationControllerImpl implements OracleIntegrationController {

	@Autowired
	OracleIntegrationService oracleIntegrationService;
	
	private static final Logger log = LogManager.getLogger(OracleIntegrationServiceImpl.class);
	
	@Override
	public String getAccessTokenForOracleApi()
	{
		return oracleIntegrationService.getAccessTokenForOracleApi();
	}
	
	@Override
	public String  updateOCILedgerData(Integer id)
	{
		return oracleIntegrationService.OracleLedgerJson(id);
	}

	@Override
	public String updateFinalResponse(Double processId, String status,String requestId) {
		 
		 return oracleIntegrationService.updateFinalResponse(processId, status,requestId);
	}

    @Override
    public String getFirstOCILedgerDataEntry(Integer payrollId)
    {
       log.info("Inside @class OracleIntegrationControllerImpl @method getFirstOCILedgerDataEntry ");	
       return oracleIntegrationService.getFirstOCILedgerDataEntry(payrollId);
    }

}
