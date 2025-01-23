package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.EmployeeWorkExperience;
import com.nouros.hrms.repository.EmployeeWorkExperienceRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeWorkExperienceService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeWorkExperienceServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeWorkExperienceService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeWorkExperience and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of
 * EmployeeWorkExperience EmployeeWorkExperience) for exporting the
 * EmployeeWorkExperience data into excel file by reading the template and
 * mapping the EmployeeWorkExperience details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeWorkExperienceServiceImpl extends AbstractService<Integer,EmployeeWorkExperience>
		implements EmployeeWorkExperienceService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeWorkExperience entities.
	 */

	public EmployeeWorkExperienceServiceImpl(GenericRepository<EmployeeWorkExperience> repository) {
		super(repository, EmployeeWorkExperience.class);
	}

	private static final Logger log = LogManager.getLogger(EmployeeWorkExperienceServiceImpl.class);

	@Autowired
	private EmployeeWorkExperienceRepository employeeWorkExperienceRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeWorkExperience The employeeWorkExperience object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeWorkExperience create(EmployeeWorkExperience employeeWorkExperience) {
		log.info("inside @class EmployeeWorkExperienceServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeWorkExperience.setWorkspaceId(workspaceId); // done done
		return employeeWorkExperienceRepository.save(employeeWorkExperience);
	}

}
