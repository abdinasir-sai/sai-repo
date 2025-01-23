package com.nouros.hrms.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.User;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.EmployeeCompleteDetailsDto;
import com.nouros.hrms.wrapper.EmployeeDataWrapper;
import com.nouros.hrms.wrapper.EmployeeDetailsDto;
import com.nouros.hrms.wrapper.EmployeeOrgChartDto;
import com.nouros.hrms.wrapper.EmployeeWrapper;
import com.nouros.hrms.wrapper.OrgnisationDto;
import com.nouros.hrms.wrapper.ReportingHeirarchyWrapper;

import jakarta.validation.Valid;

/**
 * 
 * EmployeeService interface is a service layer interface which handles all the
 * business logic related to Employee model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Employee
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface EmployeeService extends GenericService<Integer,Employee> {

	/**
	 * 
	 * This method is used to soft delete an Employee identified by id.
	 * 
	 * @param id The id of the Employee to be soft deleted.
	 */
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public Employee createWithNaming(Employee employee);

	/**
	 * @param employeeWrapper containg Id and emailId of Employee
	 * @return List of ReportingHeirarchyWrapper based on id, emailId
	 */
	List<ReportingHeirarchyWrapper> getAllParentChildEmployeeObjects(EmployeeWrapper employeeWrapper);

	Employee findByProcessInstanceId(String processInstanceId);

	String execute(Integer id);

	Map<String,Integer> getCountOfEmployeesByEmploymentType(Integer departmentId);

	void setReportingManagerUserId(Employee employee);

	Employee getEmployeeByUserId(Integer empId);

	OrgnisationDto getOrgChart(String departmentName);

	EmployeeOrgChartDto getEmployeeOrgChart(String employeeName, Integer employeeUserId);

	List<Employee> findAll();

	Map<String,Integer> getCountOfEmployeesByEmploymentType(List<Integer> departmentIds);

	EmployeeDetailsDto getSelfEmployeeDetails(@Valid Integer id, @Valid Integer userId);

	EmployeeDetailsDto updateSelfEmployeeDetails(@Valid EmployeeDetailsDto employeeDetailsDto);

	List<EmployeeCompleteDetailsDto> getEmployeeDetailsForAdmin(Integer id, String fullName);

	List<EmployeeCompleteDetailsDto> updateEmployeeDetailsForAdmin(
			EmployeeCompleteDetailsDto employeeCompleteDetailsDto);

	Employee getEmployeeByIdOrUserId(Integer id, Integer userId);

	List<EmployeeDataWrapper> findEmployeeByDateAndMonthWhenMatchDob();

	void sendBirthDayGreeting();

	List<Employee> findEmployeesWhoCompletedThreeMonths();

	Employee getEmployeeByCreator(Integer creator);

	String importEmployeeData(MultipartFile excelFile);

	Employee findEmployeeByFullName(String name);

	Employee findByEmployeeId(String employeeId);
	 List<Employee> getEmployeeByEmploymentTypeList(String[] list);
	 
	 String deleteEmployeeCorrespondingData(Integer id);
	 
	 List<User> getUserFromName( List<String> userName);

	 String getDesignationLevelByUserId(Integer userId);

	  List<String> getDepartmentCodeForEmployees();
		 
	
		 public List<Employee> getEmployeesByDepartmentCode(String departmentCode,Integer payrollRunId,String[] typeList);

		 String addEmployeeInMilvus();
}
