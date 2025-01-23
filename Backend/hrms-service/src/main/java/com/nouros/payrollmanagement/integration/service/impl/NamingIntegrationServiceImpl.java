package com.nouros.payrollmanagement.integration.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.nouros.payrollmanagement.integration.service.NamingIntegrationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class NamingIntegrationServiceImpl implements NamingIntegrationService{
	
	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;
	
	private static final Logger log = LogManager.getLogger(NamingIntegrationServiceImpl.class);
	
    @Override
	public String generateNaming(String rulename  ,Map<String, String> mp ){
		
		try {
	    log.info("inside NamingIntegrationServiceImpl @METHOD generateNaming nameGenerationWrapperV2: rulename : {} , map :{} ", rulename , mp);
			
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName(rulename, mp,
				Status.ALLOCATED);
		log.info("inside NamingIntegrationServiceImpl @METHOD generateNaming nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		return nameGenerationWrapperV2.getGeneratedName();
		}catch(Exception ex) {
			log.error("error getting in naming :{}",ex.getMessage());
		}
		return null;
	}

}
