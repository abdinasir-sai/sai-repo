package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.repository.EmployeeComplianceLegalRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeComplianceLegalService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeComplianceLegalServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeComplianceLegalService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeComplianceLegal and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeComplianceLegal EmployeeComplianceLegal) for exporting the
 * EmployeeComplianceLegal data into excel file by reading the template and
 * mapping the EmployeeComplianceLegal details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeComplianceLegalServiceImpl extends AbstractService<Integer,EmployeeComplianceLegal>
		implements EmployeeComplianceLegalService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeComplianceLegal entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeComplianceLegalServiceImpl.class);

	public EmployeeComplianceLegalServiceImpl(GenericRepository<EmployeeComplianceLegal> repository) {
		super(repository, EmployeeComplianceLegal.class);
	}

	@Autowired
	private EmployeeComplianceLegalRepository employeeComplianceLegalRepository;

	/**
	 * This method is responsible for soft-deleting an EmployeeComplianceLegal
	 * record in the database. The method takes in an int id which represents the id
	 * of the EmployeeComplianceLegal that needs to be soft-deleted. It uses the id
	 * to find the EmployeeComplianceLegal by calling the
	 * EmployeeComplianceLegalRepository.findById method. If the
	 * EmployeeComplianceLegal is found, it sets the "deleted" field to true, save
	 * the EmployeeComplianceLegal in the repository, and saves it in the database
	 *
	 * @param id an int representing the id of the EmployeeComplianceLegal that
	 *           needs to be soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		EmployeeComplianceLegal employeeComplianceLegal = super.findById(id);

		if (employeeComplianceLegal != null) {

			EmployeeComplianceLegal employeeComplianceLegal1 = employeeComplianceLegal;

			employeeComplianceLegal1.setDeleted(true);
			employeeComplianceLegalRepository.save(employeeComplianceLegal1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple EmployeeComplianceLegal
	 * records in the database in bulk. The method takes in a List of integers, each
	 * representing the id of an EmployeeComplianceLegal that needs to be
	 * soft-deleted. It iterates through the list, calling the softDelete method for
	 * each id passed in the list.
	 *
	 * @param list a List of integers representing the ids of the
	 *             EmployeeComplianceLegal that need to be soft-deleted
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
	 * @param employeeComplianceLegal The employeeComplianceLegal object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeComplianceLegal create(EmployeeComplianceLegal employeeComplianceLegal) {
		return employeeComplianceLegalRepository.save(employeeComplianceLegal);
	}

	@Override
	public EmployeeComplianceLegal findComplianceLegalDetailsOfEmployee(Integer id) {
		if (id != null) {
			log.info("Inside if to fetch Employee Compliance Details by employee Id ");
			return employeeComplianceLegalRepository.findComplianceLegalDetailsOfEmployee(id);
		}
		return null;
	}
	
	@Override
	public EmployeeComplianceLegal getSelfEmployeeComplianceLegal(Integer id, Integer userId) {
		log.info("Inside Method getSelfEmployeeComplianceLegal");
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		Employee employee = employeeService.getEmployeeByIdOrUserId(id, userId);
		if (employee != null) {
			return findComplianceLegalDetailsOfEmployee(employee.getId());
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
	}

	@Override
	public EmployeeComplianceLegal updateEmployeeComplianceLegal(
			EmployeeComplianceLegal employeeComplianceLegal) {
		log.info("Inside Method updateEmployeeComplianceLegal");
		return  employeeComplianceLegalRepository.save(employeeComplianceLegal);
		}

}
