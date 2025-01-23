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
import com.nouros.hrms.controller.WeightageConfigurationController;
import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.service.WeightageConfigurationService;

@Primary
@RestController
@RequestMapping("/WeightageConfiguration")
public class WeightageConfigurationControllerImpl implements WeightageConfigurationController {
	
	
	 private static final Logger log = LogManager.getLogger(WeightageConfigurationControllerImpl.class);

	  @Autowired
	  private WeightageConfigurationService weightageConfigurationService;

	  @Override
	  @TriggerBPMN(entityName = "WeightageConfiguration", appName = "HRMS_APP_NAME")
	  public WeightageConfiguration create(WeightageConfiguration weightageConfiguration) {
		  log.info("inside @class WeightageConfigurationControllerImpl @method create");
	    return weightageConfigurationService.create(weightageConfiguration);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return weightageConfigurationService.count(filter);
	  }

	  @Override
	  public List<WeightageConfiguration> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return weightageConfigurationService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public WeightageConfiguration update(WeightageConfiguration weightageConfiguration) {
	    return weightageConfigurationService.update(weightageConfiguration);
	  }

	  @Override
	  public WeightageConfiguration findById(Integer id) {
	    return weightageConfigurationService.findById(id);
	  }

	  @Override
	  public List<WeightageConfiguration> findAllById(List<Integer> id) {
	    return weightageConfigurationService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  weightageConfigurationService.deleteById(id);
	  }


	@Override
	public List<WeightageConfiguration> createInBatch(List<WeightageConfiguration> weightageConfigurationList) {
		log.info("inside @class WeightageConfigurationControllerImpl @method createInBatch");
		return weightageConfigurationService.createInBatch(weightageConfigurationList);
	}

}
