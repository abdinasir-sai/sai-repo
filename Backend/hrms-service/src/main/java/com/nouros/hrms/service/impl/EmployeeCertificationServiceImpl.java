
package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeCertification;
import com.nouros.hrms.repository.EmployeeCertificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeCertificationService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class EmployeeCertificationServiceImpl extends AbstractService<Integer, EmployeeCertification>
		implements EmployeeCertificationService {
	
	
	private static final Logger log = LogManager.getLogger(EmployeeCertificationServiceImpl.class);

	public EmployeeCertificationServiceImpl(GenericRepository<EmployeeCertification> repository) {
		super(repository, EmployeeCertification.class);
	}

	@Autowired
	private EmployeeCertificationRepository employeeCertificationRepository;

	@Override
	public void softDelete(int id) {
		Optional<EmployeeCertification> employeeCertification = employeeCertificationRepository.findById(id);
		if (employeeCertification.isPresent()) {
			EmployeeCertification employeeCertification1 = employeeCertification.get();
			employeeCertification1.setDeleted(true);
			employeeCertificationRepository.save(employeeCertification1);
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
	public EmployeeCertification create(EmployeeCertification employeeCertification) {
		log.info("Inside @class EmployeeCertificationServiceImpl @method create");
		return employeeCertificationRepository.save(employeeCertification);
	}
}
