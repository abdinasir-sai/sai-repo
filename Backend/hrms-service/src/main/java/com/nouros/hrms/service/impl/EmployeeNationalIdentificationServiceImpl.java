package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeNationalIdentificationService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeNationalIdentificationServiceImpl" which is
 * located in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeNationalIdentificationService" interface and
 * it extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeNationalIdentification and is used to call the superclass's
 * constructor. This class have one public method public byte[] export(List of
 * EmployeeNationalIdentification EmployeeNationalIdentification) for exporting
 * the EmployeeNationalIdentification data into excel file by reading the
 * template and mapping the EmployeeNationalIdentification details into it. It's
 * using Apache POI library for reading and writing excel files, and has methods
 * for parsing the json files for column names and identities , and it also used
 * 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class EmployeeNationalIdentificationServiceImpl extends AbstractService<Integer,EmployeeNationalIdentification>
		implements EmployeeNationalIdentificationService {

	private static final Logger log = LogManager.getLogger(EmployeeNationalIdentificationServiceImpl.class);

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeNationalIdentification entities.
	 */

	public EmployeeNationalIdentificationServiceImpl(GenericRepository<EmployeeNationalIdentification> repository) {
		super(repository, EmployeeNationalIdentification.class);
	}

	@Autowired
	private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;

	/**
	 * This method is responsible for soft-deleting an
	 * EmployeeNationalIdentification record in the database. The method takes in an
	 * int id which represents the id of the EmployeeNationalIdentification that
	 * needs to be soft-deleted. It uses the id to find the
	 * EmployeeNationalIdentification by calling the
	 * EmployeeNationalIdentificationRepository.findById method. If the
	 * EmployeeNationalIdentification is found, it sets the "deleted" field to true,
	 * save the EmployeeNationalIdentification in the repository, and saves it in
	 * the database
	 *
	 * @param id an int representing the id of the EmployeeNationalIdentification
	 *           that needs to be soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		EmployeeNationalIdentification employeeNationalIdentification = super.findById(id);

		if (employeeNationalIdentification != null) {

			EmployeeNationalIdentification employeeNationalIdentification1 = employeeNationalIdentification;

			employeeNationalIdentification1.setDeleted(true);
			employeeNationalIdentificationRepository.save(employeeNationalIdentification1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple
	 * EmployeeNationalIdentification records in the database in bulk. The method
	 * takes in a List of integers, each representing the id of an
	 * EmployeeNationalIdentification that needs to be soft-deleted. It iterates
	 * through the list, calling the softDelete method for each id passed in the
	 * list.
	 *
	 * @param list a List of integers representing the ids of the
	 *             EmployeeNationalIdentification that need to be soft-deleted
	 */
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	/**
	 * @param employeeNationalIdentification The employeeNationalIdentification
	 *                                       object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeNationalIdentification create(EmployeeNationalIdentification employeeNationalIdentification) {
		return employeeNationalIdentificationRepository.save(employeeNationalIdentification);
	}

	@Override
	public EmployeeNationalIdentification findNationalIdentificationDetailsOfEmployee(Integer id) {
		if (id != null) {
			log.info("Inside if to fetch Employee National Identification Details by employee Id ");
			return employeeNationalIdentificationRepository.findNationalIdentificationDetailsOfEmployee(id);
		}
		return null;
	}
	
	@Override
	public List<EmployeeNationalIdentification> findNationalIdentificationDetailsList(Integer id) {
		if (id != null) {
			log.info("Inside if to fetch List Of Employee National Identification Details by employee Id ");
			return employeeNationalIdentificationRepository.findNationalIdentificationDetailsList(id);
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmployeeNationalIdentification> getSelfEmployeeNationalIdentification(Integer id, Integer userId) {
		log.info("Inside Method getSelfEmployeeNationalIdentification");
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		Employee employee = employeeService.getEmployeeByIdOrUserId(id, userId);
		if (employee != null) {
			return findNationalIdentificationDetailsList(employee.getId());
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
	}

	@Override
	public List<EmployeeNationalIdentification> updateEmployeeNationalIdentification(
			List<EmployeeNationalIdentification> employeeNationalIdentificationList) {
		log.info("Inside Method updateEmployeeNationalIdentification");
		List<EmployeeNationalIdentification> updatedEmployeeNationalIdentification =  new ArrayList<>();
		for (EmployeeNationalIdentification employeeNationalIdentifications : employeeNationalIdentificationList) {
			EmployeeNationalIdentification employeeNationalIdentificationUpdated = employeeNationalIdentificationRepository.save(employeeNationalIdentifications);
			updatedEmployeeNationalIdentification.add(employeeNationalIdentificationUpdated);
				}
		return updatedEmployeeNationalIdentification;
	}
	
	@Override
	public EmployeeNationalIdentification getEmployeeNationalIdentificationByEmployeeId(Integer employeeId)
	{
		try
		{
			log.debug("Inisde EmployeeNationalIdentificationServiceImpl @method getEmployeeNationalIdentificationByEmployeeId employeeId :{} ",employeeId);
			List<EmployeeNationalIdentification> employeeNationalIdentificationList = employeeNationalIdentificationRepository.findNationalIdentificationDetailsList(employeeId);
			log.debug("List of EmployeeNationationalIdentificationList :{}",employeeNationalIdentificationList.size());
			EmployeeNationalIdentification employeeNationalIdentification = employeeNationalIdentificationList.get(0);
			log.debug(" EmployeeNationalIdentification Id :{} ",employeeNationalIdentification.getId());
			return employeeNationalIdentification;
		}
		catch(Exception e)
		{
			log.error("Error inside @class EmployeeNationalIdentificationServiceImpl @method getEmployeeNationalIdentificationByEmployeeId  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

}
