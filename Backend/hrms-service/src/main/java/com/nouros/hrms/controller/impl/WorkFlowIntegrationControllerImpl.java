package com.nouros.hrms.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.WorkFlowIntegrationController;
import com.nouros.hrms.service.WorkFlowIntegrationService;

@Primary
@RestController
@RequestMapping("/WorkFlowIntegration")
public class WorkFlowIntegrationControllerImpl implements WorkFlowIntegrationController {

	@Autowired
	private WorkFlowIntegrationService workFlowIntegrationService;

	@Override
	public String updateActions(String processInstanceId) {
		return workFlowIntegrationService.updateActions(processInstanceId);
	}

}
