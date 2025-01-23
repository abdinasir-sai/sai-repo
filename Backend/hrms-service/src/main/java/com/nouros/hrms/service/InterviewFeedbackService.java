package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.InterviewFeedback;
import com.nouros.hrms.service.generic.GenericService;

import jakarta.validation.Valid;

public interface InterviewFeedbackService  extends GenericService<Integer,InterviewFeedback> {

	void softDelete(int id);
	
	void softBulkDelete(List<Integer> list);

	List<InterviewFeedback> findAllByInterviewId(Integer interviewId);
	
	InterviewFeedback setAverageInterviewScore(Integer interviewFeedbackId ,List<Integer> scoreList);
}
