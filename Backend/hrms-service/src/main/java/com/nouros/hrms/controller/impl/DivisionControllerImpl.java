package com.nouros.hrms.controller.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.DivisionController;
import com.nouros.hrms.model.Division;
import com.nouros.hrms.service.DivisionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/Division")
public class DivisionControllerImpl implements DivisionController {
	
	private static final Logger log = LogManager.getLogger(DivisionControllerImpl.class);

	  @Autowired
	  private DivisionService divisionService;


		
	  @Override 
	  @TriggerBPMN(entityName = "Division", appName = "HRMS_APP_NAME")
	  public Division create(Division division) {
		  log.info("inside @class DivisionControllerImpl @method create");
	    return divisionService.create(division);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return divisionService.count(filter);
	  }

	  @Override
	  public List<Division> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return divisionService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public Division update(Division division) {
	    return divisionService.update(division);
	  }

	  @Override
	  public Division findById(Integer id) {
	    return divisionService.findById(id);
	  }

	  @Override
	  public List<Division> findAllById(List<Integer> id) {
	    return divisionService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  divisionService.deleteById(id);
	  }
	 

		@Override
		public List<Map<String, String>> getAirportDetails(String valueField) {
			 return Collections.emptyList();
		}
	    
	  

}
