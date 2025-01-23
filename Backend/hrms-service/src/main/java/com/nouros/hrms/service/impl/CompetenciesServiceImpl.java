package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Competencies;
import com.nouros.hrms.repository.CompetenciesRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CompetenciesService;
import com.nouros.hrms.service.generic.AbstractService;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "CompetenciesServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "CompetenciesService" interface and it extends the "AbstractService"
 * class, which seems to be a generic class for handling CRUD operations for
 * entities. This class is annotated with @Service, indicating that it is a
 * Spring Service bean. This class is using Lombok's @Slf4j annotation which
 * will automatically generate an Slf4j based logger instance, so it is using
 * the Slf4j API for logging. The class has a constructor which takes a single
 * parameter of GenericRepository Competencies and is used to call the
 * superclass's constructor. This class have one public method public byte[]
 * export(List of Competencies Competencies) for exporting the Competencies data
 * into excel file by reading the template and mapping the Competencies details
 * into it. It's using Apache POI library for reading and writing excel files,
 * and has methods for parsing the json files for column names and identities ,
 * and it also used 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class CompetenciesServiceImpl extends AbstractService<Integer,Competencies> implements CompetenciesService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Competencies entities.
	 */

	 private static final Logger log = LogManager.getLogger(CompetenciesServiceImpl.class);

	public CompetenciesServiceImpl(GenericRepository<Competencies> repository) {
		super(repository, Competencies.class);
	}

	@Autowired
	private CompetenciesRepository competenciesRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param competencies The competencies object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Competencies create(Competencies competencies) {
		log.info("inside @class CompetenciesServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		competencies.setWorkspaceId(workspaceId); // done done
		return competenciesRepository.save(competencies);
	}

	@Override
	public	List<Competencies> getCompetenciesList()
	{
		try
		{
			log.info("inside @class CompetenciesServiceImpl @method getCompetenciesList ");
			List<Competencies> listOfCompetencies = competenciesRepository.findAll() ;
			log.debug("Size of Competency :{} ",listOfCompetencies.size());
			return listOfCompetencies;
		}
		catch(Exception e)
		{
			log.debug("Error inside class CompetenciesServiceImpl method getCompetenciesList :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}
}
