package com.nouros.hrms.controller.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.SuccessorController;
import com.nouros.hrms.model.Successor;
import com.nouros.hrms.service.SuccessorService;

import jakarta.validation.Valid;


@Primary
@RestController
@RequestMapping("/Successor")
public class SuccessorControllerImpl implements SuccessorController{

	@Autowired
	private SuccessorService successorService;
	
	private static final Logger log = LogManager.getLogger(SuccessorControllerImpl.class);

	@Override
	  @TriggerBPMN(entityName = "Successor", appName = "HRMS_APP_NAME")
	public Successor create(@Valid Successor successor) {
		// TODO Auto-generated method stub
		return successorService.create(successor);
	}

	@Override
	public Long count(String filter) {
		// TODO Auto-generated method stub
		return successorService.count(filter);
	}

	@Override
	public List<Successor> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		// TODO Auto-generated method stub
		return successorService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public Successor update(@Valid Successor successor) {
		// TODO Auto-generated method stub
		return successorService.update(successor);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		// TODO Auto-generated method stub
		successorService.deleteById(id);
	}

	@Override
	public Successor findById(@Valid Integer id) {
		// TODO Auto-generated method stub
		return successorService.findById(id);
	}

	@Override
	public List<Successor> findAllById(@Valid List<Integer> id) {
		// TODO Auto-generated method stub
		return successorService.findAllById(id);
	}
	
	@Override
	public String setRecommendedCandidate(Integer employeeSuccessorId)
	{
		return successorService.setRecommendedCandidate(employeeSuccessorId);
	}
	 
}
