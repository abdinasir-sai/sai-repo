package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Offers;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The OffersRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Offers} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface OffersRepository extends GenericRepository<Offers>{
	@Query(name="SELECT o FROM Offers o WHERE o.processInstanceId = ?1 ")
	Offers findByProcessInstanceId(@Param("processInstanceId") String processInstanceId);


	
	@Query("SELECT o FROM Offers o WHERE o.isOfferEmailSent=:email and o.isSecurityEmailSent=:security")
	List<Offers> findOffersForEmails(@Param("email") boolean isOfferEmailSent, @Param("security") boolean isSecurityEmailSent);
	
	@Query(value = "SELECT * FROM OFFERS WHERE APPLICANT_ID = :id and DELETED=0 AND OFFER_STATUS='Accepted' AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Offers findByApplicantId(@Param("id") Integer id, @Param("customerId") Integer customerId);
    
}
