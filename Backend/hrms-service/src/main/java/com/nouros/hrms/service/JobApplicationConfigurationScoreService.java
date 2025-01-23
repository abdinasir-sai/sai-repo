package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.JobApplicationConfigurationScore;
import com.nouros.hrms.service.generic.GenericService;

public interface JobApplicationConfigurationScoreService extends GenericService<Integer, JobApplicationConfigurationScore> {

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public JobApplicationConfigurationScore create(JobApplicationConfigurationScore jobApplicationConfigurationScore);
}
