package com.nouros.hrms.integration.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.wrapper.HttpResponseWrapper;
import com.nouros.hrms.wrapper.InterviewMeetingWrapper;

public interface VectorIntegrationService {
	
	HttpResponseWrapper sendPostRequest(String url, String json, Map<String, String> headers);

	String createVectorAndInsert(JSONObject rawJson, String entity);

	String createVectorAndInsertForImport(JSONArray rowJson, String entity);

	String compareObjectsPrompt(String payload);

	String searchByVector(String searchString, String entity);

	String uploadAndExecutePrompt(String prompt, byte[] byts, MultipartFile file);
	
	HttpResponseWrapper sendPostRequestWithFile(String methodUrl, String json, byte[] byts, MultipartFile file ,Map<String, String> headers);

	String executePrompt(String payload);

	String meetingLinkCreation(InterviewMeetingWrapper meetingWrapper);
	 
	String createReminder(Reminder reminder);

	String executePromptTemplate(String promptName,Map<String,String> variableMap);


}
