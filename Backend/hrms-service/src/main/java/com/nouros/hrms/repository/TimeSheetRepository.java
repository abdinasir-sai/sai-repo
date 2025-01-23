package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TimeSheet;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The TimeSheetRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link TimeSheet} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface TimeSheetRepository extends GenericRepository<TimeSheet> {

	@Query("SELECT t FROM TimeSheet t WHERE t.employee.id = :empId and t.weekNumber = :weekNumber")
	TimeSheet findTimeSheetByEmployeeIdAndWeekNumber(@Param("empId")Integer empId,@Param("weekNumber") Integer weekNumber);
	
}