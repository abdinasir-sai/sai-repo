package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.OtherExpenseBankRequestController;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.service.OtherExpenseBankRequestService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/OtherExpenseBankRequest")
public class OtherExpenseBankRequestControllerImpl implements OtherExpenseBankRequestController {

	private static final Logger log = LogManager.getLogger(OtherExpenseBankRequestControllerImpl.class);

	@Autowired
	  private OtherExpenseBankRequestService otherExpenseBankRequestService;

	@Override
	public OtherExpenseBankRequest create(@Valid OtherExpenseBankRequest otherExpenseBankRequest) {
		
		return otherExpenseBankRequestService.create(otherExpenseBankRequest);
	}

	@Override
	public Long count(String filter) {
		
		return otherExpenseBankRequestService.count(filter);
	}

	@Override
	public List<OtherExpenseBankRequest> search(String filter, @Valid Integer offset, @Valid Integer size,
			String orderBy, String orderType) {
		
		return otherExpenseBankRequestService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public OtherExpenseBankRequest update(@Valid OtherExpenseBankRequest otherExpenseBankRequest) {
		
		return otherExpenseBankRequestService.update(otherExpenseBankRequest);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		
		 otherExpenseBankRequestService.deleteById(id);
		
	}

	@Override
	public OtherExpenseBankRequest findById(@Valid Integer id) {
		
		return otherExpenseBankRequestService.findById(id);
	}

	@Override
	public List<OtherExpenseBankRequest> findAllById(@Valid List<Integer> id) {
		
		return otherExpenseBankRequestService.findAllById(id);
	}
}
