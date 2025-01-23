package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Jobs;
import com.nouros.hrms.repository.JobsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.JobsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "JobsServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "JobsService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Jobs and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of Jobs Jobs) for
 * exporting the Jobs data into excel file by reading the template and mapping
 * the Jobs details into it. It's using Apache POI library for reading and
 * writing excel files, and has methods for parsing the json files for column
 * names and identities , and it also used 'ExcelUtils' for handling the excel
 * operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class JobsServiceImpl extends AbstractService<Integer,Jobs> implements JobsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Jobs entities.
	 */

	private static final Logger log = LogManager.getLogger(JobsServiceImpl.class);

	public JobsServiceImpl(GenericRepository<Jobs> repository) {
		super(repository, Jobs.class);
	}

	@Autowired
	private JobsRepository jobsRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param jobs The jobs object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Jobs create(Jobs jobs) {
		log.info("inside @class JobsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		jobs.setWorkspaceId(workspaceId); // done done
		return jobsRepository.save(jobs);
	}

}
