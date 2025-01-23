package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Breakdetails;
import com.nouros.hrms.repository.BreakdetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.BreakdetailsService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * This is a class named "BreakdetailsServiceImpl" which is located in the package " com.nouros.hrms.service.impl", It appears to be an implementation of the "BreakdetailsService" interface and it extends the "AbstractService" class, which seems to be a generic class for handling CRUD operations for entities. This class is annotated with @Service, indicating that it is a Spring Service bean.
This class is using Lombok's @Slf4j annotation which will automatically generate an Slf4j based logger instance, so it is using the Slf4j API for logging.
The class has a constructor which takes a single parameter of GenericRepository Breakdetails and is used to call the superclass's constructor.
This class have one public method public byte[] export(List of Breakdetails  Breakdetails) for exporting the Breakdetails data into excel file by reading the template and mapping the Breakdetails details into it.
It's using Apache POI library for reading and writing excel files, and has methods for parsing the json files for column names and identities , and it also used 'ExcelUtils' for handling the excel operations.
It also uses 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and 'APIConstants' from 'com.nouros.hrms.util'
* */










@Service
public class BreakdetailsServiceImpl extends AbstractService<Integer,Breakdetails> implements BreakdetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for Breakdetails entities.
	 */
	 
	 private static final Logger log = LogManager.getLogger(BreakdetailsServiceImpl.class);
	 
	public BreakdetailsServiceImpl(GenericRepository<Breakdetails> repository) {
		super(repository,Breakdetails.class);
	}
	@Autowired
	private BreakdetailsRepository breakdetailsRepository;
	
	
  
	
	
	@Override
    public void bulkDelete(List<Integer> list) {
	   if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				breakdetailsRepository.deleteById(list.get(i));
			
			}
		}
    }
	
	
 /**
 * Creates a new vendor.
 *
 * @param breakdetails The breakdetails  object to create.
 * @return The created vendor object.
 */
	@Override
	public Breakdetails create(Breakdetails breakdetails)
	{
		log.info("inside @class BreakdetailsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
	    Integer workspaceId=customerInfo.getActiveUserSpaceId();
			breakdetails.setWorkspaceId(workspaceId); //done done
			return breakdetailsRepository.save(breakdetails);
	}
	
	
	

}
