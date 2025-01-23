package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantResumeController;
import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.service.ApplicantResumeService;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Primary
@RestController
@RequestMapping("/ApplicantResume")

public class ApplicantResumeControllerImpl  implements ApplicantResumeController {
	private static final Logger log = LogManager.getLogger(ApplicantResumeControllerImpl.class);
	
	
	  @Autowired
	  private ApplicantResumeService applicantResumeService;
	
	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE APPLICANT RESUME")
	public ApplicantResume create(@Valid ApplicantResume applicantResume) {
		log.info("inside @class ApplicantResumeControllerImpl @method create");
		return applicantResumeService.create(applicantResume);
	}

	@Override
	public Long count(String filter) {
		return applicantResumeService.count(filter);
	}

	@Override
	public List<ApplicantResume> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		 return applicantResumeService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE APPLICANT RESUME")
	public ApplicantResume update(@Valid ApplicantResume applicantResume) {
		return applicantResumeService.update(applicantResume);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE APPLICANT RESUME")
	public void deleteById(@Valid Integer id) {
		applicantResumeService.deleteById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE APPLICANT RESUME")
	public void bulkDelete(@Valid List<Integer> list) {
		applicantResumeService.bulkDelete(list);
	}

	@Override
	public ApplicantResume findById(@Valid Integer id) {
		return applicantResumeService.findById(id);
	}

	@Override
	public List<ApplicantResume> findAllById(@Valid List<Integer> id) {
		return applicantResumeService.findAllById(id);
	}

	@Override
	public List<ApplicantResume> findAllApplicantResumeByApplicantId(@Valid Integer applicantId) {
		return applicantResumeService.findAllApplicantResumeByApplicantId(applicantId);
	}

}
