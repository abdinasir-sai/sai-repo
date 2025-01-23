package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Feedback;
import com.nouros.hrms.repository.FeedbackRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.FeedbackService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "FeedbackServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "FeedbackService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Feedback and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Feedback
 * Feedback) for exporting the Feedback data into excel file by reading the
 * template and mapping the Feedback details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class FeedbackServiceImpl extends AbstractService<Integer,Feedback> implements FeedbackService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Feedback entities.
	 */

	private static final Logger log = LogManager.getLogger(FeedbackServiceImpl.class);

	public FeedbackServiceImpl(GenericRepository<Feedback> repository) {
		super(repository, Feedback.class);
	}

	@Autowired
	private FeedbackRepository feedbackRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param feedback The feedback object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Feedback create(Feedback feedback) {
		log.info("inside @class FeedbackServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		feedback.setWorkspaceId(workspaceId); // done done
		return feedbackRepository.save(feedback);
	}

}
