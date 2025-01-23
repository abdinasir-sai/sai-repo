package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.TravelExpense;
import com.nouros.hrms.repository.TravelExpenseRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TravelExpenseService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "TravelExpenseServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "TravelExpenseService" interface and it extends the "AbstractService"
 * class, which seems to be a generic class for handling CRUD operations for
 * entities. This class is annotated with @Service, indicating that it is a
 * Spring Service bean. This class is using Lombok's @Slf4j annotation which
 * will automatically generate an Slf4j based logger instance, so it is using
 * the Slf4j API for logging. The class has a constructor which takes a single
 * parameter of GenericRepository TravelExpense and is used to call the
 * superclass's constructor. This class have one public method public byte[]
 * export(List of TravelExpense TravelExpense) for exporting the TravelExpense
 * data into excel file by reading the template and mapping the TravelExpense
 * details into it. It's using Apache POI library for reading and writing excel
 * files, and has methods for parsing the json files for column names and
 * identities , and it also used 'ExcelUtils' for handling the excel operations.
 * It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class TravelExpenseServiceImpl extends AbstractService<Integer,TravelExpense> implements TravelExpenseService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   TravelExpense entities.
	 */

	private static final Logger log = LogManager.getLogger(TravelExpenseServiceImpl.class);

	public TravelExpenseServiceImpl(GenericRepository<TravelExpense> repository) {
		super(repository, TravelExpense.class);
	}

	@Autowired
	private TravelExpenseRepository travelExpenseRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param travelExpense The travelExpense object to create.
	 * @return The created vendor object.
	 */
	@Override
	public TravelExpense create(TravelExpense travelExpense) {
		log.info("inside @class TravelExpenseServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		travelExpense.setWorkspaceId(workspaceId); // done done
		return travelExpenseRepository.save(travelExpense);
	}

}
