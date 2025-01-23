package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.JobApplicationBatch;

import lombok.Data;

@Data
public class ApplicantDto {

	String fullName;
	String type;
    List<KeyFactsDto> keyFacts;
	List<ApplicantExperienceDto> applicantExperienceDto;
	Double educationScore;
	Integer ranking;
	String jobApplicationId;
	boolean pinnedApplication;
	Integer id;
	JobApplicationBatch jobApplicationBatch;
	String jobApplicationStatus;

}
