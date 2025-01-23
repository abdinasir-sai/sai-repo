package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Country;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The CountryRepository interface is a extension of {@link GenericRepository}.
 * It's purpose is to provide additional methods that are specific to the
 * {@link Country} entity. see GenericRepository see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface CountryRepository extends GenericRepository<Country> {

	@Query(value = "SELECT * FROM COUNTRY WHERE NAME = :name AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Country findCountryByName(@Param("name") String name, @Param("customerId") Integer customerId);

}
