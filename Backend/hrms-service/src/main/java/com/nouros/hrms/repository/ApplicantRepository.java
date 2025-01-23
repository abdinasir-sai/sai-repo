package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The ApplicantRepository interface is a extension of
 * {@link GenericRepository}. It's purpose is to provide additional methods that
 * are specific to the {@link Applicant} entity. see GenericRepository see
 * JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface ApplicantRepository extends GenericRepository<Applicant> {

	@Query(value = "SELECT CONCAT('Review the salary range for job offer of position ', '<span class=\"highlighted-reminders-title\">', jo.POSTING_TITLE, '</span>', ' and candidate <span class=\"highlighted-reminders-title\">', a.FIRST_NAME, ' ', a.LAST_NAME, '</span>') as Result, a.APPLICANT_UNIQUE_ID as applicantId, jo.JOB_ID as jobOpeningId FROM APPLICANT a INNER JOIN OFFERS o ON a.ID = o.CANDIDATE_ID INNER JOIN JOB_OPENING jo ON o.POSTING_TITLE = jo.ID WHERE o.OFFER_STATUS='Accepted' AND a.APPLICANT_STATUS='New' AND DATE(a.CREATED_TIME)=CURRENT_DATE AND a.CUSTOMER_ID = :customerId AND o.CUSTOMER_ID = :customerId AND jo.CUSTOMER_ID = :customerId;", nativeQuery = true)
	List<Object[]> reviewTheSalaryForJobOfferPositionAndCandidate( @Param("customerId") Integer customerId);

	@Query(value = "SELECT CONCAT('Review the job offer for position ', '<span class=\"highlighted-reminders-title\">', jo.POSTING_TITLE, '</span>', ' and candidate <span class=\"highlighted-reminders-title\">', a.FIRST_NAME, ' ', a.LAST_NAME, '</span>') as Result, a.APPLICANT_UNIQUE_ID as applicantId, jo.JOB_ID as jobOpeningId FROM APPLICANT a INNER JOIN OFFERS o ON a.ID = o.CANDIDATE_ID INNER JOIN JOB_OPENING jo ON o.POSTING_TITLE = jo.ID WHERE o.OFFER_STATUS='Accepted' AND a.APPLICANT_STATUS='New' AND DATE(a.CREATED_TIME)=CURRENT_DATE AND a.CUSTOMER_ID = :customerId AND o.CUSTOMER_ID = :customerId AND jo.CUSTOMER_ID = :customerId;", nativeQuery = true)
	List<Object[]> reviewJobOfferForPositionAndCandidate(@Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM APPLICANT WHERE DATE(CREATED_TIME) = CURDATE() and AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<Applicant> findAllByCreatedDate(@Param("customerId") Integer customerId);

	@Query(value = "SELECT a.ID FROM APPLICANT a WHERE DATE(a.CREATED_TIME) = CURDATE() AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<Object[]> findApplicantIdByCreatedDate(@Param("customerId") Integer customerId);
	
    @Query(value ="SELECT * FROM APPLICANT a WHERE a.USER_ID = :userId AND CUSTOMER_ID = :customerId" , nativeQuery = true)
 	Applicant findByUserId(@Param("userId") Integer userId , @Param("customerId") Integer customerId);

    @Query(value = "SELECT * FROM APPLICANT a WHERE a.EMAIL_ID = :emailId AND CUSTOMER_ID = :customerId", nativeQuery = true)
    Applicant findByEmailId(@Param("emailId") String emailId , @Param("customerId") Integer customerId);

    @Query("SELECT a FROM Applicant a WHERE a.id = :id and a.customerId= :customerId")
	Applicant findApplicantById(@Param("id") Integer id, @Param("customerId") Integer customerId);
    
    @Query(value ="SELECT * FROM APPLICANT a WHERE a.PROCESS_INSTANCE_ID = :id AND DELETED=0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
    Applicant findApplicantByProcessInstanceId(@Param("id") String id, @Param("customerId") Integer customerId);
    
    @Query(value ="SELECT * FROM APPLICANT a WHERE a.ID = :id AND DELETED=0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
 	Applicant findActiveApplicantById(@Param("id") Integer id, @Param("customerId") Integer customerId);
    
    
}
