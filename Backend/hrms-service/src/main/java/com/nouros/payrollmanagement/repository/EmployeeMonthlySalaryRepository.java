package com.nouros.payrollmanagement.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;

/**

The EmployeeMonthlySalaryRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link EmployeeMonthlySalary} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional
public interface EmployeeMonthlySalaryRepository extends GenericRepository<EmployeeMonthlySalary>{


	
	//@Query(value=" SELECT rd.NET_AMOUNT FROM EMPLOYEE_MONTHLY_SALARY rd JOIN  PAYROLL_RUN pr ON rd.PAYROLL_RUN_FK = pr.ID WHERE rd.EMPLOYEE_PK = :employeeId  AND pr.RUN_DATE = (    SELECT MAX(pr2.RUN_DATE)    FROM EMPLOYEE_MONTHLY_SALARY  rd2    JOIN PAYROLL_RUN pr2 ON rd2.PAYROLL_RUN_FK = pr2.ID    WHERE rd2.EMPLOYEE_PK = rd.EMPLOYEE_PK  )ORDER BY pr.RUN_DATE DESC LIMIT 1 ",nativeQuery = true)
	
	
	@Query(value="SELECT rd.NET_AMOUNT FROM EMPLOYEE_MONTHLY_SALARY rd JOIN  PAYROLL_RUN pr1 on pr1.ID = rd.PAYROLL_RUN_FK   WHERE pr1.WORKFLOW_STAGE IN ('PAYROLL_BANK_APPROVAL','BOARD_MEMBER_BANK_APPROVAL','COMPLETED') AND   rd.EMPLOYEE_PK = :employeeId AND  rd.DELETED = 0 AND    pr1.DELETED =0  AND rd.ID = ( SELECT MAX(rd2.ID)    FROM EMPLOYEE_MONTHLY_SALARY  rd2  JOIN PAYROLL_RUN pr2 on pr2.ID = rd2.PAYROLL_RUN_FK  WHERE pr2.WORKFLOW_STAGE IN ('PAYROLL_BANK_APPROVAL','BOARD_MEMBER_BANK_APPROVAL','COMPLETED') AND rd2.EMPLOYEE_PK = :employeeId and rd2.DELETED = 0 AND  pr2.DELETED =0 AND rd2.CUSTOMER_ID=:customerId  AND pr2.CUSTOMER_ID = :customerId )AND rd.CUSTOMER_ID=:customerId  AND pr1.CUSTOMER_ID = :customerId",nativeQuery = true)
	BigDecimal getRecentNetForEmployee(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId);
	
	
	
	@Query(value="SELECT COALESCE(SUM(rd.NET_AMOUNT),0.0) , COALESCE(SUM(rd.LAST_MONTH_NET_AMOUNT),0.0), COALESCE(SUM(rd.STI),0.0) , COALESCE(SUM(rd.SIGN_UP_BONUS),0.0) ,COALESCE(SUM(rd.OTHER_EARNING),0.0) , COALESCE(SUM(rd.TOTAL_DEDUCTION_AMOUNT),0.0) ,COALESCE(SUM(rd.RELOCATION_ALLOWANCE),0.0) ,COALESCE(SUM(rd.BASIC_SALARY),0.0), COALESCE(SUM(rd.HRA),0.0), COALESCE(SUM(rd.TA),0.0), COALESCE(SUM(rd.MOBILE_ALLOWANCE),0.0),COALESCE(SUM(rd.OVERTIME),0.0),COALESCE(SUM(rd.OVERBASE),0.0),COALESCE(SUM(rd.CRITICAL_SKILLS),0.0) FROM EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e on rd.EMPLOYEE_PK = e.ID  JOIN PAYROLL_RUN pr on pr.ID = rd.PAYROLL_RUN_FK  WHERE e.EMPLOYMENT_TYPE NOT IN (:list) AND rd.PAYROLL_RUN_FK =:payrollRunId AND rd.DELETED = 0 AND pr.DELETED = 0 AND rd.CUSTOMER_ID=:customerId AND pr.CUSTOMER_ID = :customerId",nativeQuery = true)
	List<Object[]> getSumSalaryOfPayroll(@Param("payrollRunId") Integer payrollRunId,@Param("list")String[] list, @Param("customerId") Integer customerId);

	@Query(value="SELECT COALESCE(SUM(rd.BASIC_SALARY),0.0) , COALESCE(SUM(rd.OVERBASE),0.0), COALESCE(SUM(rd.CRITICAL_SKILLS),0.0) , COALESCE(SUM(rd.HRA),0.0) ,COALESCE(SUM(rd.TA),0.0) , COALESCE(SUM(rd.MOBILE_ALLOWANCE),0.0) ,COALESCE(SUM(rd.STI),0.0) ,COALESCE(SUM(rd.SIGN_UP_BONUS),0.0), COALESCE(SUM(rd.OVERTIME),0.0), COALESCE(SUM(rd.RELOCATION_ALLOWANCE),0.0), COALESCE(SUM(rd.GOSI_EMPLOYEE),0.0), COALESCE(SUM(rd.GOSI_EMPLOYER),0.0), COALESCE(SUM(rd.OTHER_EARNING),0.0), COALESCE(SUM(rd.OTHER_DEDUCTION),0.0), COALESCE(SUM(rd.TOTAL_EARNING_AMOUNT),0.0), COALESCE(SUM(rd.TOTAL_DEDUCTION_AMOUNT),0.0), COALESCE(SUM(rd.NET_AMOUNT),0.0), COALESCE(SUM(rd.VARIANCE_AMOUNT),0.0) FROM EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e on rd.EMPLOYEE_PK = e.ID  JOIN PAYROLL_RUN pr on pr.ID = rd.PAYROLL_RUN_FK  WHERE e.EMPLOYMENT_TYPE NOT IN (:list) AND rd.PAYROLL_RUN_FK =:payrollRunId AND rd.DELETED = 0 AND pr.DELETED = 0  AND rd.CUSTOMER_ID=:customerId AND pr.CUSTOMER_ID = :customerId",nativeQuery = true)
	List<Object[]> getSumOfEmployeeMonthlySalaryComponents(@Param("payrollRunId") Integer payrollRunId,@Param("list")String[] list, @Param("customerId") Integer customerId);

	
	@Query("SELECT rd FROM EmployeeMonthlySalary rd WHERE rd.payrollRun.id = :payrollId and rd.payrollRun.deleted = false and rd.deleted = false ")
    List<EmployeeMonthlySalary> employeeMonthlySalaryByPayrollId(@Param("payrollId") Integer payrollId);
	
	@Query("SELECT rd FROM EmployeeMonthlySalary rd WHERE rd.payrollRun.id = :payrollId and rd.payrollRun.deleted = false and rd.deleted = false and rd.employee.employmentType NOT IN (:list)")
    List<EmployeeMonthlySalary> employeeMonthlySalaryByPayrollIdForWps(@Param("payrollId") Integer payrollId,@Param("list")String[] list);

	
	@Query(value = " SELECT rd FROM EmployeeMonthlySalary rd WHERE rd.employee.id = :employeeId and (rd.relocationAllowance > 0 and rd.relocationAllowance IS NOT null) and rd.deleted = false ")
	  List<EmployeeMonthlySalary> checkEmployeeMonthlySalaryforRellocationAllowance(@Param("employeeId") Integer employeeId);
	 
	 
	 @Query(value = " SELECT rd FROM EmployeeMonthlySalary rd WHERE rd.employee.id = :employeeId and (rd.signUpBonus > 0 and rd.signUpBonus IS NOT null)  and rd.deleted = false  ")
	  List<EmployeeMonthlySalary> checkEmployeeMonthlySalaryforSignUpBonus(@Param("employeeId") Integer employeeId);

	// @Query(value =" SELECT rd.ID , pd.END_DATE FROM EMPLOYEE_MONTHLY_SALARY rd  JOIN EMPLOYEE e on e.ID = rd.EMPLOYEE_PK JOIN  PAYROLL_RUN pd  on pd.ID = rd.PAYROLL_RUN_FK  WHERE e.ID = :employeeId AND pd.WORKFLOW_STAGE ='PAYROLL_BANK_APPROVAL'  AND pd.DELETED =0   ORDER BY   rd.ID ",nativeQuery = true)
	 
//	 @Query("SELECT rd from EmployeeMonthlySalary rd where rd.employee.id =:employeeId and rd.payrollRun.workflowStage = 'PAYROLL_BANK_APPROVAL' and rd.payrollRun.deleted = false order by rd.id ")
//	 List<EmployeeMonthlySalary> getPayHistoryRecord(@Param("employeeId") Integer employeeId);

	 @Query(value ="SELECT rd.ID FROM EMPLOYEE_MONTHLY_SALARY rd  INNER JOIN PAYROLL_RUN pr on pr.ID = rd.PAYROLL_RUN_FK  WHERE  pr.WORKFLOW_STAGE IN ('PAYROLL_BANK_APPROVAL','BOARD_MEMBER_BANK_APPROVAL','COMPLETED') AND   rd.EMPLOYEE_PK = :employeeId AND pr.DELETED = 0    and rd.ID  = (SELECT MAX(rd2.ID)    FROM   EMPLOYEE_MONTHLY_SALARY rd2 INNER JOIN PAYROLL_RUN pr2 on pr2.ID = rd2.PAYROLL_RUN_FK  WHERE pr2.WORKFLOW_STAGE IN ('PAYROLL_BANK_APPROVAL', 'BOARD_MEMBER_BANK_APPROVAL','COMPLETED')   AND  pr2.DELETED = 0 AND   rd2.EMPLOYEE_PK =  :employeeId  AND rd2.ID !=    :employeeMonthlySalaryId  AND  rd2.ID <   :employeeMonthlySalaryId AND rd2.CUSTOMER_ID=:customerId AND pr2.CUSTOMER_ID = :customerId) AND rd.CUSTOMER_ID=:customerId AND pr.CUSTOMER_ID = :customerId" ,nativeQuery = true)
	 List<Long> getRecentEmployeeMonthlySalary(@Param("employeeId") Integer employeeId,	@Param("employeeMonthlySalaryId") Integer employeeMonthlySalaryId, @Param("customerId") Integer customerId);

	 @Query(value =  "SELECT reason, COUNT(reason) AS reason_count  FROM ( SELECT TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(rd.VARIANCE_REASON, ',', numbers.n), ',', -1)) AS reason     FROM          EMPLOYEE_MONTHLY_SALARY rd  JOIN (SELECT 1 n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) numbers     ON          CHAR_LENGTH(rd.VARIANCE_REASON) - CHAR_LENGTH(REPLACE(rd.VARIANCE_REASON, ',', '')) >= numbers.n -1  JOIN PAYROLL_RUN pr on pr.ID =  rd.PAYROLL_RUN_FK   WHERE          rd.VARIANCE_REASON IS NOT NULL AND pr.DELETED = 0 AND  rd.PAYROLL_RUN_FK = :payrollId AND rd.CUSTOMER_ID = :customerId AND pr.CUSTOMER_ID = :customerId) AS AllReasons GROUP BY reason ORDER BY reason_count DESC LIMIT 3", nativeQuery = true)
   	List<Object[]> getTop3ReasonsByPayrollId(@Param("payrollId") Integer payrollId, @Param("customerId") Integer customerId);
   	
   	@Query(value = "select e.TEXT1, SUM(rd.OVERBASE),SUM(rd.CRITICAL_SKILLS),SUM(rd.STI),SUM(rd.MOBILE_ALLOWANCE),SUM(rd.OVERTIME),SUM(rd.GOSI_EMPLOYER),SUM(rd.GOSI_EMPLOYEE),SUM(rd.SIGN_UP_BONUS),SUM(rd.RELOCATION_ALLOWANCE),SUM(rd.OTHER_EARNING),SUM(rd.OTHER_DEDUCTION) from EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e ON rd.EMPLOYEE_PK = e.ID where rd.PAYROLL_RUN_FK=:payrollId AND e.TEXT1 IS NOT NULL AND e.EMPLOYMENT_TYPE NOT IN (:list) AND rd.CUSTOMER_ID=:customerId AND e.CUSTOMER_ID=:customerId GROUP BY e.TEXT1",nativeQuery = true)
   	List<Object[]> getSumOfValue(@Param("payrollId") Integer payrollId,@Param("list")String[] list, @Param("customerId") Integer customerId);

   	@Query(value = "select SUM(rd.OVERBASE),SUM(rd.CRITICAL_SKILLS),SUM(rd.STI),SUM(rd.MOBILE_ALLOWANCE),SUM(rd.OVERTIME),SUM(rd.GOSI_EMPLOYER),SUM(rd.GOSI_EMPLOYEE),SUM(rd.SIGN_UP_BONUS),SUM(rd.RELOCATION_ALLOWANCE),SUM(rd.OTHER_EARNING),SUM(rd.OTHER_DEDUCTION) from EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e on rd.EMPLOYEE_PK = e.ID  where rd.PAYROLL_RUN_FK=:payrollId  AND e.EMPLOYMENT_TYPE NOT IN (:list) AND e.TEXT1 IS NOT NULL AND rd.CUSTOMER_ID=:customerId AND e.CUSTOMER_ID=:customerId" ,nativeQuery = true)
   	List<Object[]> getSumOfValueForAccural(@Param("payrollId")Integer payrollId,@Param("list")String[] list, @Param("customerId") Integer customerId);
   		 
   	@Query("SELECT rd FROM EmployeeMonthlySalary rd WHERE rd.payrollRun.id = :payrollId and rd.payrollRun.deleted = false and rd.deleted = false AND rd.employee.employmentType =:type")
    List<EmployeeMonthlySalary> employeeMonthlySalaryByPayrollIdForOnBoarded(@Param("payrollId") Integer payrollId,@Param("type") String type);

	@Query(value = "INSERT INTO FILE_DATA(FILE_PATH,RECORD_DATE,MONTH,YEAR,TYPE,CUSTOMER_ID) VALUES(:path,:date,:month,:year,:type,:customerId) ",nativeQuery = true)
   	void saveData(@Param("path")String path,@Param("date")LocalDate date,@Param("month")Integer month ,@Param("year")Integer year,@Param("type")String type, @Param("customerId") Integer customerId);
   	
   	@Query(value="SELECT FILE_PATH FROM FILE_DATA WHERE MONTH=:month AND YEAR=:year AND CUSTOMER_ID=:customerId" ,nativeQuery = true)
	String getFilePath(@Param("month")Integer month,@Param("year")Integer year, @Param("customerId") Integer customerId);
   	
   	
   	@Query(value="select e.TEXT1,SUM(NET_AMOUNT) from EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e on rd.EMPLOYEE_PK = e.ID  where rd.PAYROLL_RUN_FK=:payrollId AND e.EMPLOYMENT_TYPE =:employeeType  AND e.TEXT1 IS NOT NULL GROUP BY e.TEXT1 AND rd.CUSTOMER_ID=:customerId AND e.CUSTOMER_ID=:customerId",nativeQuery = true)
   	List<Object[]> getExpenseForBOD(@Param("payrollId") Integer payrollId,@Param("employeeType")String employeeType, @Param("customerId") Integer customerId);
   	
   	@Query(value="select SUM(NET_AMOUNT)  from EMPLOYEE_MONTHLY_SALARY rd INNER JOIN EMPLOYEE e on rd.EMPLOYEE_PK = e.ID  where rd.PAYROLL_RUN_FK=:payrollId AND e.EMPLOYMENT_TYPE =:employeeType  AND e.TEXT1 IS NOT NULL AND rd.CUSTOMER_ID=:customerId AND e.CUSTOMER_ID=:customerId ",nativeQuery = true)
   	 Object[] getAccuralForBOD(@Param("payrollId") Integer payrollId,@Param("employeeType")String employeeType, @Param("customerId") Integer customerId);

     EmployeeMonthlySalary findByEmployeeIdAndPayrollRunIdAndCustomerId(@Param("employeeId")Integer employeeId ,@Param("payrollRunId")Integer payrollRunId,@Param("customerId")Integer customerId);

}
