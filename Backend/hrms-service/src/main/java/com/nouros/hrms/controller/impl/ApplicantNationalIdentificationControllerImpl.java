package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantNationalIdentificationController;
import com.nouros.hrms.model.ApplicantNationalIdentification;
import com.nouros.hrms.service.ApplicantNationalIdentificationService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/ApplicantNationalIdentification")
public class ApplicantNationalIdentificationControllerImpl implements ApplicantNationalIdentificationController {

	 private static final Logger log = LogManager.getLogger(ApplicantNationalIdentificationControllerImpl.class);

	  @Autowired
	  private ApplicantNationalIdentificationService applicantNationalIdentificationService;

	@Override
	public ApplicantNationalIdentification create(ApplicantNationalIdentification applicantNationalIdentification) {
		log.info("inside @class ApplicantNationalIdentificationControllerImpl @method create");
		return applicantNationalIdentificationService.create(applicantNationalIdentification);
	}

	@Override
	public Long count(String filter) {
		return applicantNationalIdentificationService.count(filter);
	}

	@Override
	public List<ApplicantNationalIdentification> search(String filter, @Valid Integer offset, @Valid Integer size,
			String orderBy, String orderType) {
		return applicantNationalIdentificationService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public ApplicantNationalIdentification update(ApplicantNationalIdentification applicantNationalIdentification) {
		return applicantNationalIdentificationService.update(applicantNationalIdentification);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		 applicantNationalIdentificationService.softDelete(id);
		
	}

	@Override
	public void bulkDelete(@Valid List<Integer> list) {
		applicantNationalIdentificationService.softBulkDelete(list);
		
	}

	@Override
	public ApplicantNationalIdentification findById(Integer id) {
		return applicantNationalIdentificationService.findById(id);
	}

	@Override
	public List<ApplicantNationalIdentification> findAllById(@Valid List<Integer> id) {
		return applicantNationalIdentificationService.findAllById(id);
	}
	  

		
	 

}
