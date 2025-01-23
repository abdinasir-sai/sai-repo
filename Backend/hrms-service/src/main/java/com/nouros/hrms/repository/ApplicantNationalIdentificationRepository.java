package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantNationalIdentification;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface ApplicantNationalIdentificationRepository extends GenericRepository<ApplicantNationalIdentification>{

	@Query(value = "SELECT * FROM APPLICANT_NATIONAL_IDENTIFICATION WHERE APPLICANT_FK = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<ApplicantNationalIdentification> getNationalIdentificationByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query( value="delete from APPLICANT_NATIONAL_IDENTIFICATION where APPLICANT_FK =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteNationalIdentificationByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
}
