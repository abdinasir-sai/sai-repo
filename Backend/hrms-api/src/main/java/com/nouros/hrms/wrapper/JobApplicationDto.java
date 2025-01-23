package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.JobApplicationBatch;

import lombok.Data;

@Data
public class JobApplicationDto {

	
	
	List<Integer> jobApplicationIds;
	JobApplicationBatch jobApplicationBatch;
	String applicantStatus;
}
