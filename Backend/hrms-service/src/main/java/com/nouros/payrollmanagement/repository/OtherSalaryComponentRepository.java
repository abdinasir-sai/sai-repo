package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;



@Repository
@Transactional(readOnly = true)
public interface OtherSalaryComponentRepository extends GenericRepository<OtherSalaryComponent>{

    @Query("SELECT e FROM OtherSalaryComponent e WHERE e.employee.id = :employeeId AND (e.workflowStage = 'APPROVED' OR e.workflowStage = 'PENDING'  ) and e.type = :type  and e.deleted = :deleted")
    List<OtherSalaryComponent> findByEmployeeIdAndTypeAndDeleted(@Param("employeeId") int employeeId,@Param("type") String type , @Param("deleted") Boolean deleted);
}
