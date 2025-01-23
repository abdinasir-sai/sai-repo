package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.repository.generic.GenericRepository;


@Repository
@Transactional(readOnly = true)
public interface EmployeeEmergencyContactRepository extends GenericRepository<EmployeeEmergencyContact>{

	@Query("SELECT ec FROM EmployeeEmergencyContact ec WHERE ec.employeeId.id = :id")
	EmployeeEmergencyContact findEmergencyContactOfEmployee(@Param("id") Integer id);

}
