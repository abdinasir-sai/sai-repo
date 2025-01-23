package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.EmployeeMonthlyAttendance;
import com.nouros.hrms.repository.EmployeeMonthlyAttendanceRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeMonthlyAttendanceService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "EmployeeMonthlyAttendanceServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "EmployeeMonthlyAttendanceService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeMonthlyAttendance and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeMonthlyAttendance EmployeeMonthlyAttendance) for exporting the
 * EmployeeMonthlyAttendance data into excel file by reading the template and
 * mapping the EmployeeMonthlyAttendance details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeMonthlyAttendanceServiceImpl extends AbstractService<Integer,EmployeeMonthlyAttendance>
		implements EmployeeMonthlyAttendanceService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeMonthlyAttendance entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeMonthlyAttendanceServiceImpl.class);

	public EmployeeMonthlyAttendanceServiceImpl(GenericRepository<EmployeeMonthlyAttendance> repository) {
		super(repository, EmployeeMonthlyAttendance.class);
	}

	@Autowired
	private EmployeeMonthlyAttendanceRepository employeeMonthlyAttendanceRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeMonthlyAttendance The employeeMonthlyAttendance object to
	 *                                  create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeMonthlyAttendance create(EmployeeMonthlyAttendance employeeMonthlyAttendance) {
		log.info("inside @class EmployeeMonthlyAttendanceServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeMonthlyAttendance.setWorkspaceId(workspaceId); // done done
		return employeeMonthlyAttendanceRepository.save(employeeMonthlyAttendance);
	}

}
