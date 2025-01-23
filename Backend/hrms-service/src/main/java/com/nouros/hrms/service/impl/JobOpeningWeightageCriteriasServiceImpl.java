package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.repository.JobOpeningWeightageCriteriasRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.JobOpeningWeightageCriteriasService;
import com.nouros.hrms.service.generic.AbstractService;

import jakarta.validation.Valid;

@Service
public class JobOpeningWeightageCriteriasServiceImpl extends AbstractService<Integer,JobOpeningWeightageCriterias>
		implements JobOpeningWeightageCriteriasService {

	public JobOpeningWeightageCriteriasServiceImpl(
			GenericRepository<JobOpeningWeightageCriterias> repository) {
		super(repository, JobOpeningWeightageCriterias.class);
	}

	@Autowired
	private JobOpeningWeightageCriteriasRepository jobOpeningWeightageCriteriasRepository;

	private static final Logger log = LogManager.getLogger(JobOpeningWeightageCriteriasServiceImpl.class);

	@Override
	public JobOpeningWeightageCriterias create(JobOpeningWeightageCriterias jobOpeningWeightageCriterias) {
		log.info("inside @class JobOpeningWeightageCriteriasServiceImpl  @method create");
		return jobOpeningWeightageCriteriasRepository.save(jobOpeningWeightageCriterias);
	}

	@Override
	public void softDelete(int id) {
		JobOpeningWeightageCriterias jobOpeningWeightageCriterias = super
				.findById(id);

		if (jobOpeningWeightageCriterias != null) {
			JobOpeningWeightageCriterias jobOpeningWeightageCriterias1 = jobOpeningWeightageCriterias;
			jobOpeningWeightageCriteriasRepository.save(jobOpeningWeightageCriterias1);
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
	public List<JobOpeningWeightageCriterias> getAllWeightageCriteriasByJobOpening(Integer id) {
		log.info("Inside getAllWeightageCriteriasByJobOpening");
		try {
			return jobOpeningWeightageCriteriasRepository.getAllWeightageCriteriasByJobOpening(id);
		} catch (Exception e) {
			log.error("An error occurred while getting JobOpeningWeightageCriterias  by job Opening Id : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public List<JobOpeningWeightageCriterias> createInBatch(
			@Valid List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList) {
		
		log.debug("Inside @class JobOpeningWeightageCriteriasServiceImpl createInBatch list:{}",jobOpeningWeightageCriteriasList);
		List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasSavedList = new ArrayList<>();
		for(JobOpeningWeightageCriterias jobOpeningWeightageCriterias: jobOpeningWeightageCriteriasList)
		{
			JobOpeningWeightageCriterias jobOpeningWeightageCriteriasSaved = jobOpeningWeightageCriteriasRepository.save(jobOpeningWeightageCriterias);
			log.debug("Inside @class WeightageConfigurationServiceImpl weightageConfigurationsaved is :{} ",jobOpeningWeightageCriterias.getId() );
			jobOpeningWeightageCriteriasSavedList.add(jobOpeningWeightageCriterias);
		}
		log.debug("Inside @class WeightageConfigurationServiceImpl size Of list is : {} ",jobOpeningWeightageCriteriasSavedList.size());
		return jobOpeningWeightageCriteriasSavedList;
	}
	

}
