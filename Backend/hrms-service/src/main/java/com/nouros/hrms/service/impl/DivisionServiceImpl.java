package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.Division;
import com.nouros.hrms.repository.DivisionRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.DivisionService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class DivisionServiceImpl extends AbstractService<Integer,Division> implements DivisionService {

	public DivisionServiceImpl(GenericRepository<Division> repository) {
		super(repository, Division.class);
	}

	private static final Logger log = LogManager.getLogger(DivisionServiceImpl.class);

	@Autowired
	private DivisionRepository divisionRepository;

	@Override
	public Division create(Division division) {
		log.info("inside @class DivisionServiceImpl @method create");
		return divisionRepository.save(division);
	}

	@Override
	public List<Division> findAll() {
		return divisionRepository.findAll();
	}

}
