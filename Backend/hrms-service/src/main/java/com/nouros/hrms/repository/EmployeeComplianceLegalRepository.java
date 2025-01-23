package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeComplianceLegalRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeComplianceLegal} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeComplianceLegalRepository extends GenericRepository<EmployeeComplianceLegal> {

	@Query("SELECT ec FROM EmployeeComplianceLegal ec WHERE ec.employee.id = :id")
	EmployeeComplianceLegal findComplianceLegalDetailsOfEmployee(@Param("id") Integer id);


}
