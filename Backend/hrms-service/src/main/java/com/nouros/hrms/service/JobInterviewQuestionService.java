package com.nouros.hrms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nouros.hrms.model.JobInterviewQuestion;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.service.generic.GenericService;

public interface JobInterviewQuestionService extends GenericService<Integer,JobInterviewQuestion> {

	String createJobInterviewQuestion(JsonNode jsonNode, String departmentName);

	String createJobInterviewQuestion(JobOpening jobOpening);

}
