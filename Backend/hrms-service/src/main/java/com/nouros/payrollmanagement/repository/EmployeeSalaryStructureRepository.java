package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;

/**

The EmployeeSalaryStructureRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeSalaryStructure} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeSalaryStructureRepository extends GenericRepository<EmployeeSalaryStructure> {

	 // @Query("SELECT emss FROM EmployeeSalaryStructure emss WHERE  emss.employee.id  = :employeeId ")
	  List<EmployeeSalaryStructure> findByEmployeeId(@Param("employeeId") Integer employeeId);
	
	  
	      @Query("SELECT emss FROM EmployeeSalaryStructure emss WHERE  emss.employee.id  = :employeeId  and emss.deleted = :deleted ")
		  List<EmployeeSalaryStructure> findByEmployeeIdAndDeleted(@Param("employeeId") Integer employeeId , @Param("deleted") Boolean deleted);
		  
		  
		  
		 

}
