package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ProductApiResponses;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.User;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.EmployeeCompleteDetailsDto;
import com.nouros.hrms.wrapper.EmployeeDataWrapper;
import com.nouros.hrms.wrapper.EmployeeDetailsDto;
import com.nouros.hrms.wrapper.EmployeeOrgChartDto;
import com.nouros.hrms.wrapper.EmployeeWrapper;
import com.nouros.hrms.wrapper.OrgnisationDto;
import com.nouros.hrms.wrapper.ReportingHeirarchyWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to Employee.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {Employee.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new Employee with the given request body and returns the created Employee.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of Employee with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of Employee based on the RSQL filter and returns the list of Employee matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the Employee by ID and returns the Employee if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing Employee with the given id with the request body and returns the updated Employee.
delete(@PathVariable("id") Long id) : deletes the Employee with the given id and returns the deleted Employee.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeController", url = "${hrms.url}", path = "/Employee", primary = false)
public interface EmployeeController {

  @Operation(summary = "Creates a new Employee",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  Employee create(@Valid @RequestBody Employee employee);
  
  @Operation(summary = "Fetches all Parent and Child Employee",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEE_FETCH"})})
  @PostMapping(path = "getAllParentChildEmployeeObjects")
  List<ReportingHeirarchyWrapper> getAllParentChildEmployeeObjects(@Valid @RequestBody EmployeeWrapper employeeWrapper);
  
  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of Employee with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<Employee> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given Employee",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  Employee update(@Valid @RequestBody Employee employee);

  @Operation(summary = "To delete the given Employee by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  

  @Operation(summary = "To get Employee by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  Employee findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get Employee Object by Id",  security = {
	      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	          "ROLE_API_EMPLOYEE_READ"})
	      })
	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "findEmployeeById")
	  Employee findEmployeeById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "getActions")
		  List<WorkflowActions> getActions(
		      @Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
  	@Operation(summary = "Get employee Organization Chart by department,division or Employee fullName",  security = {
			@SecurityRequirement(name = "default", scopes = {
					"ROLE_API_EMPLOYEE_DOCUMENT_READ"}) })
	@ProductApiResponses
	@GetMapping(value = "getOrgChart")
	OrgnisationDto getOrgChart(@RequestParam(required = false, name = "departmentName") String departmentName);
   	  
     
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "getActions/{id}")
   	  String execute(@PathVariable(name = "id") Integer id);

   	  
   	  @Operation(summary = "Get employee Organization Chart by department,division or Employee fullName",  security = {
			@SecurityRequirement(name = "default", scopes = {
					"ROLE_API_EMPLOYEE_DOCUMENT_READ"}) })
   	  @ProductApiResponses
		@GetMapping(value = "getEmployeeOrgChart")
   	  EmployeeOrgChartDto getEmployeeOrgChart(@RequestParam(required = false, name = "employeeFullName") String employeeFullName,
   			             @RequestParam(required = false, name = "employeeUserId") Integer employeeUserId);
     
   	  
   	  @Operation(summary = "To get Employee Related Details  by primary Id or userId",  security = {
   		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
   		          "ROLE_API_EMPLOYEE_READ"})
   		      })
   		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   		  @GetMapping(path = "getSelfEmployeeDetails")
   	  EmployeeDetailsDto getSelfEmployeeDetails(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id,
   				@Valid @RequestParam(name = "userId", required = false) Integer userId);
   
   	  @Operation(summary = "Update Self Employee Additional Details ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
      "ROLE_API_EMPLOYEE_WRITE"})})
    @PostMapping(path = "updateSelfEmployeeDetails")
   	EmployeeDetailsDto updateSelfEmployeeDetails(@RequestBody EmployeeDetailsDto employeeDetailsDto);
   	  
   	  @Operation(summary = "To get Employee Related Details by primary Id or full Name for Admin ",  security = {
   		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
   		          "ROLE_API_EMPLOYEE_READ"})
   		      })
   		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   		  @GetMapping(path = "getEmployeeDetailsForAdmin")
   	List<EmployeeCompleteDetailsDto> getEmployeeDetailsForAdmin(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id,
   				@Valid @RequestParam(name = "fullName", required = false) String fullName);
   
   	  @Operation(summary = "Update Self Employee Additional Details For Admin ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
      "ROLE_API_EMPLOYEE_WRITE"})})
    @PostMapping(path = "updateEmployeeDetailsForAdmin")
   	List<EmployeeCompleteDetailsDto> updateEmployeeDetailsForAdmin(@RequestBody EmployeeCompleteDetailsDto employeeCompleteDetailsDto);
   	  
   	@GetMapping("findEmployeeByDateAndMonthWhenMatchDob")
   	List<EmployeeDataWrapper> findEmployeeByDateAndMonthWhenMatchDob();

   	
	@Operation(summary = "Send birthday greeting to employee ",  security = { @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
      "ROLE_API_EMPLOYEE_WRITE"})})
	@GetMapping(path = "sendBirthDayGreeting")
	public void sendBirthDayGreeting();
	
	@Operation(summary = "Create, Update Employee from Sheet", security = { 
		    @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_EMPLOYEE_WRITE" }) 
		})
		@PostMapping(path = "importEmployeeData")
		public String importEmployeeData(@RequestParam(required = false, name = "file") MultipartFile excelFile);

	
	@Operation(summary = "To delete Employee and its corresponding data",  security = {
 		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
 		          "ROLE_API_EMPLOYEE_DELETE"})
 		      })
 		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
 		  @GetMapping(path = "deleteEmployeeCorrespondingData")
 	  String deleteEmployeeCorrespondingData(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
 
	@Operation(summary = "To get user from name",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_GET_USER"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @PostMapping(path = "getUserFromName")
	  List<User> getUserFromName(@RequestBody List<String> userName);

	@Operation(summary = "To insert Employee in milvus",  security = {
		      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
		          "ROLE_API_ADD_EMPLOYEE"})
		      })
		  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "addEmployeeToMilvus")
	  String addEmployeeToMilvus();

     
}
