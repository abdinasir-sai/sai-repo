package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Competencies;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.repository.EmployeeCompetenciesDetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CompetenciesService;
import com.nouros.hrms.service.EmployeeCompetenciesDetailsService;
import com.nouros.hrms.service.EmployeeReviewService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import java.time.LocalDate;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeCompetenciesDetailsServiceImpl" which is
 * located in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeCompetenciesDetailsService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeCompetenciesDetails and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeCompetenciesDetails EmployeeCompetenciesDetails) for exporting the
 * EmployeeCompetenciesDetails data into excel file by reading the template and
 * mapping the EmployeeCompetenciesDetails details into it. It's using Apache
 * POI library for reading and writing excel files, and has methods for parsing
 * the json files for column names and identities , and it also used
 * 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class EmployeeCompetenciesDetailsServiceImpl extends AbstractService<Integer,EmployeeCompetenciesDetails>
		implements EmployeeCompetenciesDetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeCompetenciesDetails entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeCompetenciesDetailsServiceImpl.class);

	public EmployeeCompetenciesDetailsServiceImpl(GenericRepository<EmployeeCompetenciesDetails> repository) {
		super(repository, EmployeeCompetenciesDetails.class);
	}

	@Autowired
	private EmployeeCompetenciesDetailsRepository employeeCompetenciesDetailsRepository;
	
	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	  private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeCompetenciesDetails The employeeCompetenciesMapping object to
	 *                                    create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeCompetenciesDetails create(EmployeeCompetenciesDetails employeeCompetenciesDetails) {
		log.info("inside @class EmployeeCompetenciesDetailsServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeCompetenciesDetails.setWorkspaceId(workspaceId); // done done
		return employeeCompetenciesDetailsRepository.save(employeeCompetenciesDetails);
	}

	@Override
	public void createCompetenciesForEmployee(Employee employee,EmployeeReview employeeReview)
	 {
		try
		{
			log.info("Inside @class EmployeeCompetenciesDetailsServiceImpl @method createCompetenciesForEmployee");
			CompetenciesService competenciesService = ApplicationContextProvider.getApplicationContext().getBean(CompetenciesService.class);
			List<Competencies> listOfCompetencies = competenciesService.getCompetenciesList();
		
			for(Competencies competency : listOfCompetencies)
			{
				EmployeeCompetenciesDetails employeeCompetenciesDetails = new EmployeeCompetenciesDetails();
				employeeCompetenciesDetails.setCompetencies(competency);
				employeeCompetenciesDetails.setLabel(competency.getCompetencyName());
				employeeCompetenciesDetails.setEmployee(employee);
				employeeCompetenciesDetails.setEmployee2(employee.getReportingManager());
				employeeCompetenciesDetails.setEmployeeReview(employeeReview);
				employeeCompetenciesDetails = employeeCompetenciesDetailsRepository.save(employeeCompetenciesDetails);
			}
			log.info("Employee Competency Created ");
		}
		catch(Exception e)
		{
			log.debug("Error inside class EmployeeCompetenciesDetailsServiceImpl method createCompetenciesForEmployee :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
		 
	 }
	
	
	
	@Override
	public List<EmployeeCompetenciesDetails> getAllEmployeeCompetencies(Integer employeeReviewId)
	{
		try
		{
			log.info("Inside @class EmployeeGoalsDetailsServiceImpl @method getAllEmployeeGoals employeeReviewId :{} ",employeeReviewId);
			log.debug(" Inside @getAllEmployeeCompetencies  customerId is : {}", commonUtils.getCustomerId());
		   List<EmployeeCompetenciesDetails> listOfEmployeeCompetency = employeeCompetenciesDetailsRepository.getEmployeeCompetenciesDetailsByEmployeeReviewId(employeeReviewId,commonUtils.getCustomerId());
	        
			log.debug("Size of Employee Goal Detail List ",listOfEmployeeCompetency.size());
	         return listOfEmployeeCompetency;
		}
		catch(Exception e)
		{
			log.error("Error occured inside EmployeeGoalsDetailsServiceImpl @Method getAllEmployeeGoals :{} :{}", Utils.getStackTrace(e),
					e.getMessage());
			throw new BusinessException();
		}
	}

}
