package com.nouros.hrms.wrapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeDependentWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String contactNumber;
	private String fullName;
	private String firstName;
	private String lastName;
	private String relationship;
	private String dependentIdentification;
}
