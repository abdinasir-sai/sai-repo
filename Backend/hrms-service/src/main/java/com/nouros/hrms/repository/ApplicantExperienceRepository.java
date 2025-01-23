package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The ApplicantExperienceRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link ApplicantExperience} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface ApplicantExperienceRepository extends GenericRepository<ApplicantExperience> {

	@Query("SELECT ax FROM ApplicantExperience ax WHERE ax.applicant.id = :id and ax.customerId= :customerId")
	List<ApplicantExperience> getExperienceForApplicant(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query( value="delete from APPLICANT_EXPERIENCE where APPLICANT_ID =:id AND CUSTOMER_ID = :customerId", nativeQuery=true)
	void deleteExperienceByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);


}
