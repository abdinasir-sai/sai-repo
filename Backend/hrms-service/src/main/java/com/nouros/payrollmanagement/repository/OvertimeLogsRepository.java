package com.nouros.payrollmanagement.repository;
 
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.OvertimeLogs;

@Repository
public interface OvertimeLogsRepository extends GenericRepository<OvertimeLogs>{
	@Query("SELECT o FROM OvertimeLogs o WHERE o.id = :id")
	OvertimeLogs getById(@Param("id")Integer id);
	
	@Query("SELECT ol FROM OvertimeLogs ol WHERE ol.overtime.id =:overtimeId AND ol.overtimeDate BETWEEN :startDate AND :endDate AND ol.overtime.workflowStage =:workflowStage")
	List<OvertimeLogs> findByOvertimeIdAndPayrollDate(@Param("overtimeId")Integer overtimeId , @Param("startDate")Date startDate,@Param("endDate")Date endDate,@Param("workflowStage")String workflowStage);
	
	@Query("SELECT ol FROM OvertimeLogs ol WHERE ol.overtime.id =:overtimeId")
	List<OvertimeLogs> findByOvertimeId(@Param("overtimeId")Integer overtimeId);
	
	@Query( value="delete from OVERTIME_LOGS where OVERTIME_ID =:id", nativeQuery=true)
	void deleteAllOvertimeLogsAssociatedWithOvertime(@Param("id") Integer id);
	
	@Query("SELECT ol FROM OvertimeLogs ol WHERE ol.workflowStage=:workflowStage  AND ol.overtimeDate<=:overtimeLogDate AND ol.overtime.employee.id=:employeeId ")
	List<OvertimeLogs> getOvertimeLogsByWorkflowStageAndDate(@Param("workflowStage")String workflowStage , @Param("overtimeLogDate")Date overtimeLogDate,@Param("employeeId")Integer employeeId);
}
