package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.JobApplicationConfigurationScoreController;
import com.nouros.hrms.model.JobApplicationConfigurationScore;
import com.nouros.hrms.service.JobApplicationConfigurationScoreService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/JobApplicationConfigurationScore")
public class JobApplicationConfigurationScoreControllerImpl implements JobApplicationConfigurationScoreController {
	
	private static final Logger log = LogManager.getLogger(JobApplicationConfigurationScoreControllerImpl.class);
	
	@Autowired
	 private JobApplicationConfigurationScoreService jobApplicationConfigurationScoreService;
	
	  @Override
	  @TriggerBPMN(entityName = "JobApplicationConfigurationScore", appName = "HRMS_APP_NAME")
	public JobApplicationConfigurationScore create (JobApplicationConfigurationScore jobApplicationConfigurationScore)
	{
		log.info("inside @class JobApplicationConfigurationScoreControllerImpl  @method create");
		return jobApplicationConfigurationScoreService.create(jobApplicationConfigurationScore);
		
	}

	@Override
	public Long count(String filter) {
		
		return jobApplicationConfigurationScoreService.count(filter);
	}

	@Override
	public List<JobApplicationConfigurationScore> search(String filter, @Valid Integer offset, @Valid Integer size,
			String orderBy, String orderType) {
		
		return jobApplicationConfigurationScoreService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public JobApplicationConfigurationScore update(
			@Valid JobApplicationConfigurationScore jobApplicationConfigurationScore) {
		
		return  jobApplicationConfigurationScoreService.update(jobApplicationConfigurationScore);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		
		jobApplicationConfigurationScoreService.deleteById(id);
	}

	@Override
	public JobApplicationConfigurationScore findById(@Valid Integer id) {
		
		return jobApplicationConfigurationScoreService.findById(id);
	}

	@Override
	public List<JobApplicationConfigurationScore> findAllById(@Valid List<Integer> id) {
		
		return jobApplicationConfigurationScoreService.findAllById(id);
	}
	


}
