package com.nouros.payrollmanagement.controller.impl;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.payrollmanagement.controller.MasterDataController;
import com.nouros.payrollmanagement.model.MasterData;
import com.nouros.payrollmanagement.service.MasterDataService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/MasterData")
public class MasterDataControllerImpl implements MasterDataController {

	
	@Autowired
	private MasterDataService masterDataService;

	@Override
	public MasterData create(@Valid MasterData masterData) {
		 
		return masterDataService.create(masterData);
	}

	@Override
	public Long count(String filter) {
		return masterDataService.count(filter);
	}

	@Override
	public List<MasterData> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		return masterDataService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public MasterData update(@Valid MasterData masterData) {
		return masterDataService.update(masterData);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		masterDataService.deleteById(id);
	}

	@Override
	public MasterData findById(@Valid Integer id) {
		return masterDataService.findById(id);
	}

	@Override
	public List<MasterData> findAllById(@Valid List<Integer> id) {
		return masterDataService.findAllById(id);
	}
	
	 }
