package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobApplicationBatch;
import com.nouros.hrms.repository.JobApplicationBatchRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.JobApplicationBatchService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;

@Service
public class JobApplicationBatchServiceImpl extends AbstractService<Integer,JobApplicationBatch>
		implements JobApplicationBatchService {

	public JobApplicationBatchServiceImpl(GenericRepository<JobApplicationBatch> repository) {
		super(repository, JobApplicationBatch.class);
	}

	@Autowired
	private JobApplicationBatchRepository jobApplicationBatchRepository;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(JobApplicationBatchServiceImpl.class);

	@Override
	public JobApplicationBatch create(JobApplicationBatch jobApplicationBatch) {
		log.info("inside @class JobApplicationBatchServiceImpl @method create");
		return jobApplicationBatchRepository.save(jobApplicationBatch);
	}

	@Override
	public void softDelete(int id) {

		JobApplicationBatch jobApplicationBatch = super.findById(id);

		if (jobApplicationBatch != null) {

			JobApplicationBatch jobApplicationBatch1 = jobApplicationBatch;
			jobApplicationBatchRepository.save(jobApplicationBatch1);

		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	@Override
	public String deleteJobApplicationBatchById(Integer id) {
		log.info("inside @class JobApplicationBatchServiceImpl @method deleteJobApplicationBatchById");

		try {
			log.debug("inside @class JobApplicationBatchServiceImpl deleteJobApplicationBatchById customerId is : {}", commonUtils.getCustomerId());
			List<JobApplication> jobApplicationList = jobApplicationRepository.findByJobApplicationBatchId(id, commonUtils.getCustomerId());

			log.debug("JobApplicationList is : {} ", jobApplicationList);

			if (jobApplicationList != null && !jobApplicationList.isEmpty()) {
				for (JobApplication jobApplication : jobApplicationList) {

					log.debug("JobApplication Id is : {} ", jobApplication.getId());
					jobApplication.setJobApplicationBatch(null);
					jobApplicationRepository.save(jobApplication);
				}
			}

			jobApplicationBatchRepository.deleteById(id);

			return APIConstants.SUCCESS_JSON;

		} catch (BusinessException ex) {
			throw new BusinessException("Error while deleting JobApplicationBatch", ex.getMessage());
		}

	}

}
