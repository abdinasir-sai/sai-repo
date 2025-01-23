/* (C)2024 */
package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.Relocation;
import com.nouros.hrms.repository.RelocationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.RelocationService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class RelocationServiceImpl extends AbstractService<Integer, Relocation> implements RelocationService {
	
	
	private static final Logger log = LogManager.getLogger(RelocationServiceImpl.class);

	public RelocationServiceImpl(GenericRepository<Relocation> repository) {
		super(repository, Relocation.class);
	}

	@Autowired
	private RelocationRepository relocationRepository;

	@Override
	public void softDelete(int id) {
		Optional<Relocation> relocation = relocationRepository.findById(id);
		if (relocation.isPresent()) {
			Relocation relocation1 = relocation.get();
			relocation1.setDeleted(true);
			relocationRepository.save(relocation1);
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
	public Relocation create(Relocation relocation) {
		log.info("Inside @class RelocationServiceImpl @method create");
		return relocationRepository.save(relocation);
	}
}
