package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantDependentDetails;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface ApplicantDependentDetailsRepository extends GenericRepository<ApplicantDependentDetails>{

	@Query(value = "SELECT * FROM APPLICANT_DEPENDENT_DETAILS WHERE APPLICANT_FK = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<ApplicantDependentDetails> getDependentDetailsByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query( value="delete from APPLICANT_NATIONAL_IDENTIFICATION where APPLICANT_FK =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteDependentDetailsByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
}
