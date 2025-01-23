package com.nouros.hrms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Cases;
import com.nouros.hrms.repository.CasesRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CasesService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "CasesServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "CasesService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Cases and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Cases Cases)
 * for exporting the Cases data into excel file by reading the template and
 * mapping the Cases details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class CasesServiceImpl extends AbstractService<Integer,Cases> implements CasesService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Cases entities.
	 */

	 private static final Logger log = LogManager.getLogger(CasesServiceImpl.class);

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	public CasesServiceImpl(GenericRepository<Cases> repository) {
		super(repository, Cases.class);
	}

	@Autowired
	private CasesRepository casesRepository;

	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				casesRepository.deleteById(list.get(i));

			}
		}
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param cases The cases object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Cases create(Cases cases) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("casesRuleHrms", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		cases.setCaseId(generatedName);
		cases.setWorkspaceId(workspaceId); // done done
		return casesRepository.save(cases);
	}

}
