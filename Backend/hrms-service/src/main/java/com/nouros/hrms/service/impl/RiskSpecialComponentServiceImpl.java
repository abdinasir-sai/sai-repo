package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.RiskSpecialComponent;
import com.nouros.hrms.repository.RiskSpecialComponentRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.RiskSpecialComponentService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "RiskSpecialComponentServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "RiskSpecialComponentService" interface and it extends
 * the "AbstractService" class, which seems to be a generic class for handling
 * CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * RiskSpecialComponent and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of
 * RiskSpecialComponent RiskSpecialComponent) for exporting the
 * RiskSpecialComponent data into excel file by reading the template and mapping
 * the RiskSpecialComponent details into it. It's using Apache POI library for
 * reading and writing excel files, and has methods for parsing the json files
 * for column names and identities , and it also used 'ExcelUtils' for handling
 * the excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class RiskSpecialComponentServiceImpl extends AbstractService<Integer,RiskSpecialComponent>
		implements RiskSpecialComponentService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   RiskSpecialComponent entities.
	 */

	private static final Logger log = LogManager.getLogger(RiskSpecialComponentServiceImpl.class);

	public RiskSpecialComponentServiceImpl(GenericRepository<RiskSpecialComponent> repository) {
		super(repository, RiskSpecialComponent.class);
	}

	@Autowired
	private RiskSpecialComponentRepository riskSpecialComponentRepository;

	/**
	 * @param riskSpecialComponent The riskSpecialComponent object to create.
	 * @return The created vendor object.
	 */
	@Override
	public RiskSpecialComponent create(RiskSpecialComponent riskSpecialComponent) {
		log.info("inside @class RiskSpecialComponentServiceImpl @method create");
		return riskSpecialComponentRepository.save(riskSpecialComponent);
	}

}
