package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Location;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The LocationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Location} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface LocationRepository extends GenericRepository<Location> {

	@Query(value = "SELECT * FROM LOCATION WHERE NAME = :locationName AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Location findByName(@Param("locationName") String locationName, @Param("customerId") Integer customerId);


}
