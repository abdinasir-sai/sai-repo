package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;

public interface EmployeeMonthlySalaryJpaRepository extends JpaRepository<EmployeeMonthlySalary, Integer>{
	 
	 @Query("SELECT rd from EmployeeMonthlySalary rd where rd.employee.id =:employeeId and rd.payrollRun.workflowStage = 'PAYROLL_BANK_APPROVAL' and rd.payrollRun.deleted = false order by rd.id ")
	 List<EmployeeMonthlySalary> getPayHistoryRecord(@Param("employeeId") Integer employeeId);


}
