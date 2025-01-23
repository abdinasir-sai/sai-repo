package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.repository.LeaveTypeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.LeaveTypeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "LeaveTypeServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "LeaveTypeService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository LeaveType and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of LeaveType
 * LeaveType) for exporting the LeaveType data into excel file by reading the
 * template and mapping the LeaveType details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class LeaveTypeServiceImpl extends AbstractService<Integer,LeaveType> implements LeaveTypeService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   LeaveType entities.
	 */

	private static final Logger log = LogManager.getLogger(LeaveTypeServiceImpl.class);

	public LeaveTypeServiceImpl(GenericRepository<LeaveType> repository) {
		super(repository, LeaveType.class);
	}

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param leaveType The leaveType object to create.
	 * @return The created vendor object.
	 */
	@Override
	public LeaveType create(LeaveType leaveType) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		leaveType.setWorkspaceId(workspaceId); // done done
		return leaveTypeRepository.save(leaveType);
	}

	@Override
	public List<LeaveType> findByDepartmentNameAndDesignationNameAndLocationName(String departmentName,
			String designationName, String locationName) {
		log.info("Inside Method findByDepartmentNameAndDesignationNameAndLocationName");
		if (departmentName != null && designationName != null && locationName != null) {
			log.debug(" Inside @findByDepartmentNameAndDesignationNameAndLocationName  customerId is : {}", commonUtils.getCustomerId());
			return leaveTypeRepository.findByDepartmentNameAndDesignationNameAndLocationName(departmentName,
					designationName, locationName);
		} else {
			throw new BusinessException("departmentName , designationName , locationName Can Not Be Null");
		}
	}

	@Override
	public LeaveType findByName(String name) {
		return leaveTypeRepository.findByNameAndCustomerId(name,commonUtils.getCustomerId());
	}

	@Override
	public List<LeaveType> findAll() {
		return leaveTypeRepository.findAll();
	}

}
