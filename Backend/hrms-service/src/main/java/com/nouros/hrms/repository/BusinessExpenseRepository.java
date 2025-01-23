package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface BusinessExpenseRepository extends GenericRepository<BusinessExpense>{

	@Query("SELECT be FROM BusinessExpense be WHERE be.processInstanceId =:processInstanceId and be.customerId= :customerId")
	Optional<BusinessExpense> findByProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM BUSINESS_EXPENSE WHERE WORKFLOW_STAGE =:workflowStage AND CREATED_TIME >=:beforeDate AND CREATED_TIME<=:curDate AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<BusinessExpense> getBusinessExpenseByWorkflowStageAndDate(@Param("workflowStage") String workflowStage,@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate, @Param("customerId") Integer customerId);

	@Query(value="INSERT INTO FILE_DATA (TYPE,RECORD_DATE,FILE_PATH,YEAR,WEEK_NUM) VALUES(:type,:recordDate,:path,:year,:weekNum)",nativeQuery = true)
    void setDataInFileData(@Param("type")String type,@Param("recordDate")LocalDate recordDate,@Param("path")String path,@Param("year")Integer year,@Param("weekNum")Integer weekNum);

	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE TYPE=:type AND WEEK_NUM=:weekNUM AND YEAR=:year AND CUSTOMER_ID = :customerId" ,nativeQuery = true)
	String getFilePath(@Param("type")String type,@Param("weekNUM")Integer weekNUM,@Param("year")Integer year, @Param("customerId") Integer customerId);


	@Query(value = "SELECT * FROM BUSINESS_EXPENSE WHERE OTHER_EXPENSE_BANK_REQUEST_ID=:otherExpenseBankRequestId AND CUSTOMER_ID = :customerId",nativeQuery =true)
	List<BusinessExpense> getBusinessExpenseListByBankRequestId(@Param("otherExpenseBankRequestId")Integer otherExpenseBankRequestId, @Param("customerId") Integer customerId);
	
	@Query(value ="SELECT e.TEXT1 , SUM(be.CLAIM_AMOUNT) FROM BUSINESS_EXPENSE be INNER JOIN EMPLOYEE e ON be.EMPLOYEE_ID = e.ID WHERE  be.ID IN (:businessExpenseList) AND e.TEXT1 IS NOT NULL AND be.CUSTOMER_ID= :customerId AND e.CUSTOMER_ID= :customerId  GROUP BY e.TEXT1  ",nativeQuery =true)
	List<Object []> getBusinessExpenseForExpense(@Param("businessExpenseList")List<Integer> businessExpenseList, @Param("customerId") Integer customerId);
	
	@Query(value ="SELECT  SUM(be.CLAIM_AMOUNT) FROM BUSINESS_EXPENSE be INNER JOIN EMPLOYEE e ON be.EMPLOYEE_ID = e.ID WHERE  be.ID IN (:businessExpenseList) AND e.TEXT1 IS NOT NULL AND be.CUSTOMER_ID= :customerId AND e.CUSTOMER_ID= :customerId ",nativeQuery =true)
	Object getBusinessExpenseForAccural(@Param("businessExpenseList")List<Integer> businessExpenseList, @Param("customerId") Integer customerId);
	
}
