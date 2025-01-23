package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.CompanyDetails;
import com.nouros.hrms.repository.CompanyDetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CompanyDetailsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "CompanyDetailsServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "CompanyDetailsService" interface and it extends the "AbstractService"
 * class, which seems to be a generic class for handling CRUD operations for
 * entities. This class is annotated with @Service, indicating that it is a
 * Spring Service bean. This class is using Lombok's @Slf4j annotation which
 * will automatically generate an Slf4j based logger instance, so it is using
 * the Slf4j API for logging. The class has a constructor which takes a single
 * parameter of GenericRepository CompanyDetails and is used to call the
 * superclass's constructor. This class have one public method public byte[]
 * export(List of CompanyDetails CompanyDetails) for exporting the
 * CompanyDetails data into excel file by reading the template and mapping the
 * CompanyDetails details into it. It's using Apache POI library for reading and
 * writing excel files, and has methods for parsing the json files for column
 * names and identities , and it also used 'ExcelUtils' for handling the excel
 * operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class CompanyDetailsServiceImpl extends AbstractService<Integer,CompanyDetails> implements CompanyDetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   CompanyDetails entities.
	 */

	private static final Logger log = LogManager.getLogger(CompanyDetailsServiceImpl.class);

	public CompanyDetailsServiceImpl(GenericRepository<CompanyDetails> repository) {
		super(repository, CompanyDetails.class);
	}

	@Autowired
	private CompanyDetailsRepository companyDetailsRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param companyDetails The companyDetails object to create.
	 * @return The created vendor object.
	 */
	@Override
	public CompanyDetails create(CompanyDetails companyDetails) {
		log.info("inside @class CompanyDetailsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		companyDetails.setWorkspaceId(workspaceId); // done done
		return companyDetailsRepository.save(companyDetails);
	}

}
