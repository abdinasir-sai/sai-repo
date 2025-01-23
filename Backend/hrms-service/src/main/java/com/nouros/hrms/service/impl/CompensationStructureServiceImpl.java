package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.repository.CompensationStructureRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CompensationStructureService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "CompensationStructureServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "CompensationStructureService" interface and it extends
 * the "AbstractService" class, which seems to be a generic class for handling
 * CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * CompensationStructure and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of
 * CompensationStructure CompensationStructure) for exporting the
 * CompensationStructure data into excel file by reading the template and
 * mapping the CompensationStructure details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class CompensationStructureServiceImpl extends AbstractService<Integer,CompensationStructure>
		implements CompensationStructureService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   CompensationStructure entities.
	 */

	private static final Logger log = LogManager.getLogger(CompensationStructureServiceImpl.class);

	public CompensationStructureServiceImpl(GenericRepository<CompensationStructure> repository) {
		super(repository, CompensationStructure.class);
	}

	@Autowired
	private CompensationStructureRepository compensationStructureRepository;


	/**
	 * Creates a new vendor.
	 *
	 * @param compensationStructure The compensationStructure object to create.
	 * @return The created vendor object.
	 */
	@Override
	public CompensationStructure create(CompensationStructure compensationStructure) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		compensationStructure.setWorkspaceId(workspaceId); // done done
		return compensationStructureRepository.save(compensationStructure);
	}

	@Override
	public List<CompensationStructure> findByDepartmentIdAndTitle(Integer departmentId, String title) {
		return compensationStructureRepository.findByDepartmentIdAndTitle(departmentId, title);
	}

	@Override
	public CompensationStructure findByTitle(String title) {
		try {
			log.info("Inside @Class CompensationStructureServiceImpl @Method findByTitle");
			log.debug("Inside @Class CompensationStructureServiceImpl @Method findByTitle title {}", title);
			CompensationStructure compStruc = compensationStructureRepository.findByTitle(title);
			if (!(compStruc instanceof CompensationStructure)) {
				throw new BusinessException("No data found for given title " + title);
			}
			return compStruc;
		} catch (Exception e) {
			String errorMessage = "An error occurred while finding by title inside CompensationStructureServiceImpl in findByTitle method";
		    throw new BusinessException(errorMessage, e);
		}
	}

}
