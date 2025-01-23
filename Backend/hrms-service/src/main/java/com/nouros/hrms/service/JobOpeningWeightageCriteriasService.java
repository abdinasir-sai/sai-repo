package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.service.generic.GenericService;

import jakarta.validation.Valid;

public interface JobOpeningWeightageCriteriasService extends GenericService<Integer,JobOpeningWeightageCriterias>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
	
	public JobOpeningWeightageCriterias create(JobOpeningWeightageCriterias jobOpeningWeightageCriterias);

	List<JobOpeningWeightageCriterias> getAllWeightageCriteriasByJobOpening(Integer id);

	List<JobOpeningWeightageCriterias> createInBatch(
			@Valid List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList);
}
