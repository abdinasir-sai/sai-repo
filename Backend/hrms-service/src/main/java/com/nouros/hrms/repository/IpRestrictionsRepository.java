package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.IpRestrictions;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The IpRestrictionsRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link IpRestrictions} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface IpRestrictionsRepository extends GenericRepository<IpRestrictions> {


}
