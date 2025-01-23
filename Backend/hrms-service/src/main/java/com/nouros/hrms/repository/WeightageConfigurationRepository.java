package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface WeightageConfigurationRepository extends GenericRepository<WeightageConfiguration> {

	@Query("SELECT jc FROM WeightageConfiguration jc WHERE jc.id = :configurationId ")
	WeightageConfiguration getWeightageConfigurationById(@Param("configurationId") Integer configurationId);

	@Query(value = "SELECT * FROM WEIGHTAGE_CONFIGURATION WHERE CONFIGURATION_NAME = :configurationName AND CUSTOMER_ID = :customerId", nativeQuery = true)
	WeightageConfiguration findbyConfigurationName(@Param("configurationName") String configurationName,@Param("customerId") Integer customerId);

}
