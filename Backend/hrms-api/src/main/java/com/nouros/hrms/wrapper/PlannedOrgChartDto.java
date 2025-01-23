package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.model.PlannedOrgChart;

import lombok.Data;

@Data
public class PlannedOrgChartDto {
	
	PlannedOrgChart plannedOrgChart;
	List<OrgChartDesignation> orgChartDesignation;

}
