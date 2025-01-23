package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.service.generic.GenericService;

/**

CandidateExperienceService interface is a service layer interface which handles all the business logic related to CandidateExperience model.

It extends GenericService interface which provides basic CRUD operations.

*/
public interface ApplicantExperienceService  extends GenericService<Integer, ApplicantExperience> {


	
	void bulkDelete(List<Integer> list);

	List<ApplicantExperience> createApplicantExperiencefromPrompt(List<Map<String, String>> applicantExperienceList,
			Applicant createdApplicant);

	List<ApplicantExperience> updateApplicantExperienceAfterResumeDetailsSet(
			List<ApplicantExperience> applicantExperienceList,Applicant updatedApplicant);

	List<ApplicantExperience> getExperienceForApplicant(Integer id);
	
   
   
}
