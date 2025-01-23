package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.service.generic.GenericService;

/**

ApplicantEducationService interface is a service layer interface which handles all the business logic related to ApplicantEducation model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext ApplicantEducation

@version 1.0

@since 2022-07-01
*/
public interface ApplicantEducationService extends GenericService<Integer, ApplicantEducation> {


	/**

This method is used to soft delete a list of ApplicantEducation identified by their ids.
@param list The list of ids of the ApplicantEducation to be soft deleted.
*/
	void bulkDelete(List<Integer> list);

	List<ApplicantEducation> createApplicationEducationfromPropt(List<Map<String, String>> applicantEducationList, Applicant applicant);

	List<ApplicantEducation> updateApplicantEducationAfterResumeDetailsSet(
			List<ApplicantEducation> applicantEducationList,Applicant updatedApplicant);

	List<ApplicantEducation> getEducationsForApplicant(Integer id);
	
   
   
}
