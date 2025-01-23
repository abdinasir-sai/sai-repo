package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeKraMapping;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The EmployeeKraMappingRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeKraMapping} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface EmployeeKraMappingRepository extends GenericRepository<EmployeeKraMapping> {


}
