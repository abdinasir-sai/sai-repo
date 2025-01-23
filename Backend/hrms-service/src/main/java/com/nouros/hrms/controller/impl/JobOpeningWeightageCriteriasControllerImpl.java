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
import com.nouros.hrms.controller.JobOpeningWeightageCriteriasController;
import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.service.JobOpeningWeightageCriteriasService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/JobOpeningWeightageCriterias")
public class JobOpeningWeightageCriteriasControllerImpl implements JobOpeningWeightageCriteriasController {

	private static final Logger log = LogManager.getLogger(JobOpeningWeightageCriteriasControllerImpl.class);
	
	@Autowired
	 private JobOpeningWeightageCriteriasService jobOpeningWeightageCriteriasService;

	@Override
	  @TriggerBPMN(entityName = "JobOpeningWeightageCriterias", appName = "HRMS_APP_NAME")
	public JobOpeningWeightageCriterias create(@Valid JobOpeningWeightageCriterias jobOpeningWeightageCriterias) {
		log.info("inside @class JobOpeningWeightageCriteriasControllerImpl  @method create");
		return jobOpeningWeightageCriteriasService.create(jobOpeningWeightageCriterias);
	}

	@Override
	public Long count(String filter) {
		
		return jobOpeningWeightageCriteriasService.count(filter) ;
	}

	@Override
	public List<JobOpeningWeightageCriterias> search(String filter, @Valid Integer offset, @Valid Integer size,
			String orderBy, String orderType) {
		
		return jobOpeningWeightageCriteriasService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public JobOpeningWeightageCriterias update(@Valid JobOpeningWeightageCriterias jobOpeningWeightageCriterias) {

		return jobOpeningWeightageCriteriasService.update(jobOpeningWeightageCriterias);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		
		jobOpeningWeightageCriteriasService.deleteById(id);
		
	}

	@Override
	public JobOpeningWeightageCriterias findById(@Valid Integer id) {
		
		return jobOpeningWeightageCriteriasService.findById(id);
		
	}

	@Override
	public List<JobOpeningWeightageCriterias> findAllById(@Valid List<Integer> id) {
		
		return jobOpeningWeightageCriteriasService.findAllById(id);
	}

	@Override
	public List<JobOpeningWeightageCriterias> createInBatch(
			@Valid List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList) {
		return jobOpeningWeightageCriteriasService.createInBatch(jobOpeningWeightageCriteriasList);
	}
	
}
