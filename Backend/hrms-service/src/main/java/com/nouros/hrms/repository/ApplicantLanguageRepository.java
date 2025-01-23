package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.repository.generic.GenericRepository;



@Repository
@Transactional(readOnly = true)
public interface ApplicantLanguageRepository extends GenericRepository<ApplicantLanguage>{

	@Query("SELECT al FROM ApplicantLanguage al WHERE al.applicantId.id = :id and al.customerId= :customerId")
	List<ApplicantLanguage> getLanguagesForApplicant(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query( value="delete from APPLICANT_LANGUAGE where APPLICANT_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteLanguageByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);

}
