package com.nouros.hrms.integration.service.impl;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.commons.http.HttpException;
import com.enttribe.commons.http.HttpPostRequest;
import com.enttribe.commons.http.HttpRequest;
import com.enttribe.commons.http.IllegalHttpStatusException;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.JwtConstant;
import com.enttribe.platform.viewbuilder.utils.HttpUtils;
import com.enttribe.product.infra.context.ContextProvider;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.HttpResponseWrapper;
import com.nouros.hrms.wrapper.InterviewMeetingWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class VectorIntegrationServiceImpl implements VectorIntegrationService {

	private static final String MAX_TOKEN = "max_token";

	private static final String TEMPERATURE = "temperature";

	@Value("${GENERATIVE_SERVICE}")
	private String generativeService;

	private static final String EXCEPTION_MESSAGE = "exceptionMessage";

	private static final String EXCEPTION_STACK_TRACE = "exceptionStackTrace";

	private static final Logger log = LogManager.getLogger(VectorIntegrationServiceImpl.class);

	public static final String X101_SERVICE_HTTP_URL = "X101_SERVICE_HTTP_URL";
	public static final String X101_SERVICE_URL = "X101_SERVICE_HTTP_URL";
	public static final String VISION_MEETING_SERVICE_HTTP_URL = "VISION__MEETING_SERVICE_HTTP_URL";
	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";
	public static final String INSIDE_METHOD_TWO_PARAMETER = "Inside @method {} {} {}";
	public static final String ERROR_OCCURRED_THREE_PARAMETER = "Error occurred @method {} {} {} {}";
	public static final String ERROR_OCCURRED_TWO_PARAMETER = "Error occurred @method {} {} {}";
	public static final String CREATE_VECTOR_FOR_APPLICANT = "createVectorForApplicant";
	public static final String EXECUTE_PROMPT = "executePrompt";
	public static final String CREATE_OBJECTS_PROMPT = "compareObjectsPrompt";
	public static final String CREATE_REMINDER = "createReminder";
	public static final String UPLOAD_AND_EXECUTE_PROMPT = "uploadAndExecutePrompt";
	public static final String SEND_POST_REQUEST = "sendPostRequest";
	public static final String MEETING_LINK_CREATION = "meetingLinkCreation";
	public static final String SEND_POST_REQUEST_WITH_FILE = "sendPostRequestWithFile";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	public static final String ROW_JSON = "rowJson";
	public static final String ENTITY = "entity";
	public static final String COLLECTION_NAME = "collectionName";
	public static final String EMBEDDING = "embedding";
	public static final String MODEL = "model";
	public static final String JSON_MODE = "jsonMode";
	public static final String CONVERSATION_LIST = "conversationList";
	public static final String ROLE = "role";
	public static final String CONTENT = "content";
	public static final String JSON = "json";
	public static final String TEST_EMBEDDING_ADA = "text-embedding-ada-002";
	public static final String MODEL_CONFIG = "modelConfig";
	public static final String VECTOR_WRAPPER = "vectorWrapper";
	public static final String HTTP_RESPONSE = "httpResponse";
	public static final String ERROR_FROM_GENERATIVE_AS_SERVICE = "Error from Generative As Service ";
	public static final String GPT_TURBO = "gpt-35-turbo-16k";
	public static final String PROMPT = "prompt";
	public static final String VARIABLE_MAP = "variableMap";
	public static final String PROMPT_WRAPPER = "promptWrapper";

	@Override
	public String createVectorAndInsert(JSONObject rowJson, String entity) {
	    try {
	    	log.info(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT ,kv(ROW_JSON,rowJson), kv(ENTITY,entity));
	    	String methodUrl;
	    	methodUrl = generativeService+"/convertToVectorAndInsert";
	    	 JSONObject vectorWrapper = new JSONObject();
	         vectorWrapper.put(COLLECTION_NAME, "HRMS" + "_" + entity.toLowerCase());
	         vectorWrapper.put("fieldToConvert", "searchstring");
	         vectorWrapper.put("fieldToStore", EMBEDDING);
	         JSONObject modelConfig = new JSONObject();
	         modelConfig.put(MODEL, TEST_EMBEDDING_ADA);
	         vectorWrapper.put(MODEL_CONFIG, modelConfig);
	         vectorWrapper.put(ROW_JSON, rowJson);
	         vectorWrapper.put(EMBEDDING,"");
	    	 log.debug("Vector Wrapper Formed is : {} ",kv(VECTOR_WRAPPER,vectorWrapper));
	    	  String json = vectorWrapper.toString();
	    	 log.debug("Json Formed is : {} ",json);
	          Map<String, String> headers = new HashMap<>();
	          headers.put(CONTENT_TYPE, APPLICATION_JSON);
	          log.debug("Headers Formed is : {} ",kv("headers",headers));
	          HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
	        log.debug(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT,kv(HTTP_RESPONSE,httpResponse.toString()));
	    } catch (Exception e) {
			log.error(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT , Utils.getStackTrace(e));
	    }
	    return "SUCCESS_JSON";
	  }
    
    @Override
    public String searchByVector(String searchString, String entity) {
	    try {
	    	log.info(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT , kv(ENTITY,entity));
	    	String methodUrl;
	    	methodUrl = generativeService+"/searchByVector";
	    	JSONObject modelConfig = new JSONObject();
	        modelConfig.put(MODEL, TEST_EMBEDDING_ADA);
	        JSONObject params = new JSONObject();
	        params.put("nprobe", 16);
	        JSONObject searchParam = new JSONObject();
	        searchParam.put("metric_type", "L2");
	        searchParam.put("params", params);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put(COLLECTION_NAME, "HRMS" + "_" + entity.toLowerCase());
	        jsonObject.put("inputStr", searchString);
	        jsonObject.put(MODEL_CONFIG, modelConfig);
	        jsonObject.put("searchParam", searchParam);
	        jsonObject.put("topCount", 10);
	        log.debug("jsonObject Formed is : {} ",kv(VECTOR_WRAPPER,jsonObject));
	        String json = jsonObject.toString();
	        Map<String, String> headers = new HashMap<>();
	        headers.put(CONTENT_TYPE,APPLICATION_JSON);
            HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
	        log.debug(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT,kv(HTTP_RESPONSE,httpResponse.toString()));
	        return httpResponse.getResponse();
	    } catch (Exception e) {
			log.error(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT , Utils.getStackTrace(e));
	 
	      throw new BusinessException(ERROR_FROM_GENERATIVE_AS_SERVICE,e.getMessage());
	    }
	  }
    
    
    
    @Override
   	public String compareObjectsPrompt(String payload) {
   	    try {
   	    	log.info(INSIDE_METHOD_ONE_PARAMETER , CREATE_OBJECTS_PROMPT ,kv("payload",payload));
   	    	String methodUrl;
   	    	methodUrl = generativeService+"/executePrompt";
   	    	 JSONObject promptWrapper = new JSONObject();
   	    	 JSONObject modelConfig = new JSONObject();
   	    	 modelConfig.put(MODEL, GPT_TURBO);
   	    	promptWrapper.put(MODEL_CONFIG, modelConfig);
   	    	promptWrapper.put(PROMPT, payload);
   	    	 log.debug("prompt Wrapper Formed is : {} ",kv(PROMPT_WRAPPER,promptWrapper));
   	    	 String json = promptWrapper.toString();
   	          Map<String, String> headers = new HashMap<>();
   	          headers.put(CONTENT_TYPE,APPLICATION_JSON);
   	          HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
   	        log.debug(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT,kv(HTTP_RESPONSE,httpResponse.getResponse()));
   	     return httpResponse.getResponse();
   	    } catch (Exception e) {
   			log.error(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT , Utils.getStackTrace(e));
   	 
   	      throw new BusinessException(ERROR_FROM_GENERATIVE_AS_SERVICE,e.getMessage());
   	    }
   	  }
    
    @Override
	public String createVectorAndInsertForImport(JSONArray rowJson, String entity) {
	    try {
	    	log.info(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT ,kv(ROW_JSON,rowJson), kv(ENTITY,entity));
	    	String methodUrl;
	    	methodUrl = generativeService+"/convertToVectorAndInsert";
	    	 JSONObject vectorWrapper = new JSONObject();
	         vectorWrapper.put(COLLECTION_NAME, "HRMS" + "_" + entity.toLowerCase());
	         vectorWrapper.put("fieldToConvert", "searchstring");
	         vectorWrapper.put("fieldToStore", EMBEDDING);
	         JSONObject modelConfig = new JSONObject();
	         modelConfig.put(MODEL, TEST_EMBEDDING_ADA);
	         vectorWrapper.put(MODEL_CONFIG, modelConfig);
	         vectorWrapper.put(ROW_JSON, rowJson);
	         vectorWrapper.put(EMBEDDING,"");
	    	 log.debug("Vector Wrapper Formed is : {} ",kv(VECTOR_WRAPPER,vectorWrapper));
	    	 String json = vectorWrapper.toString();
	    	 log.debug("Json Formed is : {} ",json);
	          Map<String, String> headers = new HashMap<>();
	          headers.put(CONTENT_TYPE, APPLICATION_JSON);
	          HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
	        log.debug(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT,kv(HTTP_RESPONSE,httpResponse.toString()));
	    } catch (Exception e) {
			log.error(INSIDE_METHOD_ONE_PARAMETER , CREATE_VECTOR_FOR_APPLICANT , Utils.getStackTrace(e));
	    }
	    return "SUCCESS_JSON";
	  }
    
	@Override
	public  HttpResponseWrapper sendPostRequest(String url, String json, Map<String, String> headers) {
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
          
            log.info(INSIDE_METHOD_TWO_PARAMETER, SEND_POST_REQUEST, kv("getUrl", url),kv("json",json));
            
            
            putAuthorizationToken(headers);
            
            if (Objects.isNull(json)) {
                json = "";
            }
            log.info("Header Found before hitting is : {} ", headers); 
         //   HttpRequest httpPost = new HttpPostRequest (url, json).addHeader(CONTENT_TYPE, APPLICATION_JSON).ignoreCertificateErrors();
            HttpRequest httpPost = new HttpPostRequest (url, json).ignoreCertificateErrors();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
            	log.debug("Entry Map is  : {} ", entry);
                if (entry.getValue() != null) { // added by vb team //vv
                	log.debug("Entry Map Value found  is  : {} ", entry.getValue());
                	
                  if (entry.getValue().startsWith("\"")) {
                	  log.info("Add Header Inside ifcondition");
                    httpPost.addHeader(entry.getKey(), entry.getValue().replaceFirst("\"", ""));
                  } else {
                	  log.info("Add Header Inside else condition");
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                  }
                }
            }
            log.info("Header Found before hitting is : {} ", httpPost.toString());
            httpResponseWrapper.setResponse(httpPost.getString());
            httpResponseWrapper.setStatusCode(httpPost.getStatusCode());
            log.info(INSIDE_METHOD_ONE_PARAMETER, SEND_POST_REQUEST, kv("HTTP_RESPONSE_WRAPPER", httpResponseWrapper));
        }catch(IllegalHttpStatusException httpEx) {
			httpResponseWrapper.setStatusCode(httpEx.getStatusCode());
			log.error(ERROR_OCCURRED_TWO_PARAMETER, SEND_POST_REQUEST,
                    kv(EXCEPTION_STACK_TRACE, Utils.getStackTrace(httpEx)), kv(EXCEPTION_MESSAGE, httpEx.getMessage()));
			httpResponseWrapper.setResponse(httpEx.getResponseMessage());
			return httpResponseWrapper;
		}
		catch(HttpException  httpEx) {
			httpResponseWrapper.setResponse(httpEx.getMessage());
			log.error(ERROR_OCCURRED_TWO_PARAMETER, SEND_POST_REQUEST,
                    kv(EXCEPTION_STACK_TRACE, Utils.getStackTrace(httpEx)), kv(EXCEPTION_MESSAGE, httpEx.getMessage()));
			return httpResponseWrapper;
		}
        return httpResponseWrapper;
    }

	@Override
	public String uploadAndExecutePrompt(String prompt, byte[] byts, MultipartFile file) {
		 try {
	   	    	log.info(INSIDE_METHOD_ONE_PARAMETER , UPLOAD_AND_EXECUTE_PROMPT ,kv(PROMPT,prompt));
	   	    	String methodUrl;
	   	    	methodUrl = generativeService+"/uploadAndExecutePrompt";
	   	    	 JSONObject promptWrapper = new JSONObject();
	   	    	 JSONObject modelConfig = new JSONObject();
	   	    	 modelConfig.put(MODEL, GPT_TURBO);
	   	    	promptWrapper.put(MODEL_CONFIG, modelConfig);
	   	    	promptWrapper.put(PROMPT, prompt);
	   	    	 log.debug("prompt JsonObject Formed is : {} ",kv(PROMPT_WRAPPER,promptWrapper));
	   	    	 String json = promptWrapper.toString();
	   	          Map<String, String> headers = new HashMap<>();
	   	          headers.put(CONTENT_TYPE,APPLICATION_JSON);
	   	          HttpResponseWrapper httpResponse = sendPostRequestWithFile(methodUrl,json, byts,file,headers);
	   	        log.debug(INSIDE_METHOD_ONE_PARAMETER , UPLOAD_AND_EXECUTE_PROMPT,kv(HTTP_RESPONSE,httpResponse.getResponse()));
	   	     return httpResponse.getResponse();
	   	    } catch (Exception e) {
	   			log.error(INSIDE_METHOD_ONE_PARAMETER , UPLOAD_AND_EXECUTE_PROMPT , Utils.getStackTrace(e));
	   	 
	   	      throw new BusinessException(ERROR_FROM_GENERATIVE_AS_SERVICE,e.getMessage());
	   	    }
	}

	@Override
	public HttpResponseWrapper sendPostRequestWithFile(String methodUrl, String json, byte[] byts, MultipartFile file ,Map<String, String> headers) {
		HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            log.info(INSIDE_METHOD_TWO_PARAMETER, SEND_POST_REQUEST_WITH_FILE, kv("methodUrl", methodUrl),kv("json",json));
            if (Objects.isNull(json)) {
                json = "";
            }
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(methodUrl);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addTextBody("json_data", json, ContentType.APPLICATION_JSON);
            entityBuilder.addBinaryBody("file", byts, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            org.apache.http.HttpEntity  httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);
            parseResponseFromClient(httpResponseWrapper, httpClient, httpPost);
            log.info(INSIDE_METHOD_TWO_PARAMETER, SEND_POST_REQUEST, kv("HTTP_RESPONSE_WRAPPER", httpResponseWrapper));
        } catch (Exception e) {
            log.error(ERROR_OCCURRED_TWO_PARAMETER, SEND_POST_REQUEST,
                    kv(EXCEPTION_STACK_TRACE, Utils.getStackTrace(e)), kv(EXCEPTION_MESSAGE, e.getMessage()));
            throw new BusinessException("Something Went Wrong For Making Post Call");
        }
        return httpResponseWrapper;
	}

	/**
	 * @param httpResponseWrapper
	 * @param httpClient
	 * @param httpPost
	 */
	private void parseResponseFromClient(HttpResponseWrapper httpResponseWrapper, HttpClient httpClient,
			HttpPost httpPost) {
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    httpResponseWrapper.setResponse(EntityUtils.toString(response.getEntity()));
		    httpResponseWrapper.setStatusCode(response.getStatusLine().getStatusCode());
		    log.debug(INSIDE_METHOD_ONE_PARAMETER , SEND_POST_REQUEST_WITH_FILE,kv("Response Object : ",response.toString()));
		} catch (IOException e) {
		    log.error("Inside @Class VectorIntegrationServiceImpl @Method sendPostRequestWithFile generates the IOException :{}",e.getMessage());
		}
	}

