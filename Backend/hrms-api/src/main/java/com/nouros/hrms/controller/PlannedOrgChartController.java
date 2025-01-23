package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.PlannedOrgChart;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.BusinessExpenseDto;
import com.nouros.hrms.wrapper.PlannedOrgChartDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "PlannedOrgChartController", url = "${hrms.url}", path = "/PlannedOrgChart", primary = false)
public interface PlannedOrgChartController {

	@Operation(summary = "Creates a new PlannedOrgChart", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	PlannedOrgChart create(@Valid @RequestBody PlannedOrgChart plannedOrgChart);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of PlannedOrgChart with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<PlannedOrgChart> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given PlannedOrgChart", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	PlannedOrgChart update(@Valid @RequestBody PlannedOrgChart plannedOrgChart);

	@Operation(summary = "To delete the given PlannedOrgChart by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


	@Operation(summary = "To get PlannedOrgChart by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	PlannedOrgChart findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all PlannedOrgChart by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_PLANNED_ORG_CHART_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<PlannedOrgChart> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	
	@Operation(summary = "To get all PlannedOrgChart by given DepartmentId", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_PLANNED_ORG_CHART_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "searchPlannedOrgChartByDeparmentId")
	List<PlannedOrgChart> searchPlannedOrgChartByDeparmentId(@RequestParam("departmentId") Integer departmentId);
	
	@Operation(summary = "To get all Org Chart Designation by given DepartmentId", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_PLANNED_ORG_CHART_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "searchOrgChartDesignationsByDepartmentId")
	PlannedOrgChartDto searchOrgChartDesignationsByDepartmentId(@RequestParam("departmentId") Integer departmentId);
	
	@Operation(summary = "To delete PlannedOrgChart by Department Id", security = {
	        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
	            "ROLE_API_ORGCHART_DESIGNATION_WRITE"})
	    })
	    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	    @GetMapping(path = "deletePlannedOrgChartByDepartmentId")
	    public String deletePlannedOrgChartByDepartmentId(@RequestParam("departmentId") Integer departmentId);

	  @Operation(summary = "To create Designation by Approved PlannedOrgChart", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_ORGCHART_DESIGNATION_READ" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	  @GetMapping(path = "createDesignationByApprovedPlannedOrgChart")
	  String createDesignationByApprovedPlannedOrgChart(@Valid @RequestParam(name = "plannedOrgChartId", required = false) Integer plannedOrgChartId, 
	    		@Valid @RequestParam(name = "processInstanceId", required = false) String processInstanceId);
	  
	  
	  @Operation(summary = "To get all Org Chart Designation by given PlannedOrgChart Id", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_PLANNED_ORG_CHART_READ" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@GetMapping(path = "searchOrgChartDesignationsByPlannedOrgChartId")
		PlannedOrgChartDto searchOrgChartDesignationsByPlannedOrgChartId(@RequestParam("plannedOrgChartId") Integer plannedOrgChartId);
		
}
