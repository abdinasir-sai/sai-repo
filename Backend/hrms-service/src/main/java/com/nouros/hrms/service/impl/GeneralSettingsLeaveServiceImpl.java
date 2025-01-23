package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.GeneralSettingsLeave;
import com.nouros.hrms.repository.GeneralSettingsLeaveRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.GeneralSettingsLeaveService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "GeneralSettingsLeaveServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "GeneralSettingsLeaveService" interface and it extends
 * the "AbstractService" class, which seems to be a generic class for handling
 * CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * GeneralSettingsLeave and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of
 * GeneralSettingsLeave GeneralSettingsLeave) for exporting the
 * GeneralSettingsLeave data into excel file by reading the template and mapping
 * the GeneralSettingsLeave details into it. It's using Apache POI library for
 * reading and writing excel files, and has methods for parsing the json files
 * for column names and identities , and it also used 'ExcelUtils' for handling
 * the excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class GeneralSettingsLeaveServiceImpl extends AbstractService<Integer,GeneralSettingsLeave>
		implements GeneralSettingsLeaveService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   GeneralSettingsLeave entities.
	 */

	private static final Logger log = LogManager.getLogger(GeneralSettingsLeaveServiceImpl.class);

	public GeneralSettingsLeaveServiceImpl(GenericRepository<GeneralSettingsLeave> repository) {
		super(repository, GeneralSettingsLeave.class);
	}

	@Autowired
	private GeneralSettingsLeaveRepository generalSettingsLeaveRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param generalSettingsLeave The generalSettingsLeave object to create.
	 * @return The created vendor object.
	 */
	@Override
	public GeneralSettingsLeave create(GeneralSettingsLeave generalSettingsLeave) {
		log.info("inside @class GeneralSettingsLeaveServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		generalSettingsLeave.setWorkspaceId(workspaceId); // done done
		return generalSettingsLeaveRepository.save(generalSettingsLeave);
	}

}
