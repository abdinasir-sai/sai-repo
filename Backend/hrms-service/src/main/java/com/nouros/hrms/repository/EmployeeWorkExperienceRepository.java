package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeWorkExperience;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeWorkExperienceRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeWorkExperience} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeWorkExperienceRepository extends GenericRepository<EmployeeWorkExperience> {
	
	@Query( value="delete from EMPLOYEE_WORK_EXPERIENCE where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteWorkExperienceByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	


}
