/* (C)2024 */
package com.nouros.hrms.repository;

import com.nouros.hrms.model.EmployeeLanguage;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * The EmployeeLanguageRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
 * methods that are specific to the {@link EmployeeLanguage} entity.
 * see GenericRepository
 * see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
public interface EmployeeLanguageRepository extends GenericRepository<EmployeeLanguage> {
	
	@Query( value="delete from EMPLOYEE_LANGUAGE where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteLanguageByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
}
