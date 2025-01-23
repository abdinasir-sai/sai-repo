package com.nouros.hrms.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeGoalsMappingRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeGoalsMapping} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeGoalsDetailsRepository extends GenericRepository<EmployeeGoalsDetails> {

	@Query(value = "SELECT * FROM EMPLOYEE_GOALS_DETAILS where EMPLOYEE_REVIEW_FK =:employeeReviewId AND CUSTOMER_ID=:customerId",nativeQuery =true)
	List<EmployeeGoalsDetails> getEmployeeGoalDetailsByEmployeeReviewId(@Param("employeeReviewId")Integer employeeReviewId, @Param("customerId") Integer customerId); 
	

}
