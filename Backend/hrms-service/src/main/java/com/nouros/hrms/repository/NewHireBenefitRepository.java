package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface NewHireBenefitRepository extends GenericRepository<NewHireBenefit>{

	@Query("SELECT nhb FROM NewHireBenefit nhb WHERE nhb.workflowStage=:workflowStage AND nhb.employee.id =:employeeId ")
	List<NewHireBenefit> findByEmployeeIdAndWorkflowStage(@Param("employeeId") Integer employeeId,@Param("workflowStage") String workflowStage);

	Optional<NewHireBenefit> findById(Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM NEW_HIRE_BENEFIT chd INNER JOIN EMPLOYEE e ON chd.EMPLOYEE_ID = e.ID WHERE chd.EMPLOYEE_ID =:employeeId AND CURDATE() BETWEEN e.DATE_OF_JOINING AND DATE_ADD(e.DATE_OF_JOINING , INTERVAL 1 YEAR) AND chd.CUSTOMER_ID = :customerId", nativeQuery = true)
int countByEmployeeIdWithinOneYearOfJoining(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);

	@Query("SELECT nhb FROM NewHireBenefit nhb WHERE nhb.processInstanceId =:processInstanceId ")
	Optional<NewHireBenefit> findByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

	@Query(value = "SELECT COUNT(*) FROM NEW_HIRE_BENEFIT WHERE EMPLOYEE_ID = ?1 AND DELETED = 0 AND WORKFLOW_STAGE = 'Approved' AND CUSTOMER_ID = ?2", nativeQuery = true)
	int countByEmployee(Integer employeeId,  Integer customerId);
	
	@Query(value = "SELECT * FROM NEW_HIRE_BENEFIT WHERE WORKFLOW_STAGE =:workflowStage AND CREATED_TIME >=:beforeDate AND CREATED_TIME<=:curDate AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<NewHireBenefit> getNewHireBenefitByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate, @Param("customerId") Integer customerId);

	@Query(value="INSERT INTO FILE_DATA (TYPE,RECORD_DATE,FILE_PATH,YEAR,WEEK_NUM,CUSTOMER_ID) VALUES(:type,:recordDate,:path,:year,:weekNum,:customerId)",nativeQuery = true)
    void setDataInFileData(@Param("type")String type,@Param("recordDate")LocalDate recordDate,@Param("path")String path,@Param("year")Integer year,@Param("weekNum")Integer weekNum, @Param("customerId") Integer customerId);

	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE TYPE=:type AND WEEK_NUM=:weekNUM AND YEAR=:year AND CUSTOMER_ID = :customerId" ,nativeQuery = true)
	String getFilePath(@Param("type")String type,@Param("weekNUM")Integer weekNUM,@Param("year")Integer year, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1, SUM(nhb.AMOUNT) from NEW_HIRE_BENEFIT nhb INNER JOIN EMPLOYEE e ON nhb.EMPLOYEE_ID = e.ID WHERE nhb.CREATED_TIME >=:startDate AND nhb.CREATED_TIME<=:endDate AND nhb.CUSTOMER_ID= :customerId  GROUP BY e.TEXT1  ",nativeQuery = true)
	List<Object[]> getNewHireBenefitByDates(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

	@Query(value=" SELECT e.TEXT1 ,SUM(nhb.AMOUNT) from NEW_HIRE_BENEFIT nhb INNER JOIN EMPLOYEE e ON nhb.EMPLOYEE_ID = e.ID WHERE nhb.CREATED_TIME >=:startDate AND nhb.CREATED_TIME<=:endDate AND nhb.CUSTOMER_ID= :customerId  ",nativeQuery = true)
	List<Object[]> getNewHireBenefitByDatesAcc(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate, @Param("customerId") Integer customerId);

	
}
