package com.nouros.hrms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The EmployeeRepository interface is a extension of {@link GenericRepository}.
 * It's purpose is to provide additional methods that are specific to the
 * {@link Employee} entity. see GenericRepository see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface EmployeeRepository extends GenericRepository<Employee> {

	@Query("SELECT e FROM Employee e WHERE e.workEmailAddress = :emailId")
	Employee findByEmailId(@Param("emailId") String emailId);

	@Query("SELECT e FROM Employee e WHERE e.id = :id")
	Employee fetchById(@Param("id") Integer id);

	@Query("SELECT e.dateOfExit FROM Employee e WHERE e.id = :id")
	Date fetchDateOfExit(@Param("id") Integer id);

	@Query("SELECT e FROM Employee e WHERE e.reportingManager.id = :id")
	List<Employee> findByReportingManagerIdDownLevel(@Param("id") int id);

	@Query("SELECT e FROM Employee e WHERE e.id = :id")
	List<Employee> findByReportingManagerIdUpLevel(@Param("id") int id);

	@Query("SELECT e FROM Employee e WHERE e.userId = :userId")
	Employee findByUserId(@Param("userId") Integer userId);

	Employee findByProcessInstanceId(String processInstanceId);

	@Query("SELECT count(*) FROM Employee e WHERE e.department.id = :departmentId And e.employmentType = 'Contractors'")
	Integer getCountOfEmployeeByDepartmentIdAndContractorsType(@Param("departmentId") Integer departmentId);

	@Query("SELECT count(*) FROM Employee e WHERE e.department.id = :departmentId And e.employmentType = 'Direct Hire'")
	Integer getCountOfEmployeeByDepartmentIdAndDirectHireType(@Param("departmentId") Integer departmentId);

	@Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName and e.customerId= :customerId")
	Employee fetchByDepartmentName(@Param("departmentName") String departmentName, @Param("customerId") Integer customerId);

	@Query("SELECT e FROM Employee e WHERE e.fullName = :employeeFullName")
	Employee findByFullName(@Param("employeeFullName") String employeeFullName);

	@Query("SELECT e FROM Employee e where e.reportingManager.id=:rmId and e.department.division.id=:divisionId")
	List<Employee> findByRMAndDivision(@Param("rmId") Integer rmId, @Param("divisionId") Integer divisionId);

	@Query("SELECT e FROM Employee e where e.department.id = :departmentId")
	List<Employee> findByDepartmentId(@Param("departmentId") Integer departmentId);

	@Query(value = "SELECT * FROM EMPLOYEE WHERE MONTH(DATE_OF_BIRTH) = MONTH(CURDATE()) AND CUSTOMER_ID = :customerId AND DAY(DATE_OF_BIRTH) = DAY(CURDATE())", nativeQuery = true)
	List<Employee> findEmployeeByMatchOfDateAndMonthWithDob(@Param("customerId") Integer customerId);

	@Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId and e.userId= :departmentLead")
	List<Employee> fetchDeptHeadByDeptIdAndLeadID(@Param("departmentId") Integer departmentId,
			@Param("departmentLead") Integer departmentLead);

	@Query(value = "SELECT *  FROM EMPLOYEE  WHERE DATE_OF_JOINING <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)    AND DELETED = 0 AND CUSTOMER_ID = :customerId ", nativeQuery = true)
	List<Employee> findEmployeesWhoCompletedThreeMonths(@Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM EMPLOYEE WHERE USERID_PK =:creator AND CUSTOMER_ID = :customerId ", nativeQuery = true)
	Optional<Employee> findByCreator(@Param("creator") Integer creator, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM EMPLOYEE WHERE FULL_NAME = :reportingManagerName AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Employee findEmployeeByFullName(@Param("reportingManagerName") String reportingManagerName, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID =:employeeId AND CUSTOMER_ID = :customerId ", nativeQuery = true)
	Employee findByEmployeeId(@Param("employeeId") String employeeId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM EMPLOYEE WHERE DESIGNATION_ID =:id AND CUSTOMER_ID = :customerId ", nativeQuery = true)
	List<Employee> findByDesignationId(@Param("id") Integer id,@Param("customerId") Integer customerId);

	@Query("SELECT e FROM Employee e WHERE e.designation.name = :name")
	List<Employee> findByDesignationName(@Param("name") String name);
	
	@Query(value ="SELECT * FROM EMPLOYEE WHERE EMPLOYMENT_TYPE IN (:list) AND CUSTOMER_ID = :customerId",nativeQuery = true)
	List<Employee> findByEmploymentTypeList(@Param("list") String[] list,@Param("customerId") Integer customerId);
	
	@Query("SELECT DISTINCT(e.text1) from Employee e WHERE e.text1 IS NOT NULL ")
	List<String> getAllDepartmentCode();
	
	@Query("SELECT e FROM Employee e INNER JOIN EmployeeMonthlySalary ems ON e.id =ems.employee.id WHERE e.text1 =:text1 AND ems.payrollRun.id =:payrollRunId AND e.employmentType NOT IN (:type) ")
	List<Employee> getEmployeeByDepartmentCode(@Param("text1")String text1,@Param("payrollRunId")Integer payrollRunId,@Param("type")String[] type);

	@Query(value = "DELETE FROM EMPLOYEE_TO_SKILL_MAPPING WHERE EMPLOYEE_ID_FK= :employeeId and CUSTOMER_ID = :customerId ", nativeQuery = true)
	void deleteEmployeeSkillMapping(@Param("employeeId") Integer employeeId,@Param("customerId") Integer customerId);

	@Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName and e.workEmailAddress != :hiringManagerWorkEmailAddress and e.designation.jobLevel = :jobLevel")
	List<Employee> findAlternateEmployeesForInterview(@Param("departmentName")String departmentName , @Param("hiringManagerWorkEmailAddress") String hiringManagerWorkEmailAddress,@Param("jobLevel") String jobLevel);

	@Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName and e.workEmailAddress not in (:hiringManagerWorkEmailAddress) ")
	List<Employee> findAlternateEmployeesForInterviewWithoutJobLevel(@Param("departmentName")String departmentName , @Param("hiringManagerWorkEmailAddress") String hiringManagerWorkEmailAddress);
	
}
