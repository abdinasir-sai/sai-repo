package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class EmployeePopulateDto {

	Integer id;
	String fullName;
	String workEmailAddress;
	String reportingManagerFullName;
	String reportingManagerWorkEmailAddress;
	String employmentStatus;
	Integer nationalId;
	String type;
}
