package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Employeeeducationdetails;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeeducationdetailsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Employeeeducationdetails} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeeducationdetailsRepository extends GenericRepository<Employeeeducationdetails> {

	@Query( value="delete from EMPLOYEEEDUCATIONDETAILS where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteEducationByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	

}