//	@Override
//	public String executePrompt(String payload) {
//		try {
//			log.info(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT, kv("payload", payload));
//			String methodUrl;
//			methodUrl = generativeService + "/executePrompt";
//			JSONObject promptWrapper = new JSONObject();
//			JSONObject modelConfig = new JSONObject();
//			modelConfig.put(MODEL, GPT_TURBO);
//			promptWrapper.put(MODEL_CONFIG, modelConfig);
//			promptWrapper.put(PROMPT, payload);
//			log.debug("prompt Wrapper Formed is : {} ", kv(PROMPT_WRAPPER, promptWrapper));
//			String json = promptWrapper.toString();
//			Map<String, String> headers = new HashMap<>();
//			headers.put(CONTENT_TYPE, APPLICATION_JSON);
//			HttpResponseWrapper httpResponse = sendPostRequest(methodUrl, json, headers);
//			log.debug(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT,
//					kv(HTTP_RESPONSE, httpResponse.getResponse()));
//			return httpResponse.getResponse();
//		} catch (Exception e) {
//			log.error(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT, Utils.getStackTrace(e));
//			throw new BusinessException(ERROR_FROM_GENERATIVE_AS_SERVICE, e.getMessage());
//		}
//	}
	
	@Override
	public String executePrompt(String payload) {
		try {
			log.info(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT, kv("payload", payload));
			String methodUrl;
			methodUrl = generativeService + "/executePrompt";
			JSONObject promptWrapper = new JSONObject();
			JSONObject variableMap = new JSONObject();
			JSONObject modelConfig = new JSONObject();
			JSONArray conversationList = new JSONArray();
			JSONObject roleList = new JSONObject();
			variableMap.put(JSON,"");
			roleList.put(ROLE,"user");
			roleList.put(CONTENT,"use response key in json");
			conversationList.put(roleList);
			
			modelConfig.put(TEMPERATURE, "1f");
			modelConfig.put(MAX_TOKEN, "3000");
			modelConfig.put(JSON_MODE, Boolean.TRUE);
			
			promptWrapper.put(MODEL_CONFIG, modelConfig);
			promptWrapper.put(PROMPT, payload);
			promptWrapper.put(VARIABLE_MAP,variableMap);
			promptWrapper.put(CONVERSATION_LIST,conversationList);
			log.debug("prompt Wrapper Formed is : {} ", kv(PROMPT_WRAPPER, promptWrapper));
			String json = promptWrapper.toString();
			Map<String, String> headers = new HashMap<>();
			headers.put(CONTENT_TYPE, APPLICATION_JSON);
			HttpResponseWrapper httpResponse = sendPostRequest(methodUrl, json, headers);
			log.debug(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT,
					kv(HTTP_RESPONSE, httpResponse.getResponse()));
			return httpResponse.getResponse();
		} catch (Exception e) {
			log.error(INSIDE_METHOD_ONE_PARAMETER, EXECUTE_PROMPT, Utils.getStackTrace(e));
			throw new BusinessException(ERROR_FROM_GENERATIVE_AS_SERVICE, e.getMessage());
		}
	}

	public String meetingLinkCreation(InterviewMeetingWrapper meetingWrapper) {
		 log.info(INSIDE_METHOD_TWO_PARAMETER, MEETING_LINK_CREATION, kv("meetingWrapper", meetingWrapper.toString()));
		 try {
		    	String methodUrl;
		    	methodUrl = ConfigUtils.getString(VISION_MEETING_SERVICE_HTTP_URL)+"/create";
		    	ObjectMapper objectMapper = new ObjectMapper();
		        String json = objectMapper.writeValueAsString(meetingWrapper);
		        Map<String, String> headers = new HashMap<>();
		        headers.put(CONTENT_TYPE,APPLICATION_JSON);
		        log.debug("meetingWrapper json to be sent is : {}", json);
	            HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
		        log.debug(INSIDE_METHOD_ONE_PARAMETER , MEETING_LINK_CREATION,kv(HTTP_RESPONSE,httpResponse.toString()));
		        return httpResponse.getResponse();
		    } catch (Exception e) {
				log.error(INSIDE_METHOD_ONE_PARAMETER , MEETING_LINK_CREATION , Utils.getStackTrace(e));
		 
		      throw new BusinessException("Error from Vision Meeting Service ",e.getMessage());
		    }
	}

	@Override
	public String createReminder(Reminder reminder) {
		 log.info(INSIDE_METHOD_TWO_PARAMETER, CREATE_REMINDER, kv("reminder", reminder.toString()));
		 try {
		    	String methodUrl;
		    	methodUrl = ConfigUtils.getString(X101_SERVICE_HTTP_URL)+"/create";
		    	ObjectMapper objectMapper = new ObjectMapper();
		        String json = objectMapper.writeValueAsString(reminder);
		        Map<String, String> headers = new HashMap<>();
		        headers.put(CONTENT_TYPE,APPLICATION_JSON);
		        log.debug("reminder json to be sent is : {} ", json);
	            HttpResponseWrapper httpResponse = sendPostRequest(methodUrl,json,headers);
		        log.debug(INSIDE_METHOD_ONE_PARAMETER , CREATE_REMINDER,kv(HTTP_RESPONSE,httpResponse.toString()));
		        return httpResponse.getResponse();
		    } catch (Exception e) {
				log.error(INSIDE_METHOD_ONE_PARAMETER , CREATE_REMINDER , Utils.getStackTrace(e));
		 
		      throw new BusinessException("Error from X101 Service ",e.getMessage());
		    }
	}

	public static Map<String, String> putAuthorizationToken(Map<String, String> headers) {
		Map<String, Object> contextInfo = ContextProvider.getContextInfo();
		log.debug("Inside @Class VectorIntegrationServiceImpl @Method putAuthorizationToken contextInfo found for token is : {}", contextInfo);
		String accessTokenHeader = (String) contextInfo.get(JwtConstant.AUTH_HEADER);
		log.debug("Inside @Class VectorIntegrationServiceImpl @Method putAuthorizationToken accessTokenHeader found for token is : {}", accessTokenHeader);
		headers.put(JwtConstant.ACCESS_TOKEN_KEY, accessTokenHeader);
		
		log.debug("Inside @Class VectorIntegrationServiceImpl @Method putAuthorizationToken headers : {}", headers);
		return headers;
	}

	@Override
