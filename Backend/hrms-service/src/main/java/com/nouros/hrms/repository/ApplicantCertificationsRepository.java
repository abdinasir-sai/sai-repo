
package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.repository.generic.GenericRepository;



@Repository
@Transactional(readOnly = true)
public interface ApplicantCertificationsRepository extends GenericRepository<ApplicantCertifications>{

	@Query("SELECT ac FROM ApplicantCertifications ac WHERE ac.applicantId.id = :id and ac.customerId= :customerId")
	List<ApplicantCertifications> getCertificationsForApplicant(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query(value = "SELECT CERTIFICATION_NAME FROM APPLICANT_CERTIFICATIONS WHERE APPLICANT_ID = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
    List<String> getCertificatesNameByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query( value="delete from APPLICANT_CERTIFICATIONS where APPLICANT_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteCertificationsByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
}
