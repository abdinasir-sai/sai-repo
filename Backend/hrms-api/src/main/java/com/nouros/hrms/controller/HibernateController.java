package com.nouros.hrms.controller;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ResponseBody
@FeignClient(name = "HibernateController", url = "${hrms.url}", path = "/HibernateEntry", primary = false)
public interface HibernateController {

	@Operation(summary = "To review POJO columns", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
					"ROLE_API_COLUMN_FIND" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "/getPojoEntries")
	 List<ObjectNode>  getEntityDetailsFromPackageAsJson();
	
}
