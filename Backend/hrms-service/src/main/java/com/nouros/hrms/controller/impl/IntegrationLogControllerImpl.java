package com.nouros.hrms.controller.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.IntegrationLogController;
import com.nouros.hrms.service.IntegrationLogService;


@RestController
@RequestMapping("IntegrationLog")
public class IntegrationLogControllerImpl implements IntegrationLogController {

	public static final String INSIDE_METHOD = "Inside @method {} ";
	
	@Autowired
	IntegrationLogService integrationLogService;
	
	
	private static final Logger logger = LogManager.getLogger(IntegrationLogControllerImpl.class);


	 
 
 
 

}
