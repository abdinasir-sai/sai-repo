package com.nouros.hrms.camunda.external.task;

import java.util.Optional;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.Offers;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.service.OffersService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@ExternalTaskSubscription("ApplicantOfferStage")
public class ApplicantOfferStageUpdatationTask implements ExternalTaskHandler{

	private static final Logger log = LogManager.getLogger(ApplicantOfferStageUpdatationTask.class);
	private static final long ONE_MINUTE = 1000L * 60;
	private static final int MAX_RETRIES = 4;
	
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try {
			log.info("inside @class ApplicantOfferStageUpdatationTask @method execute");
			Offers offers=getOffers(externalTask);
			ApplicantService applicationService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantService.class);
			Applicant applicantOptional = applicationService.findById(offers.getApplicant().getId());
			if (applicantOptional != null) {
				Applicant applicant = applicantOptional;
				updateApplicantStage(offers, applicationService, applicant);
				updateJobApplicationStage(offers,applicant);
				log.info("inside @class ApplicantOfferStageUpdatationTask @method execute after update stage");
			}
			
		} catch (Exception e) {
			Integer retries = this.getRetries(externalTask);
			Long timeout = this.getNextTimeout(retries);
			log.error("inside @class ApplicantStageUpdatationTask @method execute exception ", e);
			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), retries, timeout);
		}
		
	}

	private void updateApplicantStage(Offers offers, ApplicantService applicationService, Applicant applicant) {
		log.info("inside @class ApplicantOfferStageUpdatationTask @method updateApplicantStage");
		applicant.setWorkflowStage(offers.getWorkflowStage());
		applicationService.update(applicant);
	}
	
	private void updateJobApplicationStage(Offers offers, Applicant applicant) {
		log.info("inside @class ApplicantOfferStageUpdatationTask @method updateJobApplicationStage");
		JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationService.class);
		JobApplication jobApplication= jobApplicationService.findByApplicantId(applicant.getId());
		jobApplication.setWorkflowStage(offers.getWorkflowStage());
		jobApplicationService.update(jobApplication);
	}

	private Offers getOffers(ExternalTask externalTask) {
		log.info("inside @class ApplicantOfferStageUpdatationTask @method getOffers");
		String processInstanceId = externalTask.getProcessInstanceId();
		OffersService offerService= ApplicationContextProvider.getApplicationContext().getBean(OffersService.class);
		return offerService.findByProcessInstanceId(processInstanceId);
	}
	
	private Integer getRetries(ExternalTask task) {
		Integer retries = task.getRetries();
		if (retries == null) {
			retries = MAX_RETRIES;
		} else {
			retries = retries - 1;
		}
		return retries;
	}

	private Long getNextTimeout(Integer retries) {
		return ONE_MINUTE * (MAX_RETRIES - retries);
	}

}
