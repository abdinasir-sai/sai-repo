package com.nouros.hrms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends GenericRepository<AccountDetails>{
	
	@Query(value ="SELECT * FROM ACCOUNT_DETAILS WHERE EMPLOYEE_ID = :id AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<AccountDetails> findAccountDetailsByEmployeeId(@Param("id") Integer id, @Param("customerId") Integer customerId);

}
