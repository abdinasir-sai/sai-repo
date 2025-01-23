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
import com.nouros.hrms.controller.JobApplicationBatchController;
import com.nouros.hrms.model.JobApplicationBatch;
import com.nouros.hrms.service.JobApplicationBatchService;

@Primary
@RestController
@RequestMapping("/JobApplicationBatch")
public class JobApplicationBatchControllerImpl implements JobApplicationBatchController{

	
	private static final Logger log = LogManager.getLogger(JobApplicationBatchControllerImpl.class);

	  @Autowired
	  private JobApplicationBatchService jobApplicationBatchService;

	  @Override
	  @TriggerBPMN(entityName = "JobApplicationBatch", appName = "HRMS_APP_NAME")
	  public JobApplicationBatch create(JobApplicationBatch jobApplicationBatch) {
		  log.info("inside @class JobApplicationBatchControllerImpl @method create");
	    return jobApplicationBatchService.create(jobApplicationBatch);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return jobApplicationBatchService.count(filter);
	  }

	  @Override
	  public List<JobApplicationBatch> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return jobApplicationBatchService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public JobApplicationBatch update(JobApplicationBatch jobApplicationBatch) {
	    return jobApplicationBatchService.update(jobApplicationBatch);
	  }

	  @Override
	  public JobApplicationBatch findById(Integer id) {
	    return jobApplicationBatchService.findById(id);
	  }

	  @Override
	  public List<JobApplicationBatch> findAllById(List<Integer> id) {
	    return jobApplicationBatchService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  jobApplicationBatchService.deleteById(id);
	  }


	@Override
	public String deleteJobApplicationBatchById(Integer id) {
		return jobApplicationBatchService.deleteJobApplicationBatchById(id);
	}
	
}
