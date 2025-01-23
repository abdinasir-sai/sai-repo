package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.AttendanceRegularization;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The AttendanceRegularizationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link AttendanceRegularization} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface AttendanceRegularizationRepository extends GenericRepository<AttendanceRegularization> {


}
