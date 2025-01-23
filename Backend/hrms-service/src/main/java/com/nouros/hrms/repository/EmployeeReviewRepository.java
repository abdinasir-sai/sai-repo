package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeReviewRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeReview} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeReviewRepository extends GenericRepository<EmployeeReview> {

	@Query(value = "SELECT * FROM EMPLOYEE_REVIEW WHERE EMPLOYEE_ID =:employeeId AND START_DATE <=:currentDate AND END_DATE>=:currentDate AND CUSTOMER_ID = :customerId " , nativeQuery = true)
     EmployeeReview getEmployeeReviewByEmployeeIdAndDate(@Param("employeeId")Integer employeeId,@Param("currentDate")LocalDate currentDate, @Param("customerId") Integer customerId); 
	
	@Query(value = " SELECT * FROM EMPLOYEE_REVIEW WHERE CUSTOMER_ID=:customerId ORDER BY COMPETENCIES_AVERAGE_REVIEWER DESC ",nativeQuery = true)
	List<EmployeeReview> getEmployeeReviewListByPerformanceScore(@Param("customerId") Integer customerId);
}
