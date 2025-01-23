package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.JobApplicationConfigurationScore;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface JobApplicationConfigurationScoreRepository extends GenericRepository<JobApplicationConfigurationScore> {

	 @Query("SELECT j FROM JobApplicationConfigurationScore j WHERE j.jobApplication.id = :jobApplicationId")
	    List<JobApplicationConfigurationScore> findAllJobApplicationConfigurationScore(@Param("jobApplicationId") Integer jobApplicationId);

	 @Query(value = "SELECT * FROM JOB_APPLICATION_CONFIGURATION_SCORE WHERE JOB_APPLICATION_ID = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	    List<JobApplicationConfigurationScore> findByJobApplicationId(@Param("id") Integer id, @Param("customerId") Integer customerId);

	

}
