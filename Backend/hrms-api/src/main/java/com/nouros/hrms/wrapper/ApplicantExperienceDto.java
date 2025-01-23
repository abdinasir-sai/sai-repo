package com.nouros.hrms.wrapper;

import java.util.Date;

import lombok.Data;

@Data
public class ApplicantExperienceDto {

	String previousExperience;
	Date workStartDate;
	Date workEndDate;

}
