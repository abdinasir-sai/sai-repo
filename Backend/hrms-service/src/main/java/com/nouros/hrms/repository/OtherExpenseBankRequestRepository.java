package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(readOnly = true)
public interface OtherExpenseBankRequestRepository extends GenericRepository<OtherExpenseBankRequest> {

	
	@Query(value = "INSERT INTO OTHER_EXPENSE_BANK_REQUEST (TYPE,WPS_FILE_PATH,DATE,YEAR,WEEK_NUM,WORKFLOW_STAGE,EMPLOYEE_ID_LIST,CUSTOMER_ID) VALUES(:type,:path,:date,:year,:weekNumber,:stage,:json,:customerId) ",nativeQuery = true)
	void saveData(@Param("type")String type , @Param("path")String path,@Param("date") LocalDate date,@Param("year")Integer year,@Param("weekNumber") Integer weekNumber,@Param("stage")String stage,@Param("json")String json, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT WPS_FILE_PATH FROM OTHER_EXPENSE_BANK_REQUEST WHERE TYPE=:type AND WEEK_NUM =:weekNum AND YEAR =:year AND CUSTOMER_ID= :customerId" , nativeQuery = true)
	String getPath(@Param("type")String type,@Param("weekNum")Integer weekNum ,@Param("year")Integer year, @Param("customerId") Integer customerId);
	
	@Query(value = "UPDATE OTHER_EXPENSE_BANK_REQUEST SET REQUEST_ID =:requestId WHERE WORKFLOW_STAGE =:workFlowStage AND CUSTOMER_ID= :customerId;",nativeQuery = true)
	Integer addRequestIdFromWorkFlowStage(@Param("requestId")String requestId,@Param("workFlowStage")String workFlowStage, @Param("customerId") Integer customerId);

	@Query(value="SELECT * FROM OTHER_EXPENSE_BANK_REQUEST WHERE WORKFLOW_STAGE =:stage AND REQUEST_ID IS NULL AND CUSTOMER_ID= :customerId",nativeQuery=true)
	List<String> getEmployeeListFromWorkFlowStage(@Param("stage")String stage, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM OTHER_EXPENSE_BANK_REQUEST WHERE WORKFLOW_STAGE=:workflowStage AND WEEK_NUM=:weekNum AND TYPE IN (:benefitsName) AND CUSTOMER_ID= :customerId",nativeQuery = true)
	List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByWorkflowStageAndWeekNum(@Param("workflowStage")String workflowStage ,@Param("weekNum")Integer weekNum,@Param("benefitsName")String[] benefitsName, @Param("customerId") Integer customerId);
	
	@Query(value="SELECT * FROM OTHER_EXPENSE_BANK_REQUEST WHERE WORKFLOW_STAGE=:workflowStage AND WEEK_NUM<=:weekNum AND TYPE = :entityName AND CUSTOMER_ID= :customerId",nativeQuery = true)
	List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByWorkflowStageAndWeekNumAndEntity(@Param("workflowStage")String workflowStage ,@Param("weekNum")Integer weekNum,@Param("entityName")String entityName, @Param("customerId") Integer customerId);
	

}
