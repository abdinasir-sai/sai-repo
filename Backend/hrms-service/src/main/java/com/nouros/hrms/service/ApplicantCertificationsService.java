package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantCertificationsService extends GenericService<Integer, ApplicantCertifications>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public ApplicantCertifications create(ApplicantCertifications applicantCertifications);

	
	List<ApplicantCertifications> createApplicantCertificationsfromPropt(List<Map<String, String>> applicantCertificationsList, Applicant applicant);

	List<ApplicantCertifications> updateApplicantCertificationsAfterResumeDetailsSet(
			List<ApplicantCertifications> applicantCertificationsList,Applicant updatedApplicant);

	List<ApplicantCertifications> getCertificationsForApplicant(Integer id);

	
	
}
