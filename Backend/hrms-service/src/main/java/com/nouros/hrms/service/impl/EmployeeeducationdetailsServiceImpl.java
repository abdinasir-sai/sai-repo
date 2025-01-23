package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Employeeeducationdetails;
import com.nouros.hrms.repository.EmployeeeducationdetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeeducationdetailsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeeducationdetailsServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeeducationdetailsService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * Employeeeducationdetails and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * Employeeeducationdetails Employeeeducationdetails) for exporting the
 * Employeeeducationdetails data into excel file by reading the template and
 * mapping the Employeeeducationdetails details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeeducationdetailsServiceImpl extends AbstractService<Integer,Employeeeducationdetails>
		implements EmployeeeducationdetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Employeeeducationdetails entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeeducationdetailsServiceImpl.class);

	public EmployeeeducationdetailsServiceImpl(GenericRepository<Employeeeducationdetails> repository) {
		super(repository, Employeeeducationdetails.class);
	}

	@Autowired
	private EmployeeeducationdetailsRepository employeeeducationdetailsRepository;



	/**
	 * Creates a new vendor.
	 *
	 * @param employeeeducationdetails The employeeeducationdetails object to
	 *                                 create.
	 * @return The created vendor object.
	 */
	@Override
	public Employeeeducationdetails create(Employeeeducationdetails employeeeducationdetails) {
		log.info("inside @class EmployeeeducationdetailsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeeducationdetails.setWorkspaceId(workspaceId); // done done
		return employeeeducationdetailsRepository.save(employeeeducationdetails);
	}

}
