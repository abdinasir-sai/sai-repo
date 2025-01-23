package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class ReportingHeirarchyWrapper {

	String name;
	String emailId;
	String department;
	String employeeId;
	String designation;
	int reportingManagerId;
	int id;
	int userId;
}
