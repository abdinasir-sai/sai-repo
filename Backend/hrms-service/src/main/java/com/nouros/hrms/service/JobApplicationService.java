package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.JobApplicationDto;

import jakarta.validation.Valid;

/**
 * 
 * JobApplicationService interface is a service layer interface which handles
 * all the business logic related to JobApplication model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves JobApplication
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface JobApplicationService extends GenericService<Integer,JobApplication> {

	JobApplication findByApplicantId(Integer id);

	String summariseResumeByJobOpeningForJobApplication(JobApplication jobApplication);

	String summariseResumeByJobOpeningWithInputFile(Integer jobApplicationId, MultipartFile file);

	List<JobApplication> applicantByApplicationStatus(String name);

	Integer getCountOfJobApplicationByDepartmentId(Integer id);

	Integer getCountOfJobApplicationByDepartmentIds(List<Integer> departmentIds);

	JobApplication updateJobApplicationForApplicant(Applicant applicant,JobApplication jobApplication);

	JobApplication findJobApplicantByUserContext();

	JobApplication calculateOverallScoreForJobApplication(JobApplication jobApplication);

	Map<String, Object> getTopRankedAndTopReferralApplicantByJobId(@Valid String jobId);

	String deleteConfigurationScoreByJobApplicationId(Integer id);
	
	List<JobApplication> setRankingForJobAppicationsById(Integer id);

	String setOverallScoresAndRankingForJobApplication();
	
	Map<String, Object> setTopRankedAndTopReferralApplicantByJobId(String jobId , String jSon);

	List<JobApplication> updateBatchForJobApplication(JobApplicationDto jobApplicationDto);

	List<JobApplication> updateJobApplicationStatus(JobApplicationDto jobApplicationDto);

	JobApplication createJobApplicationForApplicantWithoutPrioritization(Applicant applicant , String jobId);

	JobApplication createJobApplicationForApplicant(Applicant applicant, String jobId); 
	
	String setRankingForJobAppicationsOnRegularBasis();
	
	String setRankingForJobAppicationsPostTwoDays();

}
