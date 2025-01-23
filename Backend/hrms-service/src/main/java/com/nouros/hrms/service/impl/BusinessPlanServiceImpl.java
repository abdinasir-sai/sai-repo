package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.BusinessPlan;
import com.nouros.hrms.repository.BusinessPlanRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.BusinessPlanService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * This is a class named "BusinessPlanServiceImpl" which is located in the package " com.nouros.hrms.service.impl", It appears to be an implementation of the "BusinessPlanService" interface and it extends the "AbstractService" class, which seems to be a generic class for handling CRUD operations for entities. This class is annotated with @Service, indicating that it is a Spring Service bean.
This class is using Lombok's @Slf4j annotation which will automatically generate an Slf4j based logger instance, so it is using the Slf4j API for logging.
The class has a constructor which takes a single parameter of GenericRepository BusinessPlan and is used to call the superclass's constructor.
This class have one public method public byte[] export(List of BusinessPlan  BusinessPlan) for exporting the BusinessPlan data into excel file by reading the template and mapping the BusinessPlan details into it.
It's using Apache POI library for reading and writing excel files, and has methods for parsing the json files for column names and identities , and it also used 'ExcelUtils' for handling the excel operations.
It also uses 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and 'APIConstants' from 'com.nouros.hrms.util'
* */










@Service
public class BusinessPlanServiceImpl extends AbstractService<Integer,BusinessPlan> implements BusinessPlanService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for BusinessPlan entities.
	 */
	 
	private static final Logger log = LogManager.getLogger(BusinessPlanServiceImpl.class);
	 
	public BusinessPlanServiceImpl(GenericRepository<BusinessPlan> repository) {
		super(repository,BusinessPlan.class);
	}
	@Autowired
	private BusinessPlanRepository businessPlanRepository;
	
	@Autowired
	  private CommonUtils commonUtils;

	
	
	@Override
    public void bulkDelete(List<Integer> list) {
	   if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				businessPlanRepository.deleteById(list.get(i));
			
			}
		}
    }
	
	
 /**
 * Creates a new vendor.
 *
 * @param businessPlan The businessPlan  object to create.
 * @return The created vendor object.
 */
	@Override
	public BusinessPlan create(BusinessPlan businessPlan)
	{
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
	    Integer workspaceId=customerInfo.getActiveUserSpaceId();
			businessPlan.setWorkspaceId(workspaceId); //done done
			return businessPlanRepository.save(businessPlan);
	}


	@Override
	public BusinessPlan findByTitleAndMonth(String title,String planDate) {
		try {
			log.info("Inside @Class BusinessPlanServiceImpl @Method findByTitleAndMonth");
			log.debug("Inside @Class BusinessPlanServiceImpl @Method findByTitleAndMonth title {} planDate {},", title,planDate);
			log.debug("Inside @Class BusinessPlanServiceImpl findByTitleAndMonth customerId is : {}", commonUtils.getCustomerId());
			BusinessPlan businessPlan = businessPlanRepository.findByTitleAndMonth(title,planDate,commonUtils.getCustomerId());
			if (!(businessPlan instanceof BusinessPlan)) {
				throw new BusinessException("No data found for given title " + title);
			}
			return businessPlan;
		} catch (Exception e) {
			String errorMessage = "An error occurred while finding by title and month inside BusinessPlanServiceImpl in findByTitleAndMonth method";
			throw new BusinessException(errorMessage, e);

		}
	}
	
	
	
	

}
