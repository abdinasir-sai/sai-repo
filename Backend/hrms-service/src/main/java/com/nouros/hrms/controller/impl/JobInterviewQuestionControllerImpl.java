package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.nouros.hrms.controller.JobInterviewQuestionController;
import com.nouros.hrms.model.JobInterviewQuestion;
import com.nouros.hrms.service.JobInterviewQuestionService;

import jakarta.validation.Valid;

public class JobInterviewQuestionControllerImpl implements JobInterviewQuestionController{

	
	@Autowired
	private JobInterviewQuestionService jobInterviewQuestionService;
	
	@Override
	public JobInterviewQuestion create(@Valid JobInterviewQuestion jobInterviewQuestion) {
		return jobInterviewQuestionService.create(jobInterviewQuestion);
	}

	@Override
	public Long count(String filter) {
		return jobInterviewQuestionService.count(filter);
	}

	@Override
	public List<JobInterviewQuestion> search(String filter, Integer offset, Integer size,
			String orderBy, String orderType) {
		return jobInterviewQuestionService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		jobInterviewQuestionService.deleteById(id);
	}

	@Override
	public JobInterviewQuestion findById(@Valid Integer id) {
		return jobInterviewQuestionService.findById(id);
	}

	@Override
	public List<JobInterviewQuestion> findAllById(@Valid List<Integer> id) {
		return jobInterviewQuestionService.findAllById(id);
	}


}
