package com.nouros.hrms.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.ApplicantReferral;
import com.nouros.hrms.repository.ApplicantReferralRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantReferralService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.PlannedOrgChartService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.JobOpeningWrapper;


@Service
public class ApplicantReferralServiceImpl extends AbstractService<Integer,ApplicantReferral> implements ApplicantReferralService{
	
	public ApplicantReferralServiceImpl(GenericRepository<ApplicantReferral> repository) {
		super(repository, ApplicantReferral.class);
	}
	
	@Autowired
	private ApplicantReferralRepository applicantReferralRepository;

	private static final Logger log = LogManager.getLogger(ApplicantReferralServiceImpl.class);

	@Override
	public ApplicantReferral create(ApplicantReferral applicantReferral) {
		log.info("inside @class ApplicantReferralServiceImpl @method create");
		
		JobOpeningService jobOpeningService = ApplicationContextProvider
				.getApplicationContext().getBean(JobOpeningService.class);
		
		JobOpeningWrapper jobOpeningWrapper = new JobOpeningWrapper();
		
		String fName = applicantReferral.getFirstName();
		String lName = applicantReferral.getLastName();
		
		String jobId = applicantReferral.getJobId();
		String email = applicantReferral.getEmailId();
		String fullName = null;
		if(fName != null && lName != null) {
			fullName = fName + " " + lName;
		}
		
		log.debug("inside @class ApplicantReferralServiceImpl @method create jobId is : {}",jobId);
		jobOpeningWrapper.setJobId(jobId);
		log.debug("inside @class ApplicantReferralServiceImpl @method create email is : {}",email);
		jobOpeningWrapper.setEmail(email);
		log.debug("inside @class ApplicantReferralServiceImpl @method create fullName is : {}",fullName);
		jobOpeningWrapper.setFullName(fullName);
		
		String applicantType = applicantReferral.getApplicantType();
		log.debug("inside @class ApplicantReferralServiceImpl @method create applicantType is : {}",applicantType);
		jobOpeningWrapper.setApplicantType(applicantType);
		
		
		if(jobId != null && email != null && fullName != null) {
		jobOpeningService.referApplicantForJobopening(jobOpeningWrapper);
		}
		
		return applicantReferralRepository.save(applicantReferral);
	}
	
	@Override
	public void softDelete(int id) {

		ApplicantReferral applicantReferral = super.findById(id);

		if (applicantReferral != null) {

			ApplicantReferral applicantReferral1 = applicantReferral;
			applicantReferralRepository.save(applicantReferral1);

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
