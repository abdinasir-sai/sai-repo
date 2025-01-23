package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class DepartmentDetailsWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	private String departmentName;
	private String designation;
	private String departmentLead;
	private Integer departmentId;
	private Integer parentDepartmentId;
	private Integer jobOpeningNumber;
	private Integer jobApplicationCount;
	private Map<String, Integer> countMap;
	
}
