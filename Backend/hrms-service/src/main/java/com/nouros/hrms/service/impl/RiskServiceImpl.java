package com.nouros.hrms.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.nouros.hrms.model.Risk;
import com.nouros.hrms.repository.RiskRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.RiskService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "RiskServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "RiskService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Risk and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of Risk Risk) for
 * exporting the Risk data into excel file by reading the template and mapping
 * the Risk details into it. It's using Apache POI library for reading and
 * writing excel files, and has methods for parsing the json files for column
 * names and identities , and it also used 'ExcelUtils' for handling the excel
 * operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class RiskServiceImpl extends AbstractService<Integer,Risk> implements RiskService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Risk entities.
	 */

	private static final Logger log = LogManager.getLogger(RiskServiceImpl.class);

	public RiskServiceImpl(GenericRepository<Risk> repository) {
		super(repository, Risk.class);
	}

	@Autowired
	private RiskRepository riskRepository;

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	@Override
	public Risk createWithNaming(Risk risk) {
		Map<String, String> mp = new HashMap<>();

		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;

		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("risk Rule", mp, Status.ALLOCATED);

		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);

		generatedName = nameGenerationWrapperV2.getGeneratedName();

		risk.setRiskId(generatedName);
		return riskRepository.save(risk);

	}

	/**
	 * @param risk The risk object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Risk create(Risk risk) {
		return riskRepository.save(risk);
	}

}
