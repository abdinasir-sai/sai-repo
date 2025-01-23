package com.nouros.payrollmanagement.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructureHistory;

/**

The EmployeeSalaryStructureHistoryRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeSalaryStructureHistory} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeSalaryStructureHistoryRepository extends GenericRepository<EmployeeSalaryStructureHistory> {
	
    @Query("Select essh from EmployeeSalaryStructureHistory essh where essh.employee.id = :employeeId and   essh.startDate <= :givenDate  and   essh.endDate >= :givenDate  ")
	List<EmployeeSalaryStructureHistory> getHistoryInGivenDate(@Param("givenDate") Date givenDate ,@Param("employeeId") Integer employeeId);

}
