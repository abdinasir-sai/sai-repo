package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface ApplicantResumeRepository extends GenericRepository<ApplicantResume> {

	@Query("SELECT ar FROM ApplicantResume ar WHERE ar.applicantId.id=:applicantId and ar.customerId= :customerId")
	List<ApplicantResume> findAllApplicantResumeByApplicantId(@Param("applicantId") Integer applicantId, @Param("customerId") Integer customerId);


}
