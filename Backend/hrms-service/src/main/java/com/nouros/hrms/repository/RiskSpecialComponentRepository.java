package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.RiskSpecialComponent;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The RiskSpecialComponentRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link RiskSpecialComponent} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface RiskSpecialComponentRepository extends GenericRepository<RiskSpecialComponent> {


}
