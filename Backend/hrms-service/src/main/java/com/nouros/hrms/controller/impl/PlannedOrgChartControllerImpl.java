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
import com.nouros.hrms.controller.PlannedOrgChartController;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.PlannedOrgChart;
import com.nouros.hrms.service.PlannedOrgChartService;
import com.nouros.hrms.wrapper.PlannedOrgChartDto;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/PlannedOrgChart")
public class PlannedOrgChartControllerImpl implements PlannedOrgChartController{
	
	private static final Logger log = LogManager.getLogger(PlannedOrgChartControllerImpl.class);

	  @Autowired
	  private PlannedOrgChartService plannedOrgChartService;

	  @Override
	  @TriggerBPMN(entityName = "PlannedOrgChart", appName = "HRMS_APP_NAME")
	  public PlannedOrgChart create(PlannedOrgChart plannedOrgChart) {
		  log.info("inside @class PlannedOrgChartControllerImpl @method create");
	    return plannedOrgChartService.create(plannedOrgChart);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return plannedOrgChartService.count(filter);
	  }

	  @Override
	  public List<PlannedOrgChart> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return plannedOrgChartService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public PlannedOrgChart update(PlannedOrgChart plannedOrgChart) {
	    return plannedOrgChartService.update(plannedOrgChart);
	  }

	  @Override
	  public PlannedOrgChart findById(Integer id) {
	    return plannedOrgChartService.findById(id);
	  }

	  @Override
	  public List<PlannedOrgChart> findAllById(List<Integer> id) {
	    return plannedOrgChartService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  plannedOrgChartService.deleteById(id);
	  }


	@Override
	public List<PlannedOrgChart> searchPlannedOrgChartByDeparmentId(Integer departmentId) {
		return plannedOrgChartService.searchPlannedOrgChartByDeparmentId(departmentId);
	}


	@Override
	public PlannedOrgChartDto searchOrgChartDesignationsByDepartmentId(Integer departmentId) {
	return plannedOrgChartService.searchOrgChartDesignationsByDepartmentId(departmentId);
	}


	@Override
	public String deletePlannedOrgChartByDepartmentId(Integer departmentId) {
		return plannedOrgChartService.deletePlannedOrgChartByDepartmentId(departmentId);
	}


	@Override
	public String createDesignationByApprovedPlannedOrgChart(@Valid Integer plannedOrgChartId,
			@Valid String processInstanceId) {
		return plannedOrgChartService.createDesignationByApprovedPlannedOrgChart(plannedOrgChartId,processInstanceId);
	}


	@Override
	public PlannedOrgChartDto searchOrgChartDesignationsByPlannedOrgChartId(Integer plannedOrgChartId) {
		return plannedOrgChartService.searchOrgChartDesignationsByPlannedOrgChartId(plannedOrgChartId);
	}


	

}
