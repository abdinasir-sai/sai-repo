/* (C)2024 */
package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeClearanceCertificate;
import com.nouros.hrms.repository.EmployeeClearanceCertificateRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeClearanceCertificateService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class EmployeeClearanceCertificateServiceImpl extends AbstractService<Integer, EmployeeClearanceCertificate>
		implements EmployeeClearanceCertificateService {
	
	private static final Logger log = LogManager.getLogger(EmployeeClearanceCertificateServiceImpl.class);

	public EmployeeClearanceCertificateServiceImpl(GenericRepository<EmployeeClearanceCertificate> repository) {
		super(repository, EmployeeClearanceCertificate.class);
	}

	@Autowired
	private EmployeeClearanceCertificateRepository employeeClearanceCertificateRepository;

	@Override
	public void softDelete(int id) {
		Optional<EmployeeClearanceCertificate> employeeClearanceCertificate = employeeClearanceCertificateRepository
				.findById(id);
		if (employeeClearanceCertificate.isPresent()) {
			EmployeeClearanceCertificate employeeClearanceCertificate1 = employeeClearanceCertificate.get();
			employeeClearanceCertificate1.setDeleted(true);
			employeeClearanceCertificateRepository.save(employeeClearanceCertificate1);
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
	public EmployeeClearanceCertificate create(EmployeeClearanceCertificate employeeClearanceCertificate) {
		log.info("Inside @class EmployeeClearanceCertificateServiceImpl @method create");
		return employeeClearanceCertificateRepository.save(employeeClearanceCertificate);
	}
}
