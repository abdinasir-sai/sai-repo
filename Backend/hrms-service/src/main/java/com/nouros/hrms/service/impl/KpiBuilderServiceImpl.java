package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.nouros.hrms.model.KpiBuilder;
import com.nouros.hrms.repository.KpiBuilderRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.KpiBuilderService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.KpiCardDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "KpiBuilderServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "KpiBuilderService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository KpiBuilder and is used to call the superclass's
 * constructor. This class have one public method public byte[] export(List of
 * KpiBuilder KpiBuilder) for exporting the KpiBuilder data into excel file by
 * reading the template and mapping the KpiBuilder details into it. It's using
 * Apache POI library for reading and writing excel files, and has methods for
 * parsing the json files for column names and identities , and it also used
 * 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class KpiBuilderServiceImpl extends AbstractService<Integer,KpiBuilder> implements KpiBuilderService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   KpiBuilder entities.
	 */

	private static final Logger log = LogManager.getLogger(KpiBuilderServiceImpl.class);

	public KpiBuilderServiceImpl(GenericRepository<KpiBuilder> repository) {
		super(repository, KpiBuilder.class);
	}

	@Autowired
	private KpiBuilderRepository kpiBuilderRepository;

	@PersistenceContext
	private EntityManager entityManager;


	/**
	 * Creates a new vendor.
	 *
	 * @param kpiBuilder The kpiBuilder object to create.
	 * @return The created vendor object.
	 */
	@Override
	public KpiBuilder create(KpiBuilder kpiBuilder) {
		return kpiBuilderRepository.save(kpiBuilder);
	}

	@Override
	public List<KpiCardDto> getKPICardByRole() {
		List<KpiCardDto> listOfKpi = new ArrayList<>();
		try {
			String roleName = "CEO";
			List<KpiBuilder> kpiList = kpiBuilderRepository.getKPIByRole(roleName);
			if (kpiList.isEmpty()) {
				throw new BusinessException("No insight's card found for user role " + roleName);
			}
			kpiList.forEach(k -> executeQuery(k, listOfKpi));
			return listOfKpi;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private void executeQuery(KpiBuilder k, List<KpiCardDto> listOfKpi) {
		KpiCardDto kpiCardDto = new KpiCardDto();
		kpiCardDto.setType(k.getVariable());
		Query query = entityManager.createNativeQuery(k.getQuery());
		Number result = ((Number) query.getSingleResult());
		if (null != result) {
			kpiCardDto.setCard(setCardData(k.getCardText(), result.longValue()));
		} else {
			kpiCardDto.setCard(setCardData(k.getCardText(), 0L));
		}
		listOfKpi.add(kpiCardDto);
		log.debug("Inside @Class KpiBuilderServiceImpl @Method executeQuery result:{}", result);
	}

	private String setCardData(String cardText, Long result) {
		return String.format(cardText, result);
	}
	
	

}
