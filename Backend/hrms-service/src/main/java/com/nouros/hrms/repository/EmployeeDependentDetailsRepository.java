package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeDependentDetailsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeDependentDetails} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeDependentDetailsRepository extends GenericRepository<EmployeeDependentDetails> {

	@Query("SELECT ed FROM EmployeeDependentDetails ed WHERE ed.employee.id = :id")
	List<EmployeeDependentDetails> findDependentDetailsOfEmployee(@Param("id") Integer id);
	
	@Query( value="delete from EMPLOYEE_DEPENDENT_DETAILS where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteDependentDetailsByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);


}
