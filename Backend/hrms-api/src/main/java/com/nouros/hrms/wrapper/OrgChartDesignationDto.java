package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class OrgChartDesignationDto {

	String jobDescription;
	Integer departmentId;
	Integer parentDesignationId;
	Integer orgChartId;
	
}
