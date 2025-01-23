package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface HealthClubBenefitRepository extends GenericRepository<HealthClubBenefit> {

	@Query("SELECT hcb FROM HealthClubBenefit hcb WHERE hcb.workflowStage=:workflowStage AND hcb.employee.id =:employeeId  ")
	List<HealthClubBenefit> findByEmployeeIdAndWorkflowStage(@Param("employeeId") Integer employeeId,
			@Param("workflowStage") String workflowStage);

	Optional<HealthClubBenefit> findById(Integer id);

//	@Query(value = "SELECT COUNT(*) FROM HEALTH_CLUB_BENEFIT WHERE EMPLOYEE_ID = :employeeId AND YEAR(CREATED_TIME) = YEAR(CURDATE()) AND WORKFLOW_STAGE IN ('Approved', 'Paid')",  nativeQuery = true)
//    int countByEmployeeIdAndWorkflowStageInCurrentYear(
//     @Param("employeeId") Integer employeeId);
	
	@Query(value = "SELECT COUNT(*) FROM HEALTH_CLUB_BENEFIT chd INNER JOIN EMPLOYEE e ON chd.EMPLOYEE_ID = e.ID WHERE chd.EMPLOYEE_ID =:employeeId AND CURDATE() BETWEEN e.DATE_OF_JOINING AND DATE_ADD(e.DATE_OF_JOINING , INTERVAL 1 YEAR) AND chd.WORKFLOW_STAGE = 'Approved' AND chd.CUSTOMER_ID = :customerId ", nativeQuery = true)
int countByEmployeeIdWithinOneYearOfJoining(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);
	
	
	@Query("SELECT hcb FROM HealthClubBenefit hcb WHERE hcb.processInstanceId =:processInstanceId ")
	Optional<HealthClubBenefit> findByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
	
	@Query(value = "SELECT SUM(AMOUNT_PAID) FROM HEALTH_CLUB_BENEFIT WHERE EMPLOYEE_ID = ?1 AND YEAR(START_DATE) = YEAR(CURDATE()) AND DELETED = 0 AND WORKFLOW_STAGE = 'Approved' AND CUSTOMER_ID = ?2  ", nativeQuery = true)
    Double getTotalAmountPaidForYearByEmployeeInApproved(Integer employeeId, Integer customerId);
	
	@Query(value = "SELECT SUM(AMOUNT_PAID) FROM HEALTH_CLUB_BENEFIT WHERE EMPLOYEE_ID = ?1 AND YEAR(START_DATE) = YEAR(CURDATE()) AND DELETED = 0 AND WORKFLOW_STAGE IN ('Approved', 'Submitted') AND CUSTOMER_ID = ?2  ", nativeQuery = true)
    Double getTotalAmountPaidForYearByEmployee(Integer employeeId, Integer customerId);
	
	@Query(value = "SELECT * FROM HEALTH_CLUB_BENEFIT WHERE WORKFLOW_STAGE =:workflowStage AND CREATED_TIME >=:beforeDate AND CREATED_TIME<=:curDate AND CUSTOMER_ID = :customerId ",nativeQuery = true)
	List<HealthClubBenefit> getHealthClubBenefitByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate, @Param("customerId") Integer customerId);

	@Query(value="INSERT INTO FILE_DATA (TYPE,RECORD_DATE,FILE_PATH,YEAR,WEEK_NUM,CUSTOMER_ID) VALUES(:type,:recordDate,:path,:year,:weekNum,:customerId)",nativeQuery = true)
    void setDataInFileData(@Param("type")String type,@Param("recordDate")LocalDate recordDate,@Param("path")String path,@Param("year")Integer year,@Param("weekNum")Integer weekNum, @Param("customerId") Integer customerId);

	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE TYPE=:type AND WEEK_NUM=:weekNUM AND YEAR=:year AND hcb.CUSTOMER_ID = :customerId " ,nativeQuery = true)
	String getFilePath(@Param("type")String type,@Param("weekNUM")Integer weekNUM,@Param("year")Integer year, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1, SUM(hcb.AMOUNT) from HEALTH_CLUB_BENEFIT hcb INNER JOIN EMPLOYEE e ON hcb.EMPLOYEE_ID = e.ID WHERE hcb.CREATED_TIME >=:startDate AND hcb.CREATED_TIME<=:endDate AND hcb.CUSTOMER_ID = :customerId  GROUP BY e.TEXT1  ",nativeQuery = true)
	List<Object[]> getHealthClubBenefitByDates(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1 ,SUM(hcb.AMOUNT) from HEALTH_CLUB_BENEFIT hcb INNER JOIN EMPLOYEE e ON hcb.EMPLOYEE_ID = e.ID WHERE hcb.CREATED_TIME >=:startDate AND hcb.CREATED_TIME<=:endDate AND hcb.CUSTOMER_ID = :customerId  ",nativeQuery = true)
	List<Object[]> getHealthClubBenefitByDatesAcc(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

}
