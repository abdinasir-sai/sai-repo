package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TravelRequest;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The TravelRequestRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link TravelRequest} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface TravelRequestRepository extends GenericRepository<TravelRequest> {


}
