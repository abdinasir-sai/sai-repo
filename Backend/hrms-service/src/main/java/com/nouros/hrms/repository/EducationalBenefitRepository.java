package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List; 
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.data.repository.query.Param;

@Repository
@Transactional(readOnly = true)
public interface EducationalBenefitRepository extends GenericRepository<EducationalBenefit>{

	@Query("SELECT eb FROM EducationalBenefit eb WHERE eb.workflowStage =:workflowStage AND eb.employee.id =:employeeId")
	List<EducationalBenefit> findByEmployeeIdAndWorkflowStage(@Param("employeeId") Integer employeeId,@Param("workflowStage") String workflowStage);
	
	Optional<EducationalBenefit> findById(Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM EDUCATIONAL_BENEFIT ed INNER JOIN EMPLOYEE e ON ed.EMPLOYEE_ID = e.ID "
			+ "WHERE ed.EMPLOYEE_ID =:employeeId AND ed.CREATED_TIME BETWEEN e.DATE_OF_JOINING AND DATE_ADD(e.DATE_OF_JOINING, INTERVAL 1 YEAR) "
			+ "AND ed.WORKFLOW_STAGE = 'Approved' AND ed.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId ",  nativeQuery = true)
    int countByEmployeeIdAndWorkflowStageInCurrentYear(
     @Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);
	
	@Query("SELECT eb FROM EducationalBenefit eb WHERE eb.processInstanceId =:processInstanceId ")
	Optional<EducationalBenefit> findByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
	
	@Query(value = "SELECT SUM(AMOUNT) FROM EDUCATIONAL_BENEFIT WHERE EMPLOYEE_ID = ?1 AND YEAR(START_DATE) = YEAR(CURDATE()) AND DELETED = 0 AND WORKFLOW_STAGE = 'Approved' AND CUSTOMER_ID = ?2 ", nativeQuery = true)
    Double getTotalAmountPaidForYearByEmployeeInApproval(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT SUM(AMOUNT) FROM EDUCATIONAL_BENEFIT WHERE EMPLOYEE_ID = ?1 AND YEAR(START_DATE) = YEAR(CURDATE()) AND DELETED = 0 AND WORKFLOW_STAGE IN ('Approved', 'Submitted') AND CUSTOMER_ID = ?2", nativeQuery = true)
    Double getTotalAmountPaidForYearByEmployee( @Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT * FROM EDUCATIONAL_BENEFIT WHERE WORKFLOW_STAGE =:workflowStage AND CREATED_TIME >=:beforeDate AND CREATED_TIME<=:curDate AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<EducationalBenefit> getEducationBenefitByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate, @Param("customerId") Integer customerId);

	@Query(value="INSERT INTO FILE_DATA (TYPE,RECORD_DATE,FILE_PATH,YEAR,WEEK_NUM) VALUES(:type,:recordDate,:path,:year,:weekNum)",nativeQuery = true)
    void setDataInFileData(@Param("type")String type,@Param("recordDate")LocalDate recordDate,@Param("path")String path,@Param("year")Integer year,@Param("weekNum")Integer weekNum);

	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE TYPE=:type AND WEEK_NUM=:weekNUM AND YEAR=:year AND CUSTOMER_ID = :customerId" ,nativeQuery = true)
	String getFilePath(@Param("type")String type,@Param("weekNUM")Integer weekNUM,@Param("year")Integer year, @Param("customerId") Integer customerId);
 
	@Query(value=" SELECT e.TEXT1, SUM(ed.AMOUNT) from EDUCATIONAL_BENEFIT ed INNER JOIN EMPLOYEE e ON ed.EMPLOYEE_ID = e.ID WHERE ed.CREATED_TIME >=:startDate AND ed.CREATED_TIME<=:endDate AND ed.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId GROUP BY e.TEXT1  ",nativeQuery = true)
	List<Object[]> getEducationalBenefitByDates(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1 ,SUM(ed.AMOUNT) from EDUCATIONAL_BENEFIT ed INNER JOIN EMPLOYEE e ON ed.EMPLOYEE_ID = e.ID WHERE ed.CREATED_TIME >=:startDate AND ed.CREATED_TIME<=:endDate AND ed.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId ",nativeQuery = true)
	List<Object[]> getEducationalBenefitByDatesAcc(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

}
