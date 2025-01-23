package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.BusinessPlan;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The BusinessPlanRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link BusinessPlan} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface BusinessPlanRepository extends GenericRepository<BusinessPlan> {


	@Query(value = "SELECT CONCAT('Review the new recruitment request for position ', '<span class=\"highlighted-reminders-title\">', bp.TITLE, '</span>') as Result, bp.TITLE as title FROM BUSINESS_PLAN bp WHERE bp.PLAN_DATE >= CURDATE() AND bp.PLAN_DATE <= DATE_ADD(CURDATE(), INTERVAL 3 MONTH) AND NOT EXISTS (SELECT 1 FROM JOB_OPENING jo WHERE jo.POSTING_TITLE = bp.TITLE AND jo.CUSTOMER_ID = :customerId AND jo.JOB_OPENING_STATUS IN ('Active','waiting for approval','filled','on-hold') AND jo.OPEN_POSITIONS >= bp.PLAN_COUNT AND jo.TARGET_CLOSSING_DATE BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)) AND bp.PLAN_COUNT IS NOT NULL AND bp.CUSTOMER_ID = :customerId GROUP BY bp.TITLE;", nativeQuery = true)
	List<Object[]> findDataForNewRecruitment( @Param("customerId") Integer customerId);

	@Query(value = "SELECT bp.* FROM BUSINESS_PLAN bp WHERE bp.TITLE=?1 and MONTH(bp.PLAN_DATE)=MONTH(?2) AND YEAR(bp.PLAN_DATE)=YEAR(?2) AND bp.CUSTOMER_ID = ?3",nativeQuery = true)
	BusinessPlan findByTitleAndMonth(@Param("title") String title,@Param("date")  String date, @Param("customerId") Integer customerId);

}
