package com.nouros.hrms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.RiskCustomField;
import com.nouros.hrms.repository.RiskCustomFieldRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.RiskCustomFieldService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "RiskCustomFieldServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "RiskCustomFieldService" interface and it extends the
 * "AbstractService" class, which seems to be a generic class for handling CRUD
 * operations for entities. This class is annotated with @Service, indicating
 * that it is a Spring Service bean. This class is using Lombok's @Slf4j
 * annotation which will automatically generate an Slf4j based logger instance,
 * so it is using the Slf4j API for logging. The class has a constructor which
 * takes a single parameter of GenericRepository RiskCustomField and is used to
 * call the superclass's constructor. This class have one public method public
 * byte[] export(List of RiskCustomField RiskCustomField) for exporting the
 * RiskCustomField data into excel file by reading the template and mapping the
 * RiskCustomField details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class RiskCustomFieldServiceImpl extends AbstractService<Integer,RiskCustomField> implements RiskCustomFieldService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   RiskCustomField entities.
	 */

    private static final Logger log = LogManager.getLogger(RiskCustomFieldServiceImpl.class);

	public RiskCustomFieldServiceImpl(GenericRepository<RiskCustomField> repository) {
		super(repository, RiskCustomField.class);
	}

	@Autowired
	private RiskCustomFieldRepository riskCustomFieldRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * @param riskCustomField The riskCustomField object to create.
	 * @return The created vendor object.
	 */
	@Override
	public RiskCustomField create(RiskCustomField riskCustomField) {
		log.info("inside @class RiskCustomFieldServiceImpl @method create");
		return riskCustomFieldRepository.save(riskCustomField);
	}

	@Override
	public List<RiskCustomField> findAllByEntityId(Integer id) {
		log.debug(" Inside @getWeightageConfigurationById  customerId is : {}", commonUtils.getCustomerId());
		return riskCustomFieldRepository.findAllByEntityId(id);
	}

}
