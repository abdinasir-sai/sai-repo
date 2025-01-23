package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.City;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The CityRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link City} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface CityRepository extends GenericRepository<City> {

	@Query(value = "SELECT * FROM CITY WHERE NAME = :name AND CUSTOMER_ID = :customerId", nativeQuery = true)
	City findCityByName(@Param("name") String name, @Param("customerId") Integer customerId);


}
