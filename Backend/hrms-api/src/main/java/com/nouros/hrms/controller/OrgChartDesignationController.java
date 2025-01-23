package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.DesignationSummaryWrapper;
import com.nouros.hrms.wrapper.OrgChartDesignationDto;
import com.nouros.hrms.wrapper.ProfessionalSummaryWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "OrgChartDesignationController", url = "${hrms.url}", path = "/OrgChartDesignation", primary = false)
public interface OrgChartDesignationController {
	
    @Operation(summary = "Creates a new OrgChartDesignation", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	OrgChartDesignation create(@Valid @RequestBody OrgChartDesignation orgChartDesignation);

    
    @Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
    
    @Operation(summary = "To get the list of OrgChartDesignation with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<OrgChartDesignation> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
    
    
    @Operation(summary = "To update the given OrgChartDesignation", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    OrgChartDesignation update(@Valid @RequestBody OrgChartDesignation orgChartDesignation);

	@Operation(summary = "To delete the given OrgChartDesignation by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get OrgChartDesignation by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	OrgChartDesignation findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all OrgChartDesignation by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ORGCHART_DESIGNATION_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<OrgChartDesignation> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	
	@Operation(summary = "To delete OrgChartDesignation by Id or Department Id", security = {
	        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	            "ROLE_API_ORGCHART_DESIGNATION_WRITE"})
	    })
	    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	    @GetMapping(path = "deleteOrgChartDesignation")
	    public String deleteOrgChartDesignation(
	    		@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id, 
	    		@Valid @RequestParam(name = "departmentId", required = false) Integer departmentId);
	
	@Operation(summary = "To regenerate Jobtitle based on Feed given by user", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_ORGCHART_DESIGNATION_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "generateJobTitleDescriptionByUserInput")
	String generateJobTitleDescriptionByUserInput(@RequestBody DesignationSummaryWrapper designationSummaryWrapper);
	
	@Operation(summary = "To Get Employee List By Approved Designation Name", security = {
	        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	            "ROLE_API_ORGCHART_DESIGNATION_WRITE"})
	    })
	    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	    @GetMapping(path = "getEmployeeByApprovedDesignation")
	    public List<Employee> getEmployeeByApprovedDesignation(
	    		@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	
}
