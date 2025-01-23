package com.nouros.hrms.camunda.external.task;

import java.util.Optional;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Interview;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.InterviewService;
import com.nouros.hrms.service.JobApplicationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@ExternalTaskSubscription("ApplicantInterviewStage")
public class ApplicantInterviewStageUpdatationTask implements ExternalTaskHandler {

	private static final Logger log = LogManager.getLogger(ApplicantInterviewStageUpdatationTask.class);

	private static final long ONE_MINUTE = 1000L * 60;
	private static final int MAX_RETRIES = 4;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try { 
			log.info("inside @class ApplicantInterviewStage @method execute");
			Interview interView = getInterview(externalTask);
			log.debug("inside @class ApplicantInterviewStage @method execute interView {}",interView);
			ApplicantService applicationService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantService.class);
			Applicant applicantOptional = applicationService.findById(interView.getApplicant().getId());
			if(applicantOptional != null) {
			Applicant applicant = applicantOptional;
			updateApplicantStage(interView, applicationService, applicant);
			updateJobApplicationStage(interView, applicant);
			}
		} catch (Exception e) {
			Integer retries = this.getRetries(externalTask);
			Long timeout = this.getNextTimeout(retries);
			log.error("inside @class ApplicantInterviewStage @method execute exception ", e);
			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), retries, timeout);
		}

	}

	private void updateJobApplicationStage(Interview interView, Applicant applicant) {
		log.info("inside @class ApplicantInterviewStage @method updateJobApplicationStage");
		JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationService.class);
		JobApplication jobApplication= jobApplicationService.findByApplicantId(applicant.getId());
		jobApplication.setWorkflowStage(interView.getWorkflowStage());
		jobApplicationService.update(jobApplication);
	}

	private Applicant updateApplicantStage(Interview interView, ApplicantService applicationService,
			Applicant applicant) {
		log.info("inside @class ApplicantInterviewStage @method updateApplicantStage");
		applicant.setWorkflowStage(interView.getWorkflowStage());
		applicationService.update(applicant);
		return applicant;
	}

	private Interview getInterview(ExternalTask externalTask) {
		String processInstanceId = externalTask.getProcessInstanceId();
		InterviewService interViewService = ApplicationContextProvider.getApplicationContext()
				.getBean(InterviewService.class);
		log.info("inside @class ApplicantInterviewStage @method getInterview processInstanceId {}",processInstanceId);
		return interViewService.findByProcessInstanceId(processInstanceId);
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
