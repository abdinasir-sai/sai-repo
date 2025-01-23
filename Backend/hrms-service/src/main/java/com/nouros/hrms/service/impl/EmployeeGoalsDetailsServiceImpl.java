package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.repository.EmployeeGoalsDetailsRepository;
import com.nouros.hrms.repository.EmployeeReviewRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeGoalsDetailsService;
import com.nouros.hrms.service.EmployeeReviewService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.GoalWrapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeGoalsMappingServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeGoalsMappingService" interface and it extends
 * the "AbstractService" class, which seems to be a generic class for handling
 * CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeGoalsMapping and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of
 * EmployeeGoalsMapping EmployeeGoalsMapping) for exporting the
 * EmployeeGoalsMapping data into excel file by reading the template and mapping
 * the EmployeeGoalsMapping details into it. It's using Apache POI library for
 * reading and writing excel files, and has methods for parsing the json files
 * for column names and identities , and it also used 'ExcelUtils' for handling
 * the excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeGoalsDetailsServiceImpl extends AbstractService<Integer,EmployeeGoalsDetails>
		implements EmployeeGoalsDetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeGoalsMapping entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeGoalsDetailsServiceImpl.class);

	public EmployeeGoalsDetailsServiceImpl(GenericRepository<EmployeeGoalsDetails> repository) {
		super(repository, EmployeeGoalsDetails.class);
	}

	@Autowired
	private EmployeeGoalsDetailsRepository employeeGoalsDetailsRepository;
	
	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	  private CommonUtils commonUtils;

	
	/**
	 * Creates a new vendor.
	 *
	 * @param employeeGoalsMapping The employeeGoalsMapping object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeGoalsDetails create(EmployeeGoalsDetails employeeGoalsDetails) {
		log.info("inside @class EmployeeGoalsDetailsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeGoalsDetails.setWorkspaceId(workspaceId); // done done
		return employeeGoalsDetailsRepository.save(employeeGoalsDetails);
	}
	
	@Override
	public List<EmployeeGoalsDetails> createEmployeeGoals(GoalWrapper goalWrapper)
	{
		try {
			log.info("Inside EmployeeGoalsDetailsServiceImpl @Method createEmployeeGoals  " );
			List<EmployeeGoalsDetails> listOfEmployeeGoalDetails = goalWrapper.getEmployeeGoalDetailList();
			Integer employeeReviewId = goalWrapper.getEmployeeReviewId();
			EmployeeReviewService employeeReviewService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeReviewService.class);
			EmployeeReview optionalEmployeeReview = employeeReviewService.findById(employeeReviewId);
			if(optionalEmployeeReview != null)
		    {
				List<EmployeeGoalsDetails> goalList = new ArrayList<>();
		    	EmployeeReview employeeReview = optionalEmployeeReview;
		    	Employee employee = employeeReview.getEmployee();
		    	Employee employeeManager = employeeReview.getEmployee2();
		    	for(EmployeeGoalsDetails employeeGoalsDetails :listOfEmployeeGoalDetails )
				{
					employeeGoalsDetails.setEmployeeReview(employeeReview);
					employeeGoalsDetails.setEmployee(employee);
					employeeGoalsDetails.setEmployee2(employeeManager);
					employeeGoalsDetails = employeeGoalsDetailsRepository.save(employeeGoalsDetails);
				  goalList.add(employeeGoalsDetails);
				}
				log.info("Data saved ");
				return goalList;
		    }
		    else
		    {
		    	log.info("Employee Review Not Present ");
		    	return null;
		    }
			
		}
		catch(Exception e)
		{
			log.error("Error occured inside EmployeeGoalsDetailsServiceImpl @Method createEmployeeGoals :{} :{}", Utils.getStackTrace(e),
					e.getMessage());
			throw new BusinessException();
		}
	}

	@Override
	public List<EmployeeGoalsDetails> getEmployeeGoalsDetailsByEmployeeReviewId(Integer employeeReviewId)
	{
		try
		{
			log.info("Inside @class EmployeeGoalsDetailsServiceImpl @method getEmployeeGoalsDetailsByEmployeeReviewId employeeReviewId :{} ",employeeReviewId);
			log.debug(" Inside @getEmployeeGoalsDetailsByEmployeeReviewId  customerId is : {}", commonUtils.getCustomerId());
		 		   List<EmployeeGoalsDetails> listOfEmployeeGoals = employeeGoalsDetailsRepository.getEmployeeGoalDetailsByEmployeeReviewId(employeeReviewId, commonUtils.getCustomerId());
	         log.debug("Size of Employee Goal Detail List ",listOfEmployeeGoals.size());
	         return listOfEmployeeGoals;
		}
		catch(Exception e)
		{
			log.error("Error occured inside EmployeeGoalsDetailsServiceImpl @Method getAllEmployeeGoals :{} :{}", Utils.getStackTrace(e),
					e.getMessage());
			throw new BusinessException();
		}
	}
	
	@Override 
	public String deleteEmployeeGoalDetailById(Integer id)
	 {
		try
		{
			log.debug("Inside @class EmployeeCompetenciesDetailsServiceImpl @method deleteEmployeeGoalDetailById Id :{}",id);
			employeeGoalsDetailsRepository.deleteById(id);
			log.info("The Data is Deleted ");
			return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e)
		{
			log.error("Error inside class EmployeeCompetenciesDetailsServiceImpl method deleteEmployeeGoalDetailById :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			return APIConstants.FAILURE_JSON;
		}
	 }
	
	@Override
	public String updateEmployeeGoalsDetails(List<EmployeeGoalsDetails> employeeGoalsDetailsList)
	{
		try {
			log.debug("Inside @class EmployeeCompetenciesDetails @method updateEmployeeGoalsDetailsByEmployeeReviewId size of EmployeeGoalsDetailsList  :{}",employeeGoalsDetailsList.size());
			 for(EmployeeGoalsDetails employeeGoalsDetails : employeeGoalsDetailsList)
			 {
				 log.debug("Inside updateEmployeeGoalsDetails :{} ",employeeGoalsDetails.getId());
				 employeeGoalsDetailsRepository.save(employeeGoalsDetails);
			 }
			 log.info("All Goals Updated ");
			 return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e)
		{
			log.error("Error inside class EmployeeCompetenciesDetailsServiceImpl method deleteEmployeeGoalDetailById :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
}

