package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.Nationality;
import com.nouros.hrms.repository.NationalityRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.NationalityService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class NationalityServiceImpl extends AbstractService<Integer,Nationality> implements NationalityService {

	public NationalityServiceImpl(GenericRepository<Nationality> repository) {
		super(repository, Nationality.class);
	}
	
	@Autowired
	private NationalityRepository nationalityRepository;

	private static final Logger log = LogManager.getLogger(NationalityServiceImpl.class);

	@Override
	public Nationality create(Nationality nationality) {
		log.info("inside @class NationalityServiceImpl @method create");
		return nationalityRepository.save(nationality);
	}
	
	@Override
	public void softDelete(int id) {

		Nationality nationality = super.findById(id);

		if (nationality != null) {

			Nationality nationality1 = nationality;
			nationalityRepository.save(nationality1);

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
	
}
