package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The CandidateEducationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link ApplicantEducation} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface ApplicantEducationRepository extends GenericRepository<ApplicantEducation> {

	@Query("SELECT ae FROM ApplicantEducation ae WHERE ae.applicant.id = :id and ae.customerId= :customerId")
	List<ApplicantEducation> getEducationsForApplicant(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query( value="delete from APPLICANT_EDUCATION where APPLICANT_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteEducationByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
}
