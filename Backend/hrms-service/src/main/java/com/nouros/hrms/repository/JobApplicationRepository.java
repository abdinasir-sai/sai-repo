package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The JobApplicationRepository interface is a extension of
 * {@link GenericRepository}. It's purpose is to provide additional methods that
 * are specific to the {@link JobApplication} entity. see GenericRepository see
 * JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface JobApplicationRepository extends GenericRepository<JobApplication> {

	@Query(name = "SELECT JA FROM JobApplication where applicant.id = ?1")
	JobApplication findByApplicantId(@Param("id") Integer id);

	@Query(value = "SELECT CONCAT('Review and fill out the feedback form for candidate&nbsp;<span class=\"highlighted-reminders-title\">', ja.FIRST_NAME, ' ', ja.LAST_NAME, '</span>') as Result , a.UNIQUE_ID_CANDIDATE as applicantId , jb.JOB_ID FROM JOB_APPLICATION ja INNER JOIN APPLICANT a ON ja.APPLICANT_ID=a.ID INNER JOIN JOB_OPENING jb ON ja.JOB_OPENING_ID=jb.ID WHERE APPLICATION_STATUS='Offer planned' AND DATE(ja.CREATED_TIME)=CURRENT_DATE AND jb.CUSTOMER_ID = :customerId AND ja.CUSTOMER_ID = :customerId AND a.CUSTOMER_ID = :customerId;", nativeQuery = true)
	List<Object[]> reviewAndFillTheFeedbackFormForCandidate( @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM JOB_APPLICATION WHERE APPLICANT_ID = :applicantId AND JOB_OPENING_ID = :jobOpeningId AND CUSTOMER_ID = :customerId", nativeQuery = true)
	JobApplication findByApplicantIdAndJobOpeningId(@Param("applicantId") int applicantId,
			@Param("jobOpeningId") int jobOpeningId, @Param("customerId") Integer customerId);

	@Query(name = "SELECT JA FROM JobApplication where JA.applicationStatus=:name")
	List<JobApplication> findAllByApplicationStatus(@Param("name") String name);

	@Query("SELECT count(*) FROM JobApplication j WHERE j.jobOpening.id IN (Select jo.id from JobOpening jo where jo.department.id = :departmentId)")
	Integer getCountOfJobApplicationByDepartmentId(@Param("departmentId") Integer departmentId);

	@Query("SELECT count(*) FROM JobApplication j WHERE j.jobOpening.id IN (Select jo.id from JobOpening jo where jo.department.id IN (:departmentIds))")
	Integer getCountOfJobApplicationByDepartmentId(@Param("departmentIds") List<Integer> departmentIds);

	@Query(value = "SELECT COUNT(*) FROM JOB_APPLICATION WHERE APPLICATION_STATUS = :status AND JOB_OPENING_ID = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Integer getCountOfJobApplicationByStatus(@Param("status") String status,@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT COUNT(*) FROM JOB_APPLICATION WHERE JOB_OPENING_ID = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Integer getCountOfJobApplicationByJobOpeningId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT COUNT(*) FROM JOB_APPLICATION WHERE JOB_OPENING_ID = :id AND DATE(CREATED_TIME) = CURDATE() AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Integer getCountOfJobApplicationByJobOpeningIdForToday(@Param("id") Integer id, @Param("customerId") Integer customerId);

	//@Query("SELECT ja FROM JobApplication ja WHERE ja.jobOpening.id = :id AND ja.jobApplicationBatch.id ")
	@Query(value = "SELECT * FROM JOB_APPLICATION ja  WHERE ja.JOB_OPENING_ID = :id AND ja.APPLICATION_STATUS =:status AND ja.CUSTOMER_ID = :customerId ", nativeQuery = true)
	List<JobApplication> findJobApplicationsByJobOpeningId(@Param("id") Integer id,@Param("status") String status, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM JOB_APPLICATION ja WHERE ja.JOB_OPENING_ID=:id AND ja.OVERALL_SCORE IS NOT NULL AND ja.PINNED_APPLICATION = 0  AND ja.CUSTOMER_ID = :customerId",nativeQuery = true)
	List<JobApplication> getJobApplicationById(@Param("id")Integer id, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM JOB_APPLICATION ja WHERE ja.JOB_OPENING_ID=:id AND ja.OVERALL_SCORE IS NOT NULL AND ja.PINNED_APPLICATION = 1 AND ja.CUSTOMER_ID = :customerId ",nativeQuery = true)
	List<JobApplication> getPinnedJobApplicationById(@Param("id")Integer id, @Param("customerId") Integer customerId);
	
	@Query("SELECT ja FROM JobApplication ja WHERE ja.jobApplicationId = :jobApplicationId")
	JobApplication findJobApplicationsByJobApplicationId(@Param("jobApplicationId") String jobApplicationId);

	@Query(value="SELECT * FROM JOB_APPLICATION ja WHERE ja.JOB_APPLICATION_BATCH_ID=:id AND ja.CUSTOMER_ID = :customerId",nativeQuery = true)
	List<JobApplication> findByJobApplicationBatchId(@Param("id") Integer id, @Param("customerId") Integer customerId);

}
