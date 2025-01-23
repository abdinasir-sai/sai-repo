package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeMonthlyAttendance;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeMonthlyAttendanceRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeMonthlyAttendance} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeMonthlyAttendanceRepository extends GenericRepository<EmployeeMonthlyAttendance> {


}