public String executePromptTemplate(String promptName, Map<String, String> variableMap) {
    log.info("Inside @Class PromptIntegrationServiceImpl @Method executePromptTemplate");

    String methodUrl = generativeService + "/executePromptTemplate";
    JSONObject json = new JSONObject();

    try {
        if (promptName == null || promptName.isEmpty()) {
            log.error("Prompt name is null or empty. Cannot execute template.");
            return APIConstants.FAILURE_JSON;
        }
        if (variableMap == null || variableMap.isEmpty()) {
            log.error("Variable map is null or empty. Cannot execute template.");
            return APIConstants.FAILURE_JSON;
        }

        JSONObject variableJson = new JSONObject();
        variableJson.put("json", new JSONObject(variableMap));  
        
        json.put("promptName", promptName);
        json.put("appName", "HRMS_APP_NAME");
        json.put(VARIABLE_MAP, variableJson);  

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        log.debug("Payload to send: {}", json.toString());
        log.debug("Headers: {}", headers);

        HttpResponseWrapper httpResponse = sendPostRequest(methodUrl, json.toString(), headers);
        String response = httpResponse.getResponse();

        log.debug("HTTP Response Status: {}", httpResponse.getStatusCode());
        log.debug("HTTP Response Body: {}", response);

        if (httpResponse.getStatusCode() != 200) {
            log.error("Unexpected response status: {}. Response: {}", httpResponse.getStatusCode(), response);
            return APIConstants.FAILURE_JSON;
        }

        return response;
    } catch (Exception e) {
        log.error("Exception occurs inside @Method executePromptTemplate: {}", Utils.getStackTrace(e));
        return APIConstants.FAILURE_JSON;
    }
}


}
