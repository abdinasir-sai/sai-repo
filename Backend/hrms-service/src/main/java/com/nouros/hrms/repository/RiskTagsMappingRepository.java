package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.RiskTagsMapping;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The RiskTagsMappingRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link RiskTagsMapping} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface RiskTagsMappingRepository extends GenericRepository<RiskTagsMapping> {


}
