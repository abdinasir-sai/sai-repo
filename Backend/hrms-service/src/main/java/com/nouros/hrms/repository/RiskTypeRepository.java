package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.RiskType;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The RiskTypeRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link RiskType} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface RiskTypeRepository extends GenericRepository<RiskType> {


}
