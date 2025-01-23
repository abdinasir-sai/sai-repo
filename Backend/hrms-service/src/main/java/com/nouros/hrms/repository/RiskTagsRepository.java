package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.RiskTags;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The RiskTagsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link RiskTags} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface RiskTagsRepository extends GenericRepository<RiskTags> {


}
