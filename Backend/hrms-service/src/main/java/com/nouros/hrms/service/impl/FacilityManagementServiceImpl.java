/* (C)2024 */
package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.FacilityManagement;
import com.nouros.hrms.repository.FacilityManagementRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.FacilityManagementService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class FacilityManagementServiceImpl extends AbstractService<Integer, FacilityManagement>
		implements FacilityManagementService {
	
	private static final Logger log = LogManager.getLogger(FacilityManagementServiceImpl.class);

	public FacilityManagementServiceImpl(GenericRepository<FacilityManagement> repository) {
		super(repository, FacilityManagement.class);
	}

	@Autowired
	private FacilityManagementRepository facilityManagementRepository;

	@Override
	public void softDelete(int id) {
		Optional<FacilityManagement> facilityManagement = facilityManagementRepository.findById(id);
		if (facilityManagement.isPresent()) {
			FacilityManagement facilityManagement1 = facilityManagement.get();
			facilityManagement1.setDeleted(true);
			facilityManagementRepository.save(facilityManagement1);
		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}
	}

	@Override
	public FacilityManagement create(FacilityManagement facilityManagement) {
		log.info("Inside @class FacilityManagementServiceImpl @method create");
		return facilityManagementRepository.save(facilityManagement);
	}
}
