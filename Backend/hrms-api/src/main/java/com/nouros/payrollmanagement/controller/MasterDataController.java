package com.nouros.payrollmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.util.APIConstants;
import com.nouros.payrollmanagement.model.MasterData;

import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "MasterDataController", url = "${hrms.url}", path = "/MasterData",primary = false)
public interface MasterDataController {

	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	MasterData create(@Valid @RequestBody MasterData masterData);

	@GetMapping(path = "count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);
	
	@GetMapping(path = "search")
	List<MasterData> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);
	
	@PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	MasterData update(@Valid @RequestBody MasterData masterData);
	
	@GetMapping(path = "deleteById")
	void deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@GetMapping(path = "findById")
	MasterData findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@GetMapping(path = "findAllById")
	List<MasterData> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);
	

//	public String getJson(@RequestParam("id")Integer id);
	
//	@GetMapping("finance/ledgerdata")
//	@GetMapping("getPayrollJson")
//	public ResponseEntity<String> getPayrollJson(@RequestParam("id")Integer id);

}

