package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String title;
	private String country;
	private List<EmployeeDto> employeesLevel2;
	private DepartmentDetailsWrapper departmentDetail;
	
}
