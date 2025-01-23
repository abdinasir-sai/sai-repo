package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List; 
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.data.repository.query.Param;


@Repository
@Transactional(readOnly = true)
public interface ChildEducationBenefitRepository extends GenericRepository<ChildEducationBenefit>  {

	@Query("SELECT ceb FROM ChildEducationBenefit ceb WHERE ceb.workflowStage =:workflowStage AND ceb.employee.id =:employeeId and ceb.customerId = :customerId")
	 List<ChildEducationBenefit>  findByEmployeeIdAndWorkflowStage(@Param("employeeId") Integer employeeId,@Param("workflowStage") String workflowStage, @Param("customerId") Integer customerId);
	
	Optional<ChildEducationBenefit> findById(Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM CHILD_EDUCATION_BENEFIT chd INNER JOIN EMPLOYEE e ON chd.EMPLOYEE_ID = e.ID "
			+ "WHERE chd.EMPLOYEE_ID =:employeeId "
			+ "AND CURDATE() "
			+ "BETWEEN e.DATE_OF_JOINING AND DATE_ADD(e.DATE_OF_JOINING , INTERVAL 1 YEAR) AND chd.customerId = :customerId AND e.customerId = :customerId ",  nativeQuery = true)
    int countEmployeeByEmployeeIdAndDateOfJoining(
     @Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);
	
	
	@Query("SELECT ceb FROM ChildEducationBenefit ceb WHERE ceb.processInstanceId =:processInstanceId and ceb.customerId = :customerId ")
	Optional<ChildEducationBenefit> findByProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT * FROM CHILD_EDUCATION_BENEFIT WHERE WORKFLOW_STAGE =:workflowStage AND CREATED_TIME >=:beforeDate AND CREATED_TIME<=:curDate AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<ChildEducationBenefit> getChildEducationBenefitByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate, @Param("customerId") Integer customerId);

	@Query(value="INSERT INTO FILE_DATA (TYPE,RECORD_DATE,FILE_PATH,YEAR,WEEK_NUM) VALUES(:type,:recordDate,:path,:year,:weekNum)",nativeQuery = true)
    void setDataInFileData(@Param("type")String type,@Param("recordDate")LocalDate recordDate,@Param("path")String path,@Param("year")Integer year,@Param("weekNum")Integer weekNum);

	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE TYPE=:type AND WEEK_NUM=:weekNUM AND YEAR=:year AND CUSTOMER_ID = :customerId" ,nativeQuery = true)
	String getFilePath(@Param("type")String type,@Param("weekNUM")Integer weekNUM,@Param("year")Integer year, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1, SUM(ceb.AMOUNT) from CHILD_EDUCATION_BENEFIT ceb INNER JOIN EMPLOYEE e ON ceb.EMPLOYEE_ID = e.ID WHERE ceb.CREATED_TIME >=:startDate AND ceb.CREATED_TIME<=:endDate AND ceb.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId GROUP BY e.TEXT1  ",nativeQuery = true)
	List<Object[]> getChildEducationBenefitByDates(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1 ,SUM(ceb.AMOUNT) from CHILD_EDUCATION_BENEFIT ceb INNER JOIN EMPLOYEE e ON ceb.EMPLOYEE_ID = e.ID WHERE ceb.CREATED_TIME >=:startDate AND ceb.CREATED_TIME<=:endDate AND ceb.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId  ",nativeQuery = true)
	List<Object[]> getChildEducationBenefitByDatesAcc(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate , @Param("customerId") Integer customerId);

}
