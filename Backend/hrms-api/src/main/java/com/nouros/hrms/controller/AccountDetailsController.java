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

import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.Nationality;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "AccountDetailsController", url = "${hrms.url}", path = "/AccountDetails", primary = false)
public interface AccountDetailsController {
	
	@Operation(summary = "Creates a new AccountDetails", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	AccountDetails create(@Valid @RequestBody AccountDetails accountDetails);
	
	@Operation(summary = "To get the count with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

	@Operation(summary = "To get the list of AccountDetails with RSQL supported filter", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_READ" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "search")
	List<AccountDetails> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
	
	@Operation(summary = "To update the given AccountDetails ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_WRITE" }) })

	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	AccountDetails update(@Valid @RequestBody AccountDetails accountDetails );
	
	@Operation(summary = "To delete the given AccountDetails by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_WRITE" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To get AccountDetails by Id", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findById")
	AccountDetails findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
	
	@Operation(summary = "To get all AccountDetails by given Ids", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_ACCOUNT_DETAILS_READ" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "findAllById")
	List<AccountDetails> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);



	

}
