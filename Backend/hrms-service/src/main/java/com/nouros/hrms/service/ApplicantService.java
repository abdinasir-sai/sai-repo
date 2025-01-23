package com.nouros.hrms.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.ApplicantPromptWrapper;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.EmployeePopulateDto;
import com.nouros.hrms.wrapper.ProfessionalSummaryWrapper;
import com.nouros.hrms.wrapper.WorkExperienceSummaryWrapper;

import jakarta.validation.Valid;

/**
 * 
 * ApplicantService interface is a service layer interface which handles all the
 * business logic related to Applicant model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Applicant
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface ApplicantService extends GenericService<Integer, Applicant> {

	/**
	 * 
	 * This method is used to retrieve audit history for an Applicant identified by
	 * id.
	 * 
	 * @param id    The id of the Applicant whose audit history is to be retrieved.
	 * @param limit The maximum number of records to retrieve.
	 * @param skip  The number of records to skip before retrieving.
	 * @return A string representation of the audit history.
	 */
	String auditHistory(int id, Integer limit, Integer skip);

	/**
	 * 
	 * This method is used to import Applicant data from an excel file.
	 * 
	 * @param excelFile The excel file containing Applicant data.
	 * @return A string indicating the status of the import operation.
	 * @throws IOException            If there is an error reading the file.
	 * @throws InstantiationException If there is an error creating an instance of
	 *                                the class.
	 * @throws ClassNotFoundException If the class specified in the file is not
	 *                                found.
	 */
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;

	/**
	 * 
	 * This method is used to export Applicant data to an excel file.
	 * 
	 * @param applicant The list of applicant to be exported.
	 * @return A byte array containing the excel file.
	 * @throws IOException If there is an error writing to the file.
	 */
	byte[] export(List<Applicant> applicant) throws IOException;

	/**
	 * 
	 * This method is used to soft delete an Applicant identified by id.
	 * 
	 * @param id The id of the Applicant to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	String compareApplicantsForJobOpening(@Valid Integer applicant1Id, @Valid Integer applicant2Id,
			@Valid Integer jobOpeningId);

	String applicantRecommendationForJobOpening(@Valid Integer applicantId, @Valid Integer jobOpeningId);

	List<JobOpening> findSuitableJobOpeningByApplicantId(@Valid Integer applicantId);

	List<Object[]> reviewTheSalaryForJobOfferPositionAndCandidate();

	List<Object[]> reviewJobOfferForPositionAndCandidate();

	List<Object[]> findApplicantByCreatedDate();

	List<JobOpening> findSuitableJobOpeningByApplicantObject(ApplicantWrapper applicantWrapper);

	Applicant createApplicant(ApplicantWrapper applicantWrapper);

	ApplicantPromptWrapper updateApplicantAfterResumeDetailsSet(ApplicantPromptWrapper applicantPromptWrapper);

	Applicant getApplicantByUserId(Integer userId);

	String regenerateProfessionalSummary(ProfessionalSummaryWrapper professionalSummaryWrapper);

	ApplicantPromptWrapper updateApplicantFromResumeFolder();

	String regenerateWorkExperienceSummary(WorkExperienceSummaryWrapper workExperienceSummaryWrapper);
	
	EmployeePopulateDto populateApplicantToEmployee(String processInstanceId, Integer applicantId);
	
	String deleteApplicantCorrespondingData(Integer id);

	ApplicantPromptWrapper updateApplicantWithResume(Integer documentId, String jobId);

}
