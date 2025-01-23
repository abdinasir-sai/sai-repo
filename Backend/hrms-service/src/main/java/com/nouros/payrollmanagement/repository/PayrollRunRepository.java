package com.nouros.payrollmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.wrapper.PayrollRunDto;
import com.nouros.payrollmanagement.model.PayrollRun;
/**

The PayrollRunRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link PayrollRun} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional
public interface PayrollRunRepository extends GenericRepository<PayrollRun> {

	    @Query("SELECT pr FROM PayrollRun pr WHERE pr.processInstanceId = :processInstanceId AND pr.deleted = false  ")
	    Optional<PayrollRun> findPayrollRunByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
	    
	    @Query("delete from PayrollRun WHERE id in (:ids) ")
	    void deletePayrollRunById(@Param("ids") List<Integer> ids);
	    
	    @Query("Select pr from PayrollRun pr WHERE MONTH(pr.endDate) =:month AND YEAR(pr.endDate)=:year")
	    List<PayrollRun> getPayrollRunByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
	    
	    
	    @Query("SELECT new com.nouros.hrms.wrapper.PayrollRunDto(pr.ID ,CAST(FUNCTION('DATE_FORMAT', pr.endDate, '%Y') as string),CAST( FUNCTION('DATE_FORMAT', pr.endDate, '%m')as string), CASE WHEN (pr.id = (SELECT MAX(pr2.id) FROM PayrollRun pr2 where pr2.deleted=false ) OR pr.workflowStage = 'REJECTED') THEN true ELSE false END) FROM PayrollRun pr Where pr.deleted = false  AND pr.endDate  IS NOT NULL  Order by FUNCTION('DATE_FORMAT', pr.endDate, '%Y') , FUNCTION('DATE_FORMAT', pr.endDate, '%m') ")
	     List<PayrollRunDto> findEndMonthYearWithFlag();
	    
	    
}
