package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.PerformanceSkills;
import com.nouros.hrms.repository.PerformanceSkillsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.PerformanceSkillsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class PerformanceSkillsServiceImpl extends AbstractService<Integer,PerformanceSkills>
		implements PerformanceSkillsService {

	public PerformanceSkillsServiceImpl(GenericRepository<PerformanceSkills> repository) {
		super(repository, PerformanceSkills.class);
	}

	private static final Logger log = LogManager.getLogger(PerformanceSkillsServiceImpl.class);

	@Autowired
	private PerformanceSkillsRepository performanceSkillsRepository;

	/**
	 * Creates a new Performance Skills.
	 *
	 * @param skill The Performance Skills object to create.
	 * @return The created PerformanceSkills object.
	 */
	@Override
	public PerformanceSkills create(PerformanceSkills performanceSkills) {
		log.info("inside @class PerformanceSkillsServiceImpl @method create");
		return performanceSkillsRepository.save(performanceSkills);
	}
}
