package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.repository.WeightageConfigurationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.WeightageConfigurationService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;


@Service
public class WeightageConfigurationServiceImpl extends AbstractService<Integer,WeightageConfiguration> implements WeightageConfigurationService {
	
	public WeightageConfigurationServiceImpl(GenericRepository<WeightageConfiguration> repository) {
		super(repository, WeightageConfiguration.class);
	}
	
	@Autowired
	private WeightageConfigurationRepository weightageConfigurationRepository;
	
	 @Autowired
	  private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(WeightageConfigurationServiceImpl.class);

	@Override
	public WeightageConfiguration create(WeightageConfiguration weightageConfiguration) {
		log.info("inside @class WeightageConfigurationServiceImpl @method create");
		return weightageConfigurationRepository.save(weightageConfiguration);
	}
	
	@Override
	public void softDelete(int id) {

		WeightageConfiguration weightageConfiguration = super.findById(id);

		if (weightageConfiguration != null) {

			WeightageConfiguration weightageConfiguration1 = weightageConfiguration;
			weightageConfigurationRepository.save(weightageConfiguration1);

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
	public WeightageConfiguration getWeightageConfigurationById(Integer configurationId) {
		log.info("Inside getWeightageConfigurationById");
		try {
			log.debug(" Inside @getWeightageConfigurationById  customerId is : {}", commonUtils.getCustomerId());
			return weightageConfigurationRepository.getWeightageConfigurationById(configurationId);
		} catch (Exception e) {
			log.error("An error occurred while getting JobOpeningWeightageCriterias  by job Opening Id : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public List<WeightageConfiguration> createInBatch(List<WeightageConfiguration> weightageConfigurationList) {
		log.debug("Inside @class WeightageConfigurationServiceImpl createInBatch list:{}",weightageConfigurationList);
		List<WeightageConfiguration> weightageConfigurationSavedList = new ArrayList<>();
		for(WeightageConfiguration weightageConfiguration: weightageConfigurationList)
		{
			WeightageConfiguration weightageConfigurationSaved = weightageConfigurationRepository.save(weightageConfiguration);
			log.debug("Inside @class WeightageConfigurationServiceImpl weightageConfigurationsaved is :{} ",weightageConfigurationSaved.getId() );
			weightageConfigurationSavedList.add(weightageConfigurationSaved);
		}
		log.debug("Inside @class WeightageConfigurationServiceImpl size Of list is : {} ",weightageConfigurationSavedList.size());
		return weightageConfigurationSavedList;
	}

}
