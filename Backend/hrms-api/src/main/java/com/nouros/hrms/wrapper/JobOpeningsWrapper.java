package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class JobOpeningsWrapper {
	private Integer id;
	private String title;
	private String location;
	private String department;
	private String employmentType;
	private String about;
	private String requirements;
	private String responsibilities;
	private String preferredQualifications;
	private String jobId;
}