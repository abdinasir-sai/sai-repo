package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.RiskTagsMapping;
import com.nouros.hrms.repository.RiskTagsMappingRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.RiskTagsMappingService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "RiskTagsMappingServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "RiskTagsMappingService" interface and it extends the
 * "AbstractService" class, which seems to be a generic class for handling CRUD
 * operations for entities. This class is annotated with @Service, indicating
 * that it is a Spring Service bean. This class is using Lombok's @Slf4j
 * annotation which will automatically generate an Slf4j based logger instance,
 * so it is using the Slf4j API for logging. The class has a constructor which
 * takes a single parameter of GenericRepository RiskTagsMapping and is used to
 * call the superclass's constructor. This class have one public method public
 * byte[] export(List of RiskTagsMapping RiskTagsMapping) for exporting the
 * RiskTagsMapping data into excel file by reading the template and mapping the
 * RiskTagsMapping details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class RiskTagsMappingServiceImpl extends AbstractService<Integer,RiskTagsMapping> implements RiskTagsMappingService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   RiskTagsMapping entities.
	 */

	private static final Logger log = LogManager.getLogger(RiskTagsMappingServiceImpl.class);

	public RiskTagsMappingServiceImpl(GenericRepository<RiskTagsMapping> repository) {
		super(repository, RiskTagsMapping.class);
	}

	@Autowired
	private RiskTagsMappingRepository riskTagsMappingRepository;

	/**
	 * This method is responsible for soft-deleting an RiskTagsMapping record in the
	 * database. The method takes in an int id which represents the id of the
	 * RiskTagsMapping that needs to be soft-deleted. It uses the id to find the
	 * RiskTagsMapping by calling the RiskTagsMappingRepository.findById method. If
	 * the RiskTagsMapping is found, it sets the "deleted" field to true, save the
	 * RiskTagsMapping in the repository, and saves it in the database
	 *
	 * @param id an int representing the id of the RiskTagsMapping that needs to be
	 *           soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		RiskTagsMapping riskTagsMapping = super.findById(id);

		if (riskTagsMapping != null) {

			RiskTagsMapping riskTagsMapping1 = riskTagsMapping;

			riskTagsMapping1.setDeleted(true);
			riskTagsMappingRepository.save(riskTagsMapping1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple RiskTagsMapping records
	 * in the database in bulk. The method takes in a List of integers, each
	 * representing the id of an RiskTagsMapping that needs to be soft-deleted. It
	 * iterates through the list, calling the softDelete method for each id passed
	 * in the list.
	 *
	 * @param list a List of integers representing the ids of the RiskTagsMapping
	 *             that need to be soft-deleted
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
	 * @param riskTagsMapping The riskTagsMapping object to create.
	 * @return The created vendor object.
	 */
	@Override
	public RiskTagsMapping create(RiskTagsMapping riskTagsMapping) {
		log.info("inside @class RiskTagsMappingServiceImpl @method create");
		return riskTagsMappingRepository.save(riskTagsMapping);
	}

}
