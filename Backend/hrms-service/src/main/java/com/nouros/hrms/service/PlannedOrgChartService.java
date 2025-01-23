package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.model.PlannedOrgChart;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.PlannedOrgChartDto;

import jakarta.validation.Valid;

public interface PlannedOrgChartService extends GenericService<Integer,PlannedOrgChart> {

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public PlannedOrgChart create(PlannedOrgChart plannedOrgChart);

	List<PlannedOrgChart> searchPlannedOrgChartByDeparmentId(Integer departmentId);

	PlannedOrgChartDto searchOrgChartDesignationsByDepartmentId(Integer departmentId);

	public PlannedOrgChart update(PlannedOrgChart plannedOrgChart);

	String deletePlannedOrgChartByDepartmentId(Integer departmentId);

	String createDesignationByApprovedPlannedOrgChart(@Valid Integer plannedOrgChartId,
			@Valid String processInstanceId);

	PlannedOrgChartDto searchOrgChartDesignationsByPlannedOrgChartId(Integer plannedOrgChartId);
	
	
	Designation createDesignationForSai(OrgChartDesignation orgChartDesignation);

}
