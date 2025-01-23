package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeCompetenciesDetailsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeCompetenciesDetails} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeCompetenciesDetailsRepository extends GenericRepository<EmployeeCompetenciesDetails> {

	@Query(value = "SELECT * FROM EMPLOYEE_COMPETENCIES_DETAILS where EMPLOYEE_REVIEW_FK =:employeeReviewId AND CUSTOMER_ID=:customerId",nativeQuery =true)
	List<EmployeeCompetenciesDetails> getEmployeeCompetenciesDetailsByEmployeeReviewId(@Param("employeeReviewId")Integer employeeReviewId, @Param("customerId") Integer customerId); 
}
