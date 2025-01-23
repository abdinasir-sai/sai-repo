package com.nouros.hrms.wrapper;


import java.util.List;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.model.JobApplication;

import lombok.Data;

@Data
public class ApplicantPromptWrapper {

	
	Applicant applicant;
	List<ApplicantEducation> applicantEducation;
	List<ApplicantLanguage> applicantLanguage;
	List<ApplicantCertifications> applicantCertifications;
	List<ApplicantExperience> applicantExperience;
	JobApplication jobApplication;
	Boolean jobApplicationUpdateNeeded;
	
	
}
