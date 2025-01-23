/* (C)2024 */
package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeProfessionalLicence;
import com.nouros.hrms.repository.EmployeeProfessionalLicenceRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeProfessionalLicenceService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class EmployeeProfessionalLicenceServiceImpl extends AbstractService<Integer, EmployeeProfessionalLicence>
		implements EmployeeProfessionalLicenceService {
	
	private static final Logger log = LogManager.getLogger(EmployeeProfessionalLicenceServiceImpl.class);

	public EmployeeProfessionalLicenceServiceImpl(GenericRepository<EmployeeProfessionalLicence> repository) {
		super(repository, EmployeeProfessionalLicence.class);
	}

	@Autowired
	private EmployeeProfessionalLicenceRepository employeeProfessionalLicenceRepository;

	@Override
	public void softDelete(int id) {
		Optional<EmployeeProfessionalLicence> employeeProfessionalLicence = employeeProfessionalLicenceRepository
				.findById(id);
		if (employeeProfessionalLicence.isPresent()) {
			EmployeeProfessionalLicence employeeProfessionalLicence1 = employeeProfessionalLicence.get();
			employeeProfessionalLicence1.setDeleted(true);
			employeeProfessionalLicenceRepository.save(employeeProfessionalLicence1);
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
	public EmployeeProfessionalLicence create(EmployeeProfessionalLicence employeeProfessionalLicence) {
		log.info("Inside @class EmployeeProfessionalLicenceServiceImpl @method create");
		return employeeProfessionalLicenceRepository.save(employeeProfessionalLicence);
	}
}
