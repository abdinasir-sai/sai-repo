package com.nouros.hrms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.BusinessTripDto;
import com.nouros.hrms.wrapper.MultiCityBusinessTripWrapper;
import com.nouros.hrms.wrapper.OneWayBusinessTripWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "BusinessTripController", url = "${hrms.url}", path = "/BusinessTrip", primary = false)
public interface BusinessTripController {

	@Operation(summary = "Creates a new BusinessTrip", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	BusinessTrip create(@Valid @RequestBody BusinessTrip businessTrip);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of BusinessTrip with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<BusinessTrip> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given BusinessTrip", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	BusinessTrip update(@Valid @RequestBody BusinessTrip businessTrip);

	@Operation(summary = "To delete the given BusinessTrip by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the BusinessTrip", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "deleteAll")
	void bulkDelete(
			@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	@Operation(summary = "To get BusinessTrip by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	BusinessTrip findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all BusinessTrip by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<BusinessTrip> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

	@Operation(summary = "Fetches Details of Airports based on County,City,ITA code , airport Name", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_FETCH" }) })
	@GetMapping(path = "getAirportDetails")
	List<Map<String, String>> getAirportDetails(@RequestParam(name = "valueField", required = true) String valueField);
	
	@Operation(summary = "Fetches Days and Pay Value based on Country and City For Single Trip", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_FETCH" }) })
	@PostMapping(path = "getCostingForSingleTrip")
	
	OneWayBusinessTripWrapper getCostingForSingleTrip( @RequestBody OneWayBusinessTripWrapper businessTripWrapper );
	
	@Operation(summary = "Fetches Days and Pay Value based on Country and City For Single Trip", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_FETCH" }) })
	@PostMapping(path = "getCostingForMultiTrip")
	
	MultiCityBusinessTripWrapper getCostingForMultiTrip( @RequestBody MultiCityBusinessTripWrapper businessTripWrapper );
	
	@Operation(summary = "To delete BusinessTrip associated with  mapping by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_DELETE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteBusinessTripForEmployee")
	String deleteBusinessTripForEmployee(@Valid @RequestParam(name = APIConstants.ID, required = false) Integer id, @RequestParam(name = "employeeId", required = false) Integer employeeId );

	 @Operation(summary = "To update BusinessTrip work flow stage", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_BUSINESS_TRIP_WRITE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@PostMapping(path = "updateBusinessTripWorkflowStage")
	   BusinessTrip updateBusinessTripWorkflowStage(@RequestBody BusinessTripDto businessTripDto);
	 
	 
	   @Operation(summary = "To create Common wps for Trips ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_CREATE_TRIPS_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path="createCommonFileForTrips")
	   ResponseEntity<byte[]> createCommonFileForTrips();
	   
	   @Operation(summary = "To get Common wps for Trips ", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
						"ROLE_API_GET_TRIPS_COMMON_WPS" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	   @GetMapping(path = "getAllTripsRecordsWps")
	   ResponseEntity<byte[]> getAllTripsRecordsWps(@RequestParam("weekNum") Integer weekNum);
	   
	   @Operation(summary = "To update BusinessTrip workflow satge", security = {
				@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_BUSINESS_TRIP_WRITE" }) })
		@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		@GetMapping(path = "updateWorkflowStageForEmployee")
		void updateWorkflowStageForEmployee( );

	
	

	
	
}