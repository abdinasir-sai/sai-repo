package com.nouros.hrms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Interview;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The InterviewRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Interview} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface InterviewRepository extends GenericRepository<Interview> {

	@Query(name = "SELECT i FROM Interview i WHERE i.processInstanceId = ?1")
	Interview findByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

	@Query("SELECT iv FROM Interview iv WHERE iv.applicant.id = :applicantId AND iv.jobOpening.id = :jobOpeningId")
	Interview findInterviewByApplicantAndJobOpening(@Param("applicantId") Integer applicantId, @Param("jobOpeningId") Integer jobOpeningId);


}
