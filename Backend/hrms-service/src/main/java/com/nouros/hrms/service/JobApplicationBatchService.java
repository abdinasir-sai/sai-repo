package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.JobApplicationBatch;
import com.nouros.hrms.service.generic.GenericService;

public interface JobApplicationBatchService extends GenericService<Integer,JobApplicationBatch>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public JobApplicationBatch create(JobApplicationBatch jobApplicationBatch);

	String deleteJobApplicationBatchById(Integer id);

}
