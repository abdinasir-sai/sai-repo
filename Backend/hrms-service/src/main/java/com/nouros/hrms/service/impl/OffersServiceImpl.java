package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.Offers;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.OffersRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.CompensationStructureService;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.OffersService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.OfferDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;
/**
 * This is a class named "OffersServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "OffersService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Offers and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Offers Offers)
 * for exporting the Offers data into excel file by reading the template and
 * mapping the Offers details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class OffersServiceImpl extends AbstractService<Integer,Offers> implements OffersService {
 
	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Offers entities. 
	 */

	private static final Logger log = LogManager.getLogger(OffersServiceImpl.class);

	public static final String SECURITY_EMAIL_TEMPLATE_NAME = "Candidate essential information";

	public OffersServiceImpl(GenericRepository<Offers> repository) {
		super(repository, Offers.class);
	}

	@Autowired
	private OffersRepository offersRepository;

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private JobOpeningService jobOpeningService;

	@Autowired
	private JobApplicationService jobApplicationService;

	@Autowired
	private CompensationStructureService compensationStructureService;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private NotificationIntegration notificationIntegration;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param offers The offers object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Offers create(Offers offers) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		offers.setWorkspaceId(workspaceId);
		return offersRepository.save(offers);
	}

	@Override
	public Offers findByProcessInstanceId(String processInstanceId) {
		try {
			log.debug(" Inside @findByProcessInstanceId  customerId is : {}", commonUtils.getCustomerId());
			return offersRepository.findByProcessInstanceId(processInstanceId);
		} catch (Exception e) {
			throw new BusinessException("No offer detail found for given processInstanceId " + processInstanceId);
		}

	}

	@Override
	public OfferDto getDataForOfferLetter(int applicantId, int jobOpeningId) {
		log.info("inside the @Class OffersServiceImpl @Method getDataForOfferLetter");
		Applicant optionalApplicant = applicantService.findById(applicantId);
	    if (optionalApplicant == null) {
	        log.error("Applicant not found with id: {}", applicantId);
	        throw new BusinessException("Applicant not found with id: " + applicantId);
	    }

	    JobOpening optionalJobOpening = jobOpeningService.findById(jobOpeningId);
	    if (optionalJobOpening == null) {
	        log.error("Job opening not found with id: {}", jobOpeningId);
	        throw new BusinessException("Job opening not found with id: " + jobOpeningId);
	    }

	    Applicant applicant = optionalApplicant;
	    JobOpening jobOpenings = optionalJobOpening;
		
		CompensationStructure compensationStructures = compensationStructureService
				.findByTitle(jobOpenings.getPostingTitle().getName());
		log.debug("inside the @Class OffersServiceImpl @Method getDataForOfferLetter customerId is : {}", commonUtils.getCustomerId());
		JobApplication jobApplication = jobApplicationRepository.findByApplicantIdAndJobOpeningId(applicantId,
				jobOpeningId, commonUtils.getCustomerId());
		OfferDto offerDto = new OfferDto();
		offerDto.setApplicant(applicant);
		offerDto.setConditionEmployment(
				"employee's medical fitness to work in the country, clear criminal checks, employee is granted with visa/work permit etc");
		offerDto.setDeadlineDate(getDeadlineDate());
		offerDto.setCompensationStructuresInsideAmount(compensationStructures.getAnnualBonusTo());
		offerDto.setJobOpening(jobOpenings);
		offerDto.setJobApplication(jobApplication);
		return offerDto;
	}

	@Override
	public String createJobOffer(OfferDto offerDto) {
		log.info("inside the @Class OffersServiceImpl @Method createJobOffer");
		Offers offers = new Offers();
		offers.setApplicant(offerDto.getApplicant());
		offers.setCompensationAmount(offerDto.getCompensationStructuresInsideAmount());
		offers.setDepartment(offerDto.getDepartment());
		offers.setEmploymentType(offerDto.getJobOpening().getJobType());
		offers.setJobOpening(offerDto.getJobOpening());
		offers.setOffersExpiry(getDeadlineDate());
		offers.setJobApplication(offerDto.getJobApplication());
		Offers offerRecived = offersRepository.save(offers);
		sendOfferEMail(offerRecived);
		return APIConstants.SUCCESS_JSON;
	}

	private Date getDeadlineDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		return calendar.getTime();
	}

	@Override
	public void sendOfferEMail(Offers offerRecived) {

		String securityFilePath = ConfigUtils.getPlainString("SECURITY_FILE_PATH");
		String securityFileName = ConfigUtils.getPlainString("SECURITY_FILE_NAME");
		log.info("SECURITY_FILE_NAME : {} ", securityFileName);
		log.info("SECURITY_FILE_PATH : {} ", securityFilePath);

		NotificationTemplate notificationTemplate = notificationIntegration.getTemplte("JOB OFFER");
		List<NotificationAttachment> notificationAttachmentList = new ArrayList<>();

		JSONObject payload = new JSONObject();
		String candidateName = getFullName(offerRecived.getApplicant().getFirstName(),
				offerRecived.getApplicant().getLastName());
		if (candidateName == null || candidateName.isEmpty()) {
	        candidateName = "";
	    }
		payload.put("candidateName", candidateName);
		payload.put("hiringManager", offerRecived.getJobOpening().getHiringManager());
		payload.put("position", offerRecived.getDesignation().getName());
		payload.put("compensationDetails", offerRecived.getCompensationAmount());
		payload.put("proposedStartDate", offerRecived.getOffersExpiry());
		payload.put("officeLocation", offerRecived.getJobOpening().getCity());
		NotificationAttachment notificationAttachment = new NotificationAttachment();
		notificationAttachment.setPath(securityFilePath);
		notificationAttachment.setName(securityFileName);
		notificationAttachmentList.add(notificationAttachment);
		try {
			notificationIntegration.sendEmail(notificationTemplate, payload, offerRecived.getApplicant().getEmailId(),
					"", notificationAttachmentList);
			offerRecived.setOfferEmailSent(true);
			offersRepository.save(offerRecived);
		} catch (Exception e) {
			offerRecived.setOfferEmailSent(false);
			offersRepository.save(offerRecived);
		}

	}

	public void sendSecurityEmail() {

		String securityFilePath = ConfigUtils.getPlainString("SECURITY_FILE_PATH");
		String securityFileName = ConfigUtils.getPlainString("SECURITY_FILE_NAME");

		log.info("Inside sendSecurityEmail in every minute");
		List<JobApplication> jobApplications = jobApplicationService.applicantByApplicationStatus("BGC initiated");

		log.info("Inside sendSecurityEmail jobApplications list size : {} ", jobApplications.size());

		NotificationTemplate notificationTemplate = notificationIntegration.getTemplte("testInfo");

		jobApplications.forEach(jobApplication -> {
			JSONObject payload = new JSONObject();
			String candidateName = getFullName(jobApplication.getApplicant().getFirstName(),
					jobApplication.getApplicant().getLastName());
			if (candidateName.isEmpty() || candidateName == null) {
				candidateName = "";
			}
			payload.put("candidateName", candidateName);
			payload.put("hiringManager", jobApplication.getJobOpening().getHiringManager());
			payload.put("position", jobApplication.getJobOpening().getPostingTitle());
			List<NotificationAttachment> notificationAttachmentList = new ArrayList<>();
			NotificationAttachment notificationAttachment = new NotificationAttachment();
			notificationAttachment.setPath(securityFilePath);
			notificationAttachment.setName(securityFileName);
			notificationAttachmentList.add(notificationAttachment);
			try {
				notificationIntegration.sendEmail(notificationTemplate, payload,
						jobApplication.getApplicant().getEmailId(), "", notificationAttachmentList);
				notificationIntegration.sendNotification(notificationTemplate, payload,
						jobApplication.getApplicant().getEmailId());
				jobApplication.setApplicationStatus("BGC Inprogress");
				jobApplicationService.create(jobApplication);
			} catch (Exception e) {
				log.error("Error Inside sendSecurityEmail : {} ", e.getMessage());
			}

		});
	}

	private String getFullName(String firstName, String lastName) {
		if (!firstName.isBlank() && !lastName.isBlank()) {
			return firstName + " " + lastName;
		}
		return firstName.isBlank() ? lastName : firstName;
	}

	@Override
	public void sendOfferEMail() {
		sendSecurityEmail();

	}

}
