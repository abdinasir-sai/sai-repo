package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.JobApplicationConfigurationScore;
import com.nouros.hrms.repository.JobApplicationConfigurationScoreRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.JobApplicationConfigurationScoreService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class JobApplicationConfigurationScoreServiceImpl extends AbstractService<Integer,JobApplicationConfigurationScore> implements JobApplicationConfigurationScoreService {

	public JobApplicationConfigurationScoreServiceImpl(GenericRepository<JobApplicationConfigurationScore> repository) {
		super(repository,JobApplicationConfigurationScore.class );
	
	}
	
	@Autowired JobApplicationConfigurationScoreRepository jobApplicationConfigurationScoreRepository;

	private static final Logger log = LogManager.getLogger(JobApplicationConfigurationScoreServiceImpl.class);
	
	@Override
	public void softDelete(int id) {
		JobApplicationConfigurationScore jobApplicationConfigurationScore = super.findById(id);

		if (jobApplicationConfigurationScore != null) {

			JobApplicationConfigurationScore jobApplicationConfigurationScore1 = jobApplicationConfigurationScore;
			jobApplicationConfigurationScoreRepository.save(jobApplicationConfigurationScore1);

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
	public JobApplicationConfigurationScore create(JobApplicationConfigurationScore jobApplicationConfigurationScore) {
		log.info("inside @class JobApplicationConfigurationScoreServiceImpl @method create");
		return jobApplicationConfigurationScoreRepository.save(jobApplicationConfigurationScore);
	}
	

}
