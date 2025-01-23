package com.nouros.hrms.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.repository.LocationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.LocationService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "LocationServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "LocationService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Location and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Location
 * Location) for exporting the Location data into excel file by reading the
 * template and mapping the Location details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class LocationServiceImpl extends AbstractService<Integer,Location> implements LocationService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Location entities.
	 */

	private static final Logger log = LogManager.getLogger(LocationServiceImpl.class);

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	public LocationServiceImpl(GenericRepository<Location> repository) {
		super(repository, Location.class);
	}

	@Autowired
	private LocationRepository locationRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param location The location object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Location create(Location location) {
		log.debug("Inside Method Create");
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		if (location.getCountry() != null || location.getStateProvince() != null) {
			String country = location.getCountry();
			log.debug("Country for rule is : {} " , country);
			mp.put("Country", country);
			String state = location.getStateProvince();
			log.debug("State for rule is : {} " , state);
			mp.put("State", state);
		}
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("locationRuleDynamic", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		location.setLocationId(generatedName);
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		location.setWorkspaceId(workspaceId); // done done
		return locationRepository.save(location);
	}

}
