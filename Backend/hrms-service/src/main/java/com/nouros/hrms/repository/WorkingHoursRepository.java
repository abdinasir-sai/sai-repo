package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.WorkingHours;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The WorkingHoursRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link WorkingHours} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface WorkingHoursRepository extends GenericRepository<WorkingHours> {


}
