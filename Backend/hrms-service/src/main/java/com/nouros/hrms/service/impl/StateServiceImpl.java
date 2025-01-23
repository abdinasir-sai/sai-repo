package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.State;
import com.nouros.hrms.repository.StateRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.StateService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "StateServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "StateService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository State and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of State State)
 * for exporting the State data into excel file by reading the template and
 * mapping the State details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class StateServiceImpl extends AbstractService<Integer,State> implements StateService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   State entities.
	 */

	public StateServiceImpl(GenericRepository<State> repository) {
		super(repository, State.class);
	}

	private static final Logger log = LogManager.getLogger(StateServiceImpl.class);

	@Autowired
	private StateRepository stateRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param state The state object to create.
	 * @return The created vendor object.
	 */
	@Override
	public State create(State state) {
		log.info("inside @class StateServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		state.setWorkspaceId(workspaceId); // done done
		return stateRepository.save(state);
	}

}
