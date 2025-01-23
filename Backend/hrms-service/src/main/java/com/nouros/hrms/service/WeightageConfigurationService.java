package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.service.generic.GenericService;

public interface WeightageConfigurationService extends GenericService<Integer,WeightageConfiguration>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public WeightageConfiguration create(WeightageConfiguration weightageConfiguration);

	WeightageConfiguration getWeightageConfigurationById(Integer configurationId);
	
	List<WeightageConfiguration> createInBatch(List<WeightageConfiguration> weightageConfigurationList);

}
