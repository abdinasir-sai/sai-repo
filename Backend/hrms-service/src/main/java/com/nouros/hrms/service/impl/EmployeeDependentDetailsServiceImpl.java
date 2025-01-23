package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.repository.EmployeeDependentDetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeDependentDetailsService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeDependentDetailsServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeDependentDetailsService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeDependentDetails and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeDependentDetails EmployeeDependentDetails) for exporting the
 * EmployeeDependentDetails data into excel file by reading the template and
 * mapping the EmployeeDependentDetails details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeDependentDetailsServiceImpl extends AbstractService<Integer,EmployeeDependentDetails>
		implements EmployeeDependentDetailsService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeDependentDetails entities.
	 */

	public EmployeeDependentDetailsServiceImpl(GenericRepository<EmployeeDependentDetails> repository) {
		super(repository, EmployeeDependentDetails.class);
	}

	private static final Logger log = LogManager.getLogger(EmployeeDependentDetailsServiceImpl.class);

	@Autowired
	private EmployeeDependentDetailsRepository employeeDependentDetailsRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeDependentDetails The employeeDependentDetails object to
	 *                                 create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeDependentDetails create(EmployeeDependentDetails employeeDependentDetails) {
		
		employeeDependentDetails.setFullName(employeeDependentDetails.getFirstName() + " " + employeeDependentDetails.getLastName());
		log.debug("Inside mployeeDependentDetails fullname: {}",employeeDependentDetails.getFullName());
		return employeeDependentDetailsRepository.save(employeeDependentDetails);
	}

	
	@Override
	public EmployeeDependentDetails update(EmployeeDependentDetails employeeDependentDetails) {
		employeeDependentDetails.setFullName(employeeDependentDetails.getFirstName() + " " + employeeDependentDetails.getLastName());
		return employeeDependentDetailsRepository.save(employeeDependentDetails);
	}
	
	
	@Override
	public List<EmployeeDependentDetails> findDependentDetailsOfEmployee(Integer id) {
		if (id != null) {
			log.info("Inside if to fetch Employee Dependenct Details by employee Id ");
			return employeeDependentDetailsRepository.findDependentDetailsOfEmployee(id);
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmployeeDependentDetails> getSelfEmployeeDependentDetails(Integer id, Integer userId) {
		log.info("Inside Method getSelfEmployeeDependentDetails");
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		Employee employee = employeeService.getEmployeeByIdOrUserId(id, userId);
		if (employee != null) {
			return findDependentDetailsOfEmployee(employee.getId());
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
	}

	@Override
	public List<EmployeeDependentDetails> updateSelfEmployeeDependentDetails(
			List<EmployeeDependentDetails> employeeDependentDetailsList) {
		List<EmployeeDependentDetails> updateEmployeeDependentDetail =  new ArrayList<>();
		log.info("Inside Method updateSelfEmployeeDependentDetails");
		for (EmployeeDependentDetails employeeDependentDetails : employeeDependentDetailsList) {
			    employeeDependentDetails.setFullName(employeeDependentDetails.getFirstName() + " " + employeeDependentDetails.getLastName());
				EmployeeDependentDetails employeeDependentDetailsUpdated = employeeDependentDetailsRepository.save(employeeDependentDetails);
				updateEmployeeDependentDetail.add(employeeDependentDetailsUpdated);
				}
		return updateEmployeeDependentDetail;
		}


}
