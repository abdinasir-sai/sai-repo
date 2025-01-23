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
import com.nouros.hrms.controller.ConfigurationScoreController;
import com.nouros.hrms.model.ConfigurationScore;
import com.nouros.hrms.service.ConfigurationScoreService;

@Primary
@RestController
@RequestMapping("/ConfigurationScore")
public class ConfigurationScoreControllerImpl implements ConfigurationScoreController{
	
	  private static final Logger log = LogManager.getLogger(ConfigurationScoreControllerImpl.class);

	  @Autowired
	  private ConfigurationScoreService configurationScoreService;

	  @Override
//	  @TriggerBPMN(entityName = "ConfigurationScore", appName = "HRMS_APP_NAME")
	  public ConfigurationScore create(ConfigurationScore configurationScore) {
		  log.info("inside @class ConfigurationScoreControllerImpl @method create");
	    return configurationScoreService.create(configurationScore);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return configurationScoreService.count(filter);
	  }

	  @Override
	  public List<ConfigurationScore> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return configurationScoreService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public ConfigurationScore update(ConfigurationScore configurationScore) {
	    return configurationScoreService.update(configurationScore);
	  }

	  @Override
	  public ConfigurationScore findById(Integer id) {
	    return configurationScoreService.findById(id);
	  }

	  @Override
	  public List<ConfigurationScore> findAllById(List<Integer> id) {
	    return configurationScoreService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  configurationScoreService.deleteById(id);
	  }
	

}
