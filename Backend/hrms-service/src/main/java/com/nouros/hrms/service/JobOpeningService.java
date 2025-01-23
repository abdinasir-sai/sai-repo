package com.nouros.hrms.service;

import java.util.List;

import org.json.JSONObject;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.JobOpeningAndApplicantReferralDto;
import com.nouros.hrms.wrapper.JobOpeningDto;
import com.nouros.hrms.wrapper.JobOpeningIdDto;
import com.nouros.hrms.wrapper.JobOpeningSchedulerDto;
import com.nouros.hrms.wrapper.JobOpeningWrapper;
import com.nouros.hrms.wrapper.JobOpeningsDto;

/**

JobOpeningService interface is a service layer interface which handles all the business logic related to JobOpening model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext JobOpening

@version 1.0

@since 2022-07-01
*/
public interface JobOpeningService extends GenericService<Integer,JobOpening> {


	void softDelete(int id);
	void softBulkDelete(List<Integer> list);
	
	public JobOpening createWithNaming(JobOpening jobOpening);

	String createSearchString(JobOpening jobOpening);

	JSONObject setJobOpeningDataFromObjects(JobOpening jobOpening);

	List<Applicant> findSuitableApplicantByJobOpeningId(Integer id);

	Integer getCountOfJobOpeningByDepartmentId(Integer id);

	Integer getCountOfJobOpeningByDepartmentIds(List<Integer> departmentIds);
	
	JobOpening getJobOpeningByReferenceId(String jobOpeningId);
	
	JobOpening findJobOpeningByUserLogin();
	
	List<JobOpeningDto> getJobOpeningByActiveWorkFlowStage();
	
	String referApplicantForJobopening(JobOpeningWrapper jobOpeningWrapper);
	
	JobOpeningSchedulerDto getActiveJobOpening();
	
	JobOpeningSchedulerDto getActiveJobOpeningByDepartment(Integer departmentId);
	JobOpening createJobOpening(JobOpeningAndApplicantReferralDto jobOpeningAndApplicantReferralDto);
	JobOpeningsDto getApplicantCountByActiveJobOpening(String isCritical,
			String departmentName, String postingTitleName, String postingTitleJobLevel);

	JobOpeningIdDto getJobOpeningDetailByJobId(String jobId);
	

	List<JobOpening> getFilteredJobOpenings(String isCritical, String departmentName,String postingTitleName, String postingTitleJobLevel);

	 List<JobOpening> getJobOpeningByDesignation(Integer designationId);
   
}
