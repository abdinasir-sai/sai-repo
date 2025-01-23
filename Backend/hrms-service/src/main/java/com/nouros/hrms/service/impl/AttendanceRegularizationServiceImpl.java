package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.AttendanceRegularization;
import com.nouros.hrms.repository.AttendanceRegularizationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AttendanceRegularizationService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "AttendanceRegularizationServiceImpl" which is located
 * in the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "AttendanceRegularizationService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * AttendanceRegularization and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * AttendanceRegularization AttendanceRegularization) for exporting the
 * AttendanceRegularization data into excel file by reading the template and
 * mapping the AttendanceRegularization details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class AttendanceRegularizationServiceImpl extends AbstractService<Integer,AttendanceRegularization>
		implements AttendanceRegularizationService {


	private static final Logger log = LogManager.getLogger(AttendanceRegularizationServiceImpl.class);
	
	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   AttendanceRegularization entities.
	 */

	public AttendanceRegularizationServiceImpl(GenericRepository<AttendanceRegularization> repository) {
		super(repository, AttendanceRegularization.class);
	}

	@Autowired
	private AttendanceRegularizationRepository attendanceRegularizationRepository;

	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				attendanceRegularizationRepository.deleteById(list.get(i));

			}
		}
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param attendanceRegularization The attendanceRegularization object to
	 *                                 create.
	 * @return The created vendor object.
	 */
	@Override
	public AttendanceRegularization create(AttendanceRegularization attendanceRegularization) {
		log.info("inside @class AttendanceRegularizationServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		attendanceRegularization.setWorkspaceId(workspaceId); // done done
		return attendanceRegularizationRepository.save(attendanceRegularization);
	}

}
