package com.nouros.payrollmanagement.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.wrapper.PayrollRequestWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeSalaryStructureController defines several endpoints
 * that can be accessed using the Spring Web's FeignClient to interact with the
 * service related to EmployeeSalaryStructure.
 * 
 * FeignClient annotation is used to create a client-side load balancer, in this
 * case to call the Employee service, and the url attribute is populated with
 * the {EmployeeSalaryStructure.url} property.
 * 
 * ApiOperation annotation describes the operation the endpoint performs and
 * details the response, authorizations and other information about the
 * endpoint.
 * 
 * It defines the following endpoints:
 * 
 * create(@Valid @RequestBody Employee employee) : creates a new
 * EmployeeSalaryStructure with the given request body and returns the created
 * EmployeeSalaryStructure. count(@RequestParam(name = APIConstants.QUERY,
 * required = false) String filter) : returns the count of
 * EmployeeSalaryStructure with the RSQL query in the filter parameter.
 * search(@RequestParam(name = APIConstants.QUERY, required = false) String
 * filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true)
 * Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required =
 * true) Integer size, @RequestParam(name = APIConstants.SORT, required = false)
 * String sort): searches for the list of EmployeeSalaryStructure based on the
 * RSQL filter and returns the list of EmployeeSalaryStructure matching the
 * criteria with the pagination and sorting information.
 * findById(@PathVariable("id") Long id) : searches for the
 * EmployeeSalaryStructure by ID and returns the EmployeeSalaryStructure if
 * found. update(@PathVariable("id") Long id, @Valid @RequestBody Employee
 * employee) : updates the existing EmployeeSalaryStructure with the given id
 * with the request body and returns the updated EmployeeSalaryStructure.
 * delete(@PathVariable("id") Long id) : deletes the EmployeeSalaryStructure
 * with the given id and returns the deleted EmployeeSalaryStructure. It also
 * includes some other annotations
 * like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are
 * used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 */
@ResponseBody
@FeignClient(name = "EmployeeSalaryStructureController", url = "${hrms.url}", path = "/EmployeeSalaryStructure", primary = false)
public interface EmployeeSalaryStructureController {

	@Operation(summary = "Creates a new EmployeeSalaryStructure", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeSalaryStructure create(@Valid @RequestBody EmployeeSalaryStructure employeeSalaryStructure);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of EmployeeSalaryStructure with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<EmployeeSalaryStructure> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given EmployeeSalaryStructure", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeSalaryStructure update(@Valid @RequestBody EmployeeSalaryStructure employeeSalaryStructure);

	@Operation(summary = "To delete the given EmployeeSalaryStructure by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the EmployeeSalaryStructure", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "deleteAll")
	void bulkDelete(
			@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	@Operation(summary = "To get EmployeeSalaryStructure by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	EmployeeSalaryStructure findById(
			@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all EmployeeSalaryStructure by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<EmployeeSalaryStructure> findAllById(
			@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "auditHistory/{id}")
	String auditHistory(@PathVariable(value = "id") int id,
			@Valid @RequestParam(name = APIConstants.LIMIT, required = true) Integer limit,
			@Valid @RequestParam(name = APIConstants.SKIP, required = true) Integer skip);

	@Operation(summary = "Import Data for EmployeeSalaryStructure", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "importData", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	String importData(@RequestParam(required = true, name = "file") MultipartFile excelFile)
			throws IOException, InstantiationException, ClassNotFoundException;

	@Operation(summary = "To export the list of EmployeeSalaryStructure with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	ResponseEntity<byte[]> export(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@RequestParam(name = APIConstants.OFFSET, required = false) Integer offset,
			@RequestParam(name = APIConstants.SIZE, required = false) Integer size,

			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType) throws IOException;

	@Operation(summary = "To download the template ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "downloadTemplate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	ResponseEntity<byte[]> downloadTemplate(@RequestParam(name = "fileName", required = true) String fileName)
			throws IOException;

	@Operation(summary = "Api to execute payroll", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEE_PAYROLL_EXECUTE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "payrollRun")
	PayrollRun payrollRun(@RequestBody(required = false) PayrollRequestWrapper payrollRequestWrapper);

	@Operation(summary = "To get EmployeeSalaryStructure by employeeId", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getEmployeeMappedSalaryStructure")
	EmployeeSalaryStructure getEmployeeMappedSalaryStructure(
			@RequestParam(name = "employeeId", required = false) Integer employeeId);

	@Operation(summary = "Verify one-time Salary Component Calculation existance", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "verifyOneTimeComponentCalculated")
	Object verifyOneTimeComponentCalculated(@RequestParam(name = "employeeId", required = false) Integer employeeId);

	@Operation(summary = "To get Employee Salary Structure for Current EmployeeUserID", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEESALARYSTRUCTURE_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "getEmployeeSalaryStructureByUserId")
	List<EmployeeSalaryStructure> getEmployeeSalaryStructureByUserId();

	
	@Operation(summary = "Create Employee_Salary_Structure from Sheet", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_EMPLOYEE_PAYROLL_EXECUTE" }) })
	@PostMapping(path = "importEmployeeSalaryStructureData")
	public List<EmployeeSalaryStructure> importEmployeeSalaryStructureData(@RequestParam(required = false, name = "fileData") MultipartFile excelFile);

}
