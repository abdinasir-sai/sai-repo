package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.Overtime;


@Repository
public interface OvertimeRepository extends GenericRepository<Overtime>{

	@Query("SELECT o FROM Overtime o WHERE o.id = :id")
	Overtime getById(@Param("id")Integer id);
	
	
	@Query("SELECT ot from Overtime ot WHERE ot.employee.id =:employeeId AND ot.workflowStage =:workflowstage")
	List<Overtime> findByEmployeeId(@Param("employeeId") Integer employeeId , @Param("workflowstage")String workflowstage);
	
	@Query(value="SELECT * FROM OVERTIME WHERE ID = (SELECT MAX(ID) FROM OVERTIME WHERE EMPLOYEE_ID = :employeeUserId  AND OVERTIME_MONTH = :monthNumber AND OVERTIME_YEAR = :year AND DELETED=0) ",nativeQuery=true)
	Overtime getOvertimeByEmployeeIdAndMonthNumberAndYear(@Param("employeeUserId") Integer employeeUserId, @Param("monthNumber") Integer monthNumber, @Param("year") Integer year );

}
