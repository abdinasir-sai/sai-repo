/* (C)2024 */
package com.nouros.hrms.repository;

import com.nouros.hrms.model.EmployeeCertification;
import com.nouros.hrms.repository.generic.GenericRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * The EmployeeCertificationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
 * methods that are specific to the {@link EmployeeCertification} entity.
 * see GenericRepository
 * see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
public interface EmployeeCertificationRepository extends GenericRepository<EmployeeCertification> {
	
	@Query( value="delete from EMPLOYEE_CERTIFICATION where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteCertificationsByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	
}
