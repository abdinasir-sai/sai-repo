package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.NationalityController;
import com.nouros.hrms.model.Nationality;
import com.nouros.hrms.service.NationalityService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/Nationality")
public class NationalityControllerImpl implements NationalityController{
	
	  private static final Logger log = LogManager.getLogger(NationalityControllerImpl.class);

	  @Autowired
	  private NationalityService nationalityService;

	  @Override
	  @TriggerBPMN(entityName = "Nationality", appName = "HRMS_APP_NAME")
	  public Nationality create(Nationality nationality) {
		  log.info("inside @class NationalityControllerImpl @method create");
	    return nationalityService.create(nationality);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return nationalityService.count(filter);
	  }

	  @Override
	  public List<Nationality> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return nationalityService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public Nationality update(Nationality nationality) {
	    return nationalityService.update(nationality);
	  }

	  @Override
	  public Nationality findById(Integer id) {
	    return nationalityService.findById(id);
	  }

	  @Override
	  public List<Nationality> findAllById(List<Integer> id) {
	    return nationalityService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  nationalityService.deleteById(id);
	  }
	

}
