package com.nouros.hrms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.repository.generic.GenericRepository;


@Repository
@Transactional(readOnly = true)
public interface EmployeeChildrenRepository extends GenericRepository<EmployeeChildren> {
	


	@Query("SELECT ec FROM EmployeeChildren ec WHERE ec.employee.id = :employeeId")
    List<EmployeeChildren> findByEmployeeId(@Param("employeeId") Integer employeeId);

    
    @Query(value = "SELECT SUM(ECB.AMOUNT) FROM EMPLOYEE_CHILDREN ECB " +
                   "JOIN CHILD_EDUCATION_BENEFIT CEB ON ECB.CHILD_EDUCATION_BENEFIT_ID = CEB.ID " +
                   "WHERE ECB.EMPLOYEE_ID = :employeeId " +
                   "AND ECB.NAME = :childName " +
                   "AND CEB.SCHOOL_YEAR BETWEEN :startOfYearCycle AND :endOfYearCycle " +
                   "AND ECB.WORKFLOW_STAGE = 'Approved' AND ECB.CUSTOMER_ID = :customerId AND CEB.CUSTOMER_ID = :customerId", nativeQuery = true)
    Double getTotalAmountForChildInYearCycle(@Param("employeeId") Integer employeeId,
                                              @Param("childName") String childName,
                                              @Param("startOfYearCycle") LocalDate startOfYearCycle,
                                              @Param("endOfYearCycle") LocalDate endOfYearCycle,
                                              @Param("customerId") Integer customerId);

    @Query(value = "SELECT * FROM EMPLOYEE_CHILDREN WHERE HR_BENEFITS_ID = :id AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<EmployeeChildren> findByHrBenefitsId(@Param("id") Integer id, @Param("customerId") Integer customerId);

}
