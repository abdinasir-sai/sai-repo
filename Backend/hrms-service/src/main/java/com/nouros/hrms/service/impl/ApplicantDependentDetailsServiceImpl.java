package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.ApplicantDependentDetails;
import com.nouros.hrms.repository.ApplicantDependentDetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantDependentDetailsService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class ApplicantDependentDetailsServiceImpl extends AbstractService<Integer,ApplicantDependentDetails>
implements ApplicantDependentDetailsService {
	
	public ApplicantDependentDetailsServiceImpl(GenericRepository<ApplicantDependentDetails> repository) {
		super(repository, ApplicantDependentDetails.class);
	}

	private static final Logger log = LogManager.getLogger(EmployeeDependentDetailsServiceImpl.class);

	@Autowired
	private ApplicantDependentDetailsRepository applicantDependentDetailsRepository;
	
	@Override
	public ApplicantDependentDetails create(ApplicantDependentDetails applicantDependentDetails) {
		return applicantDependentDetailsRepository.save(applicantDependentDetails);
	}

	@Override
	public void softDelete(int id) {
		
		ApplicantDependentDetails applicantDependentDetails = super.findById(id);

		if (applicantDependentDetails != null) {

			ApplicantDependentDetails applicantDependentDetails1 = applicantDependentDetails;

			applicantDependentDetails1.setDeleted(true);
			applicantDependentDetailsRepository.save(applicantDependentDetails1);

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
