package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;

@Repository
@Transactional(readOnly = true)
public interface OtherSalaryPayrollMappingRepository extends GenericRepository<OtherSalaryPayrollMapping>{

	  @Query("SELECT ospm.otherSalaryComponent FROM OtherSalaryPayrollMapping ospm WHERE ospm.payrollRunId =:payrollRunId and ospm.otherSalaryComponent.type =:type  AND  ospm.otherSalaryComponent.deleted =:deleted")
	  List<OtherSalaryComponent> findByPayrollRunIdAndOtherSalaryComponentTypeAndOtherSalaryComponentDeleted(@Param("payrollRunId") Integer payrollRunId,@Param("type") String type , @Param("deleted") Boolean deleted);
	  
	  @Query("SELECT ospm FROM OtherSalaryPayrollMapping ospm WHERE ospm.otherSalaryComponent.id =:othersalarycomponentId and ospm.otherSalaryComponent.deleted =:deleted")
	  List<OtherSalaryPayrollMapping> findMapping(@Param("othersalarycomponentId") Integer othersalarycomponentId , @Param("deleted") Boolean deleted);

}
