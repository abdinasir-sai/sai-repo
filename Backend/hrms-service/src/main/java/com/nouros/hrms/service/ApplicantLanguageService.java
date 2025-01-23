package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantLanguageService  extends GenericService<Integer, ApplicantLanguage>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public ApplicantLanguage create(ApplicantLanguage applicantLanguage);
	
	List<ApplicantLanguage> createApplicantLanguagefromPrompt(List<Map<String, String>> applicantLanguageList, Applicant applicant);

	List<ApplicantLanguage> updateApplicantLanguageAfterResumeDetailsSet(List<ApplicantLanguage> applicantLanguageList,Applicant updatedApplicant);

	List<ApplicantLanguage> getLanguagesForApplicant(Integer id);
	
	
}
