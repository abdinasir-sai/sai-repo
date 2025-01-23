package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.ConfigurationScore;
import com.nouros.hrms.service.generic.GenericService;

public interface ConfigurationScoreService  extends GenericService<Integer, ConfigurationScore> {
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public ConfigurationScore create(ConfigurationScore configurationScore);
}
