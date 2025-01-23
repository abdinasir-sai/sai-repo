package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.repository.generic.GenericRepository;

import jakarta.validation.Valid;

/**

The JobOpeningRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link JobOpening} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface JobOpeningRepository extends GenericRepository<JobOpening> {

	@Query("SELECT count(*) FROM JobOpening j WHERE j.department.id = :departmentId")
	Integer getCountOfJobOpeningByDepartmentId(@Param("departmentId") Integer departmentId);

	@Query("SELECT count(*) FROM JobOpening j WHERE j.department.id in (:departmentIds)")
	Integer getCountOfJobOpeningByDepartmentIds(@Param("departmentIds") List<Integer> departmentIds);

	@Query("SELECT j FROM JobOpening j WHERE j.jobId = :jobOpeningId")
	JobOpening getJobOpeningByReferenceId(@Param("jobOpeningId") String jobOpeningId);
	
	@Query("SELECT j FROM JobOpening j WHERE j.workflowStage = :workflowStage")
	List<JobOpening> getJobOpeningByWorkflowStage(@Param("workflowStage") String workflowStage);
	
	@Query("SELECT j FROM JobOpening j WHERE j.jobOpeningStatus = 'Active' AND j.careerWebsite = true ")
	List<JobOpening> getActiveAndCareerSiteJobOpenings();
	
	@Query("SELECT j FROM JobOpening j WHERE j.jobOpeningStatus = 'Active'")
	List<JobOpening> getActiveJobOpening();
	
	@Query("SELECT j FROM JobOpening j WHERE j.department.id = :departmentId AND j.jobOpeningStatus = 'Active'")
	List<JobOpening> getActiveJobOpeningByDepartment(@Param("departmentId") Integer departmentId);

	
	@Query("SELECT j FROM JobOpening j WHERE j.postingTitle.name = :postingTitle")
	JobOpening findJobOpeningByPostingTitle(@Param("postingTitle") String postingTitle);
	
	@Query(value = "SELECT * FROM JOB_OPENING WHERE JOB_OPENING_STATUS = 'Active' AND CREATED_TIME <= DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY) AND CUSTOMER_ID = :customerId", nativeQuery = true)
    List<JobOpening> getActiveJobOpeningsByCreationDate(@Param("customerId") Integer customerId);	
	
	@Query(value = "SELECT * FROM JOB_OPENING WHERE JOB_OPENING_STATUS = 'Active' AND HEAD_HUNTING= 1 AND CUSTOMER_ID = :customerId", nativeQuery = true)
    List<JobOpening> getHeadHuntedEnabledAndActiveJobOpening(@Param("customerId") Integer customerId);	
	
	@Query("SELECT jo FROM JobOpening jo WHERE jo.hiringManager.userId = :userId")
    List<JobOpening> findByHiringManagerUserId(@Param("userId") Integer userId);
	
	@Query("SELECT jo FROM JobOpening jo WHERE jo.postingTitle.id=:designationId ")
	List<JobOpening> getJobOpeningFromDesignationId(@Param("designationId")Integer designationId);
	
}
