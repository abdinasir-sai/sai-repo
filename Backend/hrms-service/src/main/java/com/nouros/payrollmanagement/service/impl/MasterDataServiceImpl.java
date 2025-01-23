package com.nouros.payrollmanagement.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.model.MasterData;
import com.nouros.payrollmanagement.repository.MasterDataRepository;
import com.nouros.payrollmanagement.service.MasterDataService;

@Service
public class MasterDataServiceImpl extends AbstractService<Integer,MasterData> implements MasterDataService {

	protected MasterDataServiceImpl(GenericRepository<MasterData> repository) {
		super(repository, MasterData.class);
		
	}

	@Autowired
	private MasterDataRepository masterDataRepository;

	private static final Logger log = LogManager.getLogger(MasterDataServiceImpl.class);

	@Override
	public MasterData create(MasterData masterData) {
		
		return masterDataRepository.save(masterData);
	}
	
	  
 }
