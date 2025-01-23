package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Country;
import com.nouros.hrms.repository.CountryRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CountryService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "CountryServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "CountryService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Country and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Country
 * Country) for exporting the Country data into excel file by reading the
 * template and mapping the Country details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class CountryServiceImpl extends AbstractService<Integer,Country> implements CountryService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Country entities.
	 */

	 private static final Logger log = LogManager.getLogger(CountryServiceImpl.class);

	public CountryServiceImpl(GenericRepository<Country> repository) {
		super(repository, Country.class);
	}

	@Autowired
	private CountryRepository countryRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param country The country object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Country create(Country country) {
		log.info("inside @class CountryServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		country.setWorkspaceId(workspaceId); // done done
		return countryRepository.save(country);
	}

}
