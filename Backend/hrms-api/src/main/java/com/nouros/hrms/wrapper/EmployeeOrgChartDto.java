package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeOrgChartDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name ;
	private String title;
	private String country;
	private List<EmployeeDto> employeesLevel1;
	private DepartmentDetailsWrapper departmentDetail;
	
	
}
