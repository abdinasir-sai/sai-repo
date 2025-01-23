package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.nouros.hrms.model.IntegrationLog;


@Repository
public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, Integer> {



	@Query(value="SELECT MAX(BATCH_NAME) FROM INTEGRATION_LOG WHERE CUSTOMER_ID =:customerId" ,nativeQuery=true)
	public Integer getBatchName(@Param("customerId") Integer customerId);
	
    @Query(value="UPDATE INTEGRATION_LOG SET FINAL_STATUS=:finalStatus WHERE PROCESS_ID=:processId AND REQUEST_ID=:requestId AND CUSTOMER_ID=:customerId",nativeQuery = true)
    public Integer updateStatus(@Param("finalStatus")String finalStatus,@Param("processId")Double processId,@Param("requestId")String requestId, @Param("customerId") Integer customerId);
    
    @Query(value=" SELECT PAYROLL_ID FROM INTEGRATION_LOG WHERE PROCESS_ID=:processId AND REQUEST_ID=:requestId  AND CUSTOMER_ID=:customerId",nativeQuery = true)
    public Integer getPayRollId(@Param("processId")Double processId,@Param("requestId")String requestId, @Param("customerId") Integer customerId);

}
