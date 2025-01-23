package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.EmployeeFeedbackMapping;
import com.nouros.hrms.repository.EmployeeFeedbackMappingRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeFeedbackMappingService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeFeedbackMappingServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeFeedbackMappingService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeFeedbackMapping and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeFeedbackMapping EmployeeFeedbackMapping) for exporting the
 * EmployeeFeedbackMapping data into excel file by reading the template and
 * mapping the EmployeeFeedbackMapping details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeFeedbackMappingServiceImpl extends AbstractService<Integer,EmployeeFeedbackMapping>
		implements EmployeeFeedbackMappingService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeFeedbackMapping entities.
	 */

	public EmployeeFeedbackMappingServiceImpl(GenericRepository<EmployeeFeedbackMapping> repository) {
		super(repository, EmployeeFeedbackMapping.class);
	}
 
	private static final Logger log = LogManager.getLogger(EmployeeFeedbackMappingServiceImpl.class);

	@Autowired
	private EmployeeFeedbackMappingRepository employeeFeedbackMappingRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeFeedbackMapping The employeeFeedbackMapping object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeFeedbackMapping create(EmployeeFeedbackMapping employeeFeedbackMapping) {
		log.info("inside @class EmployeeFeedbackMappingServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeFeedbackMapping.setWorkspaceId(workspaceId); // done done
		return employeeFeedbackMappingRepository.save(employeeFeedbackMapping);
	}

}
