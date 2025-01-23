/* (C)2024 */
package com.nouros.hrms.controller;

import com.nouros.hrms.model.EmployeeClearanceCertificate;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
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


@ResponseBody
@FeignClient(name = "EmployeeClearanceCertificateController", url = "${hrms.url}", path = "/EmployeeClearanceCertificate", primary = false)
public interface EmployeeClearanceCertificateController {

	public static final String ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE = "ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE";
	public static final String ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ = "ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ";

	@Operation(summary = "Creates a new EmployeeClearanceCertificate", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeClearanceCertificate create(@Valid @RequestBody EmployeeClearanceCertificate employeeClearanceCertificate);

	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of EmployeeClearanceCertificate with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<EmployeeClearanceCertificate> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "To update the given EmployeeClearanceCertificate", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	EmployeeClearanceCertificate update(@Valid @RequestBody EmployeeClearanceCertificate employeeClearanceCertificate);

	@Operation(summary = "To delete the given EmployeeClearanceCertificate by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To delete all the EmployeeClearanceCertificate", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_WRITE }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "deleteAll")
	void bulkDelete(
			@Valid @Parameter(name = APIConstants.ID, required = true) @RequestBody(required = true) List<Integer> list);

	@Operation(summary = "To get EmployeeClearanceCertificate by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	EmployeeClearanceCertificate findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get all EmployeeClearanceCertificate by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					ROLE_API_EMPLOYEECLEARANCECERTIFICATE_READ }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<EmployeeClearanceCertificate> findAllById(
			@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

}
