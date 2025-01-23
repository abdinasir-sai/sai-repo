package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.ConfigurationScore;
import com.nouros.hrms.repository.ConfigurationScoreRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ConfigurationScoreService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class ConfigurationScoreServiceImpl  extends AbstractService<Integer,ConfigurationScore> implements ConfigurationScoreService {

	public ConfigurationScoreServiceImpl(GenericRepository<ConfigurationScore> repository) {
		super(repository, ConfigurationScore.class);
	}
	
	@Autowired
	private ConfigurationScoreRepository configurationScoreRepository;

	private static final Logger log = LogManager.getLogger(ConfigurationScoreServiceImpl.class);

	@Override
	public ConfigurationScore create(ConfigurationScore configurationScore) {
		log.info("inside @class ConfigurationScoreServiceImpl @method create");
		return configurationScoreRepository.save(configurationScore);
	}
	
	@Override
	public void softDelete(int id) {

		ConfigurationScore configurationScore = super.findById(id);

		if (configurationScore != null) {

			ConfigurationScore configurationScore1 = configurationScore;
			configurationScoreRepository.save(configurationScore1);

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
