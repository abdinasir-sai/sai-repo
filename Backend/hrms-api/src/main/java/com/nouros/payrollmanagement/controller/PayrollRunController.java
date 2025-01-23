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

import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.LeavesDto;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.wrapper.PayrollRunWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * 
 * The interface PayrollRunController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to PayrollRun.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {PayrollRun.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new PayrollRun with the given request body and returns the created PayrollRun.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of PayrollRun with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of PayrollRun based on the RSQL filter and returns the list of PayrollRun matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the PayrollRun by ID and returns the PayrollRun if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing PayrollRun with the given id with the request body and returns the updated PayrollRun.
delete(@PathVariable("id") Long id) : deletes the PayrollRun with the given id and returns the deleted PayrollRun.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "PayrollRunController", url = "${hrms.url}", path = "/PayrollRun", primary = false)
public interface PayrollRunController {

  @Operation(summary = "Creates a new PayrollRun",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  PayrollRun create(@Valid @RequestBody PayrollRun payrollRun);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of PayrollRun with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<PayrollRun> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given PayrollRun",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  PayrollRun update(@RequestBody PayrollRun payrollRun);

  @Operation(summary = "To delete the given PayrollRun by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "deleteById")
  void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
  
  @Operation(summary = "To delete all the PayrollRun",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "deleteAll")
  void bulkDelete(@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

  @Operation(summary = "To get PayrollRun by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  PayrollRun findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all PayrollRun by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<PayrollRun> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "auditHistory/{id}")
   String auditHistory(@PathVariable(value = "id")  int id,@Valid @RequestParam(name = APIConstants.LIMIT, required = true) Integer limit,
		      @Valid @RequestParam(name = APIConstants.SKIP, required = true) Integer skip) ;
		      
		      
  @Operation(summary = "Import Data for PayrollRun",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_WRITE"})
      })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path ="importData", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
String importData(@RequestParam(required = true, name = "file") MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException ;		      


@Operation(summary = "To export the list of PayrollRun with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
      })
  

  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "export",produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
  ResponseEntity<byte[]> export(@RequestParam(name = APIConstants.QUERY, required =false) String filter, @RequestParam(name = APIConstants.OFFSET, required = false) Integer offset,
       @RequestParam(name = APIConstants.SIZE, required = false) Integer size,      

      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType) throws IOException;
      
        @Operation(summary = "To download the template ",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_PAYROLLRUN_READ"})
  })
  

  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "downloadTemplate", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
  ResponseEntity<byte[]> downloadTemplate(@RequestParam(name ="fileName", required = true) String fileName) throws IOException;

   
   
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
   	@Operation(summary = "To get and generate excel file ",  security = {
	  	       @SecurityRequirement (name = APIConstants.DEFAULT_SCHEME, scopes = {
	  	          "ROLE_API_PAYROLLRUN_READ"})
	  	  })
		  @GetMapping(path = "getActions")
		  List<WorkflowActions> getActions(
		      @Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@Operation(summary = "To get and generate excel file ",  security = {
	  	       @SecurityRequirement (name = APIConstants.DEFAULT_SCHEME, scopes = {
	  	          "ROLE_API_PAYROLLRUN_GENERATE_FILE"})
	  	  })
	@GetMapping(path = "generateExcel")
	ResponseEntity<byte[]> generateExcel(@RequestParam(name = APIConstants.ID, required = true) Integer payrollId) throws IOException;


     @Operation(summary = "To get reasons of variance in payroll by Id",  security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
            "ROLE_API_PAYROLLRUN_WRITE"})
        })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "identifyVarianceReasons")
    PayrollRun identifyVarianceReasons(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
     
     @Operation(summary = "To Rerun payroll",  security = {
    	        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
    	            "ROLE_API_PAYROLLRUN_EXECUTION_RERUN"})
    	        })
    	    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    	    @PostMapping(path = "reExecutePayroll")
     PayrollRun reExecutePayroll( @RequestParam(name = "month", required = false) Integer month, @RequestParam(name = "year", required = false) Integer year);

     @Operation(summary = "To get year ,month and id,rerunflag for payroll overview",  security = {
         @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
             "ROLE_API_PAYROLLRUN_READ"})
         })
     @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
     @GetMapping(path = "getPayrollEmployeeMonthlySalary")
     Object getPayrollEmployeeMonthlySalary();
 
     
     @Operation(summary = "To update PayrollRun work flow stage", security = {
 			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
 					"ROLE_API_PAYROLLRUN_WRITE" }) })
 	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
 	@PostMapping(path = "updatePayrollRunWorkflowStage")
     PayrollRun updatePayrollRunWorkflowStage(@RequestBody PayrollRunWrapper payrollRunWrapper);

}
