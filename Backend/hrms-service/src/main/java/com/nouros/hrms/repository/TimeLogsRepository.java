package com.nouros.hrms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TimeLogs;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The TimeLogsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link TimeLogs} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface TimeLogsRepository extends GenericRepository<TimeLogs> {

	 @Query(value="select * from TIME_LOGS where USER_NAME = :un and FROM_TIME BETWEEN :frt and :tt and TO_TIME BETWEEN :frt and :tt AND CUSTOMER_ID =:customerId",nativeQuery=true)
		List<TimeLogs> getTimeLogsByUserNameAndWeeks(@Param("un")String un,@Param("frt")Date frt,@Param("tt")Date tt, @Param("customerId") Integer customerId);
	  
	 @Query(value="select * from TIME_LOGS where EMPLOYEE_ID = :employeeUserId and  WEEK_NO = :weekNumber AND CUSTOMER_ID =:customerId",nativeQuery=true)
	 List<TimeLogs> getTimeLogsByEmployeeIdAndWeekNumber(@Param("employeeUserId") Integer employeeUserId, @Param("weekNumber") Integer weekNumber, @Param("customerId") Integer customerId);

	@Query(value="delete from TIME_LOGS where EMPLOYEE_ID = :empId and  WEEK_NO = :weekNumber AND CUSTOMER_ID =:customerId",nativeQuery=true)
	void deleteTimeLogsByEmployeeIdAndWeekNumber(@Param("empId") Integer empId, @Param("weekNumber") Integer weekNumber, @Param("customerId") Integer customerId);

   @Query(value="select * from TIME_LOGS where TIMESHEET_FK = :timeSheetId AND CUSTOMER_ID =:customerId",nativeQuery=true)
	List<TimeLogs> getTimeLogsByTimeSheetId( @Param("timeSheetId") Integer timeSheetId, @Param("customerId") Integer customerId);

	@Query(value="delete from TIME_LOGS where TIMESHEET_FK = :id AND CUSTOMER_ID =:customerId ",nativeQuery=true)
	void deleteAllTimeLogsAssociatedWithTimeSheet( @Param("id") Integer id, @Param("customerId") Integer customerId);
}
