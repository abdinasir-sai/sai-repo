package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.platform.customannotation.annotation.GenericAnnotation;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeController;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.User;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.wrapper.EmployeeCompleteDetailsDto;
import com.nouros.hrms.wrapper.EmployeeDataWrapper;
import com.nouros.hrms.wrapper.EmployeeDetailsDto;
import com.nouros.hrms.wrapper.EmployeeOrgChartDto;
import com.nouros.hrms.wrapper.EmployeeWrapper;
import com.nouros.hrms.wrapper.OrgnisationDto;
import com.nouros.hrms.wrapper.ReportingHeirarchyWrapper;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the EmployeeController interface.
 * It is annotated with the Spring annotations @RestController, @RequestMapping
 * and @Primary to indicate that it is a Spring-managed bean and should be used
 * as the primary implementation of the EmployeeController. It is also annotated
 * with @Api to provide metadata for the Swagger documentation. The class also
 * uses Lombok's @Slf4j annotation to automatically generate a logger field
 * named "log" that is used to log method calls and results. The class fields
 * include an instance of the EmployeeService bean, which is injected by Spring
 * using the @Autowired annotation. The class implements the following methods:
 * create(Employee Employee): creates an Employee and returns the newly created
 * Employee. count(String filter): returns the number of Employee that match the
 * specified filter. search(String filter, Integer offset, Integer size, String
 * orderBy, String orderType): returns a list of Employee that match the
 * specified filter, sorted according to the specified orderBy and orderType.
 * update(Employee Employee): updates an Employee and returns the updated
 * Employee.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of Employee with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/Employee")
//@Tag(name="/Employee",tags="Employee",description="Employee")
public class EmployeeControllerImpl implements EmployeeController {

	private static final Logger log = LogManager.getLogger(EmployeeControllerImpl.class);

	@Autowired
	private EmployeeService employeeService;

	@TriggerBPMN(entityName = "Employee", appName = "HRMS_APP_NAME")
	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE WITH NAMING")
	@GenericAnnotation(actionType = "CREATE", uniqEntityId = "id", annotationName = {
			"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "Employee", globalSearchData = "firstName, lastName, workEmailAddress , givenName, arabicFirstName, arabicLastName , arabicMiddleName ", searchTitle = "fullName")
	public Employee create(Employee employee) {
		return employeeService.createWithNaming(employee);
	}

	@Override
	public Long count(String filter) {
		return employeeService.count(filter);
	}

	@Override
	public List<ReportingHeirarchyWrapper> getAllParentChildEmployeeObjects(EmployeeWrapper employeeWrapper) {
		return employeeService.getAllParentChildEmployeeObjects(employeeWrapper);
	}

	@Override
	public List<Employee> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return employeeService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE")
	@GenericAnnotation(actionType = "UPDATE", uniqEntityId = "id", annotationName = {
	"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "Employee", globalSearchData = "firstName, lastName, workEmailAddress , givenName, arabicFirstName, arabicLastName , arabicMiddleName ", searchTitle = "fullName")
	public Employee update(Employee employee) {
		return employeeService.update(employee);
	}

	@Override
	public Employee findById(Integer id) {
		return employeeService.findById(id);
	}

	@Override
	public Employee findEmployeeById(Integer id) {
		log.info("going to get Employee by Id");
		Employee optionalEmployee = employeeService.findById(id);
		if (optionalEmployee != null) {
	        return optionalEmployee;
	    } else {
	        log.info("Employee with Id {} not found", id);
	        throw new NoSuchElementException("Employee not found with Id: " + id);
	    }
	}


	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE BY ID")
	public void deleteById(Integer id) {
		employeeService.softDelete(id);
	}


	@Override
	public  OrgnisationDto getOrgChart(String departmentName) {
		return employeeService.getOrgChart(departmentName);
	}

	@Override
	public List<WorkflowActions> getActions(@Valid Integer id) {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "Employee");
	}

	@Override
	public String execute(Integer id) {
		return employeeService.execute(id);
	}
	
	@Override
	public  EmployeeOrgChartDto getEmployeeOrgChart(String employeeName,Integer employeeUserId) {
		return employeeService.getEmployeeOrgChart(employeeName,employeeUserId);
	}

	@Override
	public EmployeeDetailsDto getSelfEmployeeDetails(@Valid Integer id, @Valid Integer userId) {
		return employeeService.getSelfEmployeeDetails(id,userId);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE SELF EMPLOYEE DETAILS")
	public EmployeeDetailsDto updateSelfEmployeeDetails( EmployeeDetailsDto employeeDetailsDto) {
		return employeeService.updateSelfEmployeeDetails(employeeDetailsDto);
	}

	@Override
	public List<EmployeeCompleteDetailsDto> getEmployeeDetailsForAdmin(Integer id, String fullName) {
		return employeeService.getEmployeeDetailsForAdmin(id,fullName);
	}
	
	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE DETAILS FOR ADMIN")
	public List<EmployeeCompleteDetailsDto> updateEmployeeDetailsForAdmin(@Valid EmployeeCompleteDetailsDto employeeCompleteDetailsDto) {
		return employeeService.updateEmployeeDetailsForAdmin(employeeCompleteDetailsDto);
	}

	@Override
	public List<EmployeeDataWrapper> findEmployeeByDateAndMonthWhenMatchDob() {
		log.info("Inside @Class EmployeeControllerImpl @Method findEmployeeByDateAndMonthWhenMatchDob");
		return employeeService.findEmployeeByDateAndMonthWhenMatchDob();
	}
	
	@Override
	public void sendBirthDayGreeting() {
	   employeeService.sendBirthDayGreeting();
	}

	@Override
	public String importEmployeeData(MultipartFile excelFile) {
		return employeeService.importEmployeeData(excelFile);
	}

	@Override
	public String deleteEmployeeCorrespondingData(@Valid Integer id) {
		log.info("Inside @Class EmployeeControllerImpl @Method deleteEmployeeCorrespondingData");
		return employeeService.deleteEmployeeCorrespondingData(id);
	}

	@Override
	public List<User> getUserFromName( List<String> userName) {
		log.info("Inside @Class EmployeeControllerImpl @Method getUserFromName");
		return employeeService.getUserFromName(userName);
	}
	
      @Override	
      public  String addEmployeeToMilvus()
      {
    	  return employeeService.addEmployeeInMilvus();
      }

}
