package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ConfigurationScore;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface ConfigurationScoreRepository extends GenericRepository<ConfigurationScore>{

	@Query(value = "SELECT * FROM CONFIGURATION_SCORE WHERE JOB_APPLICATION_FK = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<ConfigurationScore> findByJobApplicationId(@Param("id") Integer id, @Param("customerId") Integer customerId);

}
