package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.ApplicantNationalIdentification;
import com.nouros.hrms.repository.ApplicantNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantNationalIdentificationService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class ApplicantNationalIdentificationServiceImpl extends AbstractService<Integer,ApplicantNationalIdentification> 
           implements ApplicantNationalIdentificationService {
	
	private static final Logger log = LogManager.getLogger(ApplicantNationalIdentificationServiceImpl.class);
	
	public ApplicantNationalIdentificationServiceImpl(GenericRepository<ApplicantNationalIdentification> repository) {
		super(repository, ApplicantNationalIdentification.class);
	}
   
	@Autowired
	private ApplicantNationalIdentificationRepository applicantNationalIdentificationRepository;

	@Override
	public void softDelete(int id) {
	
		ApplicantNationalIdentification applicantNationalIdentification = super.findById(id);

		if (applicantNationalIdentification != null) {

			ApplicantNationalIdentification applicantNationalIdentification1 = applicantNationalIdentification;

			applicantNationalIdentification1.setDeleted(true);
			applicantNationalIdentificationRepository.save(applicantNationalIdentification1);

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
	

}
