package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeNationalIdentificationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeNationalIdentification} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeNationalIdentificationRepository extends GenericRepository<EmployeeNationalIdentification> {

	@Query("SELECT en FROM EmployeeNationalIdentification en WHERE en.employee.id = :id")
	EmployeeNationalIdentification findNationalIdentificationDetailsOfEmployee(@Param("id") Integer id);

	@Query("SELECT en FROM EmployeeNationalIdentification en WHERE en.employee.id = :id")
	List<EmployeeNationalIdentification> findNationalIdentificationDetailsList(@Param("id") Integer id);

	@Query(value = "SELECT * FROM EMPLOYEE_NATIONAL_IDENTIFICATION WHERE EMPLOYEE_ID = :id AND CUSTOMER_ID = :customerId AND (TYPE = 'National ID' OR TYPE = 'NationalID' OR TYPE = 'Iqama');",nativeQuery = true)
	EmployeeNationalIdentification findNationalIdentificationNumberByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query( value="delete from EMPLOYEE_NATIONAL_IDENTIFICATION where EMPLOYEE_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteNationalIdentificationByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
}