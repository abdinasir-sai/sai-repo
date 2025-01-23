package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Shifts;
import com.nouros.hrms.repository.ShiftsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ShiftsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "ShiftsServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "ShiftsService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Shifts and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Shifts Shifts)
 * for exporting the Shifts data into excel file by reading the template and
 * mapping the Shifts details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class ShiftsServiceImpl extends AbstractService<Integer,Shifts> implements ShiftsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Shifts entities.
	 */

	private static final Logger log = LogManager.getLogger(ShiftsServiceImpl.class);

	public ShiftsServiceImpl(GenericRepository<Shifts> repository) {
		super(repository, Shifts.class);
	}

	@Autowired
	private ShiftsRepository shiftsRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param shifts The shifts object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Shifts create(Shifts shifts) {
		log.info("inside @class ShiftsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		shifts.setWorkspaceId(workspaceId); // done done
		return shiftsRepository.save(shifts);
	}

}
