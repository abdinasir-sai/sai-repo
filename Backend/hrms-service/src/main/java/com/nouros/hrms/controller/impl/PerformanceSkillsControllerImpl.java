package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.PerformanceSkillsController;
import com.nouros.hrms.model.PerformanceSkills;
import com.nouros.hrms.service.PerformanceSkillsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/PerformanceSkills")
public class PerformanceSkillsControllerImpl implements PerformanceSkillsController {

	private static final Logger log = LogManager.getLogger(PerformanceSkillsControllerImpl.class);
	 @Autowired
	  private PerformanceSkillsService performanceSkillsService;
	  

		
	  @Override
	  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	  public PerformanceSkills create(PerformanceSkills performanceSkills) {
		  log.info("inside @class PerformanceSkillsControllerImpl @method create");
	    return performanceSkillsService.create(performanceSkills);
	  }

	  @Override
	  public Long count(String filter) {
	    return performanceSkillsService.count(filter);
	  }

	  @Override
	  public List<PerformanceSkills> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return performanceSkillsService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	  public PerformanceSkills update(PerformanceSkills performanceSkills) {
	    return performanceSkillsService.update(performanceSkills);
	  }

	  @Override
	  public PerformanceSkills findById(Integer id) {
	    return performanceSkillsService.findById(id);
	  }

	  @Override
	  public List<PerformanceSkills> findAllById(List<Integer> id) {
	    return performanceSkillsService.findAllById(id);
	  }

	  @Override
	  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	  public void deleteById(Integer id) {
		  performanceSkillsService.deleteById(id);
	  }
	  

	
	    
}
