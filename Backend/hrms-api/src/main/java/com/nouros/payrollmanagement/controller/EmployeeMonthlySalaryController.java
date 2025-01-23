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
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.wrapper.PayrollTotals;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface EmployeeMonthlySalaryController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to EmployeeMonthlySalary.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {EmployeeMonthlySalary.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new EmployeeMonthlySalary with the given request body and returns the created EmployeeMonthlySalary.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of EmployeeMonthlySalary with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of EmployeeMonthlySalary based on the RSQL filter and returns the list of EmployeeMonthlySalary matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the EmployeeMonthlySalary by ID and returns the EmployeeMonthlySalary if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing EmployeeMonthlySalary with the given id with the request body and returns the updated EmployeeMonthlySalary.
delete(@PathVariable("id") Long id) : deletes the EmployeeMonthlySalary with the given id and returns the deleted EmployeeMonthlySalary.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 *
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "EmployeeMonthlySalaryController", url = "${hrms.url}", path = "/EmployeeMonthlySalary", primary = false)
public interface EmployeeMonthlySalaryController {

  @Operation(summary = "Creates a new EmployeeMonthlySalary",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeMonthlySalary create(@Valid @RequestBody EmployeeMonthlySalary employeeMonthlySalary);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of EmployeeMonthlySalary with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<EmployeeMonthlySalary> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given EmployeeMonthlySalary",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  EmployeeMonthlySalary update(@Valid @RequestBody EmployeeMonthlySalary employeeMonthlySalary);

  @Operation(summary = "To delete the given EmployeeMonthlySalary by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  @Operation(summary = "To delete all the EmployeeMonthlySalary",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "deleteAll")
  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

  @Operation(summary = "To get EmployeeMonthlySalary by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  EmployeeMonthlySalary findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all EmployeeMonthlySalary by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<EmployeeMonthlySalary> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "auditHistory/{id}")
   String auditHistory(@PathVariable(value = "id")  int id,@Valid @RequestParam(name = APIConstants.LIMIT, required = true) Integer limit,
		      @Valid @RequestParam(name = APIConstants.SKIP, required = true) Integer skip) ;
		      
		      
  @Operation(summary = "Import Data for EmployeeMonthlySalary",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_WRITE"})
      })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path ="importData", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
String importData(@RequestParam(required = true, name = "file") MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException ;		      


@Operation(summary = "To export the list of EmployeeMonthlySalary with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
      })
  

  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "export",produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
  ResponseEntity<byte[]> export(@RequestParam(name = APIConstants.QUERY, required =false) String filter, @RequestParam(name = APIConstants.OFFSET, required = false) Integer offset,
       @RequestParam(name = APIConstants.SIZE, required = false) Integer size,      

      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType) throws IOException;
      
        @Operation(summary = "To download the template ",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
  })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "downloadTemplate", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
  ResponseEntity<byte[]> downloadTemplate(@RequestParam(name ="fileName", required = true) String fileName) throws IOException;


  @Operation(summary = "To get employeeMonthlySalary for an Context employee",  security = {
@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    "ROLE_API_EMPLOYEE_MONTHLY_SALARY_READ"})
})
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "getEmployeeMonthlySalary")
  String getEmployeeMonthlySalary(@RequestParam(name = "employeeId", required = false) Integer employeeId);
 
  
  @Operation(summary = "To generate wps file for an payroll by processinstanceid",  security = {
@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    "ROLE_API_EMPLOYEE_MONTHLY_SALARY_CREATE_WPS"})
})
  @GetMapping(path = "createWpsTxtFile")
  ResponseEntity<byte[]> createWpsTxtFile(@RequestParam("processInstanceId") String processInstanceId);
  
  @Operation(summary = "To generate wps file for an payroll by processinstanceid",  security = {
    @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
        "ROLE_API_EMPLOYEE_MONTHLY_SALARY_CREATE_WPS"})
    })
      @GetMapping(path = "createGOSIReportFile")
      ResponseEntity<byte[]> createGOSIReportFile(@RequestParam("payrollRunId") Integer payrollRunId);
      
      @Operation(summary = "To generate wps file for Board of Director an payroll by processinstanceid",  security = {
    		  @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    		      "ROLE_API_BOARD_OF_DIRECTOR_CREATE_WPS"})
    		  })
      @GetMapping(path = "createWpsTxtFileForBOD")
      ResponseEntity<byte[]> createWpsTxtFileForBod(@RequestParam("processInstanceId") String processInstanceId);      

      @Operation(summary = "To download wps file for Board of Director  by month and year",  security = {
    		  @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    		      "ROLE_API_BOARD_OF_DIRECTOR_DOWNLOAD_WPS"})
    		  })
      @GetMapping(path = "getWpsTxtFileForBOD")
      ResponseEntity<byte[]> getWpsTxtFileForBod(@RequestParam("year")String year , @RequestParam("month")String month);      

      
      @Operation(summary = "To generate sum an payroll components",  security = {
    		    @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    		        "ROLE_API_EMPLOYEE_MONTHLY_SALARY_GET_SUM"})
    		    })
      @GetMapping(path = "getPayrollSum")
      PayrollTotals getPayrollSum(@RequestParam(name ="payrollRunId", required = true) Integer payrollRunId);
    		      
}
