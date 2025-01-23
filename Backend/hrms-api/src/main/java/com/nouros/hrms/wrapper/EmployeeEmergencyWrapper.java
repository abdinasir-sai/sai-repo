package com.nouros.hrms.wrapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeEmergencyWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String emergencyContactFirstName;
	private String emergencyContactMiddleName;
	private String emergencyContactLastName;
	private String emergencyContactEmailAddress;
	private String emergencyContactNumber;
	private String relationship;
}
