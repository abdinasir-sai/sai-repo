package com.nouros.hrms.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.IntegrationLog;
import com.nouros.hrms.repository.IntegrationLogRepository;
import com.nouros.hrms.service.IntegrationLogService;
import com.nouros.hrms.util.report.CommonUtils;


@Service
public class IntegrationLogServiceImpl implements IntegrationLogService {

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String DATA_NOT_FOUND="Data not found ";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final Integer OK = 200;
    public static final String EXCEPTION_LABEL_TAG = "exceptionLabelTag";
    public static final String EXCEPTION_OCCURED = "Error occured @method {} ";
    public static final String INSIDE_METHOD = "Inside @method {} ";
    public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {}";
    public static final String INSIDE_METHOD_TWO_PARAMETER = "Inside @method {} {}";
    public static final String SUCCESS_JSON = "{\"status\":\"success\"}";
	
    private static ObjectMapper objectMapper = null;
    
    @Autowired
    private IntegrationLogRepository integrationRequestRepository;
    
    @Autowired
	  private CommonUtils commonUtils;
    
	private static final Logger log = LogManager.getLogger(IntegrationLogServiceImpl.class);

    
	@Override
	  @Transactional
	  public IntegrationLog createIntegrationRequest(IntegrationLog integrationRequest) {
	    try {
	      log.info(INSIDE_METHOD, "createIntegrationRequest");
	      integrationRequest.setCreatedTime(new Date());
	      return integrationRequestRepository.save(integrationRequest);
	    } catch (Exception ex) {
	      log.error("Error occured inside  @Method createIntegrationRequest : {} ", Utils.getStackTrace(ex),ex);
	      throw new BusinessException(SOMETHING_WENT_WRONG);
	    }
	  }

 
 
 		public static String getJsonString(Map<String, String> map) {
			  log.info(INSIDE_METHOD , "getJsonString");
		    try {
		      return getObjectMapper().writeValueAsString(map);
		    } catch (IOException e) {
		      log.error(EXCEPTION_OCCURED, "@getJsonString", EXCEPTION_LABEL_TAG+"{}", Utils.getStackTrace(e), e);
		      throw new BusinessException("Invalid map to read");
		    }
		  }
		
		  public static ObjectMapper getObjectMapper() {
			  log.info(INSIDE_METHOD , "getObjectMapper");
		    if (!(objectMapper instanceof ObjectMapper)) {
		      objectMapper = new ObjectMapper();
		    }
		    return objectMapper;
		  }
 			public Integer getBatchName()
			{
				log.info("Inside IntegrationLog @method getBatchName ");
				log.debug(" Inside @getBatchName  customerId is : {}", commonUtils.getCustomerId());
				try
				{
				  if(integrationRequestRepository.getBatchName(commonUtils.getCustomerId())==null)
					  return 6000;
				 
				 
					Integer value  = integrationRequestRepository.getBatchName(commonUtils.getCustomerId());
					return value;
				}
				catch(Exception e)
				{
				log.error("Inside IntegrationLog @method getBatchName  :{} :{} ",e.getMessage(),Utils.getStackTrace(e));	
				throw new BusinessException();
				}
				
			}
 			
 		@Override	
 		public Integer updateResponse(Double processId,String finalStatus,String requestId)
 		{
 			log.info("Inside IntegrationLog @method updateResponse ");
 			log.debug(" Inside @updateResponse  customerId is : {}", commonUtils.getCustomerId());
 			try
 			{
 				Integer value = integrationRequestRepository.updateStatus(finalStatus,processId,requestId, commonUtils.getCustomerId());
 				log.debug("The Updated rows are :{}",value);
 				return value;
 			}
 			catch(Exception e)
			{
			log.error("Inside IntegrationLog @method updateResponse  :{} :{} ",e.getMessage(),Utils.getStackTrace(e));	
			throw new BusinessException();
			}
 		}
 		
 		public Integer getPayRollRun(Double processId,String requestId)
 		{
 			log.info("inside @class IntegrationLog @method getPayRollRun ");
 			log.debug(" Inside @getPayRollRun  customerId is : {}", commonUtils.getCustomerId());
 			try
 			{		
 				Integer payrollId = integrationRequestRepository.getPayRollId(processId, requestId,commonUtils.getCustomerId());
 				return payrollId;
 			}
 			catch(Exception e)
 			{
 				log.error("Inside IntegrationLog @method getPayRollRun  :{} :{} ",e.getMessage(),Utils.getStackTrace(e));	
 				throw new BusinessException(); 				
 			}
 		}

}
