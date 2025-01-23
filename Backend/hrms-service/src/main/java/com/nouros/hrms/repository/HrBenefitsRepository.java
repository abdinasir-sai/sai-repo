package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface HrBenefitsRepository extends GenericRepository<HrBenefits>{

	@Query(value = "SELECT e.TEXT1 , SUM(hr.AMOUNT) FROM HR_BENEFITS hr INNER JOIN EMPLOYEE e ON hr.EMPLOYEE_ID = e.ID WHERE hr.GL_STATUS =:status  AND hr.BENEFIT_TYPE =:type AND hr.ID IN (:idList) AND e.TEXT1 IS NOT NULL AND hr.CUSTOMER_ID = :customerId  GROUP BY e.TEXT1 " ,nativeQuery= true)
	List<Object[]> getHRBenefitsForExpense(@Param("status")String status , @Param("type")String type,@Param("idList")List<Integer> idList, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT SUM(h.AMOUNT) FROM HR_BENEFITS h INNER JOIN EMPLOYEE e ON h.EMPLOYEE_ID = e.ID WHERE h.GL_STATUS =:status  AND h.BENEFIT_TYPE =:type AND h.ID IN (:idList) AND e.TEXT1 IS NOT NULL AND h.CUSTOMER_ID = :customerId " ,nativeQuery= true)
	 Object[] getHRBenefitsForAccural(@Param("status")String status , @Param("type")String type,@Param("idList")List<Integer> idList, @Param("customerId") Integer customerId);
	
	@Query(value = "UPDATE HR_BENEFITS SET GL_STATUS =:newStatus where GL_STATUS =:oldStatus  AND WORKFLOW_STAGE=:stage AND hrb.CUSTOMER_ID = :customerId ",nativeQuery = true)
	Integer updateGLStatus(@Param("newStatus")String newStatus , @Param("oldStatus")String oldStatus,@Param("stage")String stage, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM HR_BENEFITS hrb WHERE hrb.EMPLOYEE_ID=:employeeId AND hrb.BENEFIT_TYPE=:type AND hrb.WORKFLOW_STAGE=:workFlowStage AND hrb.CUSTOMER_ID = :customerId" ,nativeQuery = true)
    List<HrBenefits> getBenefitList(@Param("employeeId")Integer employeeId,@Param("type")String type,@Param("workFlowStage")String workFlowStage, @Param("customerId") Integer customerId);	
	
	@Query(value="SELECT * FROM HR_BENEFITS hrb WHERE hrb.EMPLOYEE_ID =:employeeId AND hrb.BENEFIT_TYPE =:type AND WORKFLOW_STAGE =:workFlowStage AND YEAR(CREATED_TIME) = YEAR(CURRENT_DATE) AND hrb.CUSTOMER_ID = :customerId" ,nativeQuery = true)
    List<HrBenefits> getBenefitListForCurrentYear(@Param("employeeId")Integer employeeId,@Param("type")String type,@Param("workFlowStage")String workFlowStage, @Param("customerId") Integer customerId);	

	@Query(value = "SELECT hrb.* FROM HR_BENEFITS hrb INNER JOIN EMPLOYEE_CHILDREN ec ON hrb.ID = ec.HR_BENEFITS_ID WHERE hrb.EMPLOYEE_ID =:employeeId AND hrb.BENEFIT_TYPE =:type AND hrb.WORKFLOW_STAGE =:workFlowStage AND ec.NAME =:name AND hrb.SCHOOL_YEAR=:schoolYear AND hrb.CUSTOMER_ID = :customerId",nativeQuery = true)
	 List<HrBenefits> getBenefitListForChildEducation(@Param("employeeId")Integer employeeId,@Param("type")String type,@Param("workFlowStage")String workFlowStage,@Param("name")String name,@Param("schoolYear") Date schoolYear , @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM HR_BENEFITS WHERE WORKFLOW_STAGE =:workflowStage AND MODIFIED_TIME >=:beforeDate AND MODIFIED_TIME<=:curDate AND BENEFIT_TYPE=:benefit AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<HrBenefits> getBenefitsByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate,@Param("benefit")String benefit, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM HR_BENEFITS WHERE OTHER_EXPENSE_BANK_REQUEST_ID=:otherExpenseBankRequestId YEAR(h.CREATED_TIME) = YEAR(CURDATE()) AND CUSTOMER_ID = :customerId",nativeQuery =true)
	List<HrBenefits> getListOfHrBenefitsByOtherExpenseBankRequestId(@Param("otherExpenseBankRequestId")Integer otherExpenseBankRequestId, @Param("customerId") Integer customerId);

    @Query(value="SELECT SUM(h.AMOUNT) FROM HR_BENEFITS WHERE BENEFIT_TYPE=:benefitType AND EMPLOYEE_ID=:employeeId AND WORKFLOW_STAGE=:workflowStage AND YEAR(h.CREATED_TIME) = YEAR(CURDATE()) AND CUSTOMER_ID = :customerId",nativeQuery = true)
     Double getSumOfAmountOfBenefit(@Param("benefitType")String benefitType,@Param("employeeId")Integer employeeId,@Param("workflowStage")String workflowStage, @Param("customerId") Integer customerId);
}
