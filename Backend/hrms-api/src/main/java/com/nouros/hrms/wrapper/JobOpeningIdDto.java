package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class JobOpeningIdDto {

	String jobId;
	Integer id;
	String title;
	String location;
	String department;
	String employmentType;
	String about;
	String requirements;
	String responsibilities;
	String preferredQualifications;
	
}
