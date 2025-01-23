package com.nouros.hrms.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.obfuscate.ParseException;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.repository.ApplicantExperienceRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantExperienceService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

/**
 * This is a class named "ApplicantExperienceServiceImpl" which is located in
 * the package " com.nouros.hrms.service.impl", It appears to be an
 * implementation of the "ApplicantExperienceService" interface and it extends
 * the "AbstractService" class, which seems to be a generic class for handling
 * CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * ApplicantExperience and is used to call the superclass's constructor. This
 * class have one public method public byte[] export(List of ApplicantExperience
 * ApplicantExperience) for exporting the ApplicantExperience data into excel
 * file by reading the template and mapping the ApplicantExperience details into
 * it. It's using Apache POI library for reading and writing excel files, and
 * has methods for parsing the json files for column names and identities , and
 * it also used 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class ApplicantExperienceServiceImpl extends AbstractService<Integer,ApplicantExperience>
		implements ApplicantExperienceService {

	private static final String COMPANY = "company";
	private static final String SUMMARY = "summary";
	private static final String DURATION = "duration";
	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   ApplicantExperience entities.
	 */

	private static final Logger log = LogManager.getLogger(ApplicantExperienceServiceImpl.class);

	public ApplicantExperienceServiceImpl(GenericRepository<ApplicantExperience> repository) {
		super(repository, ApplicantExperience.class);
	}

	@Autowired
	private ApplicantExperienceRepository applicantExperienceRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				applicantExperienceRepository.deleteById(list.get(i));

			}
		}
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param applicantExperience The applicantExperience object to create.
	 * @return The created vendor object.
	 */
	@Override
	public ApplicantExperience create(ApplicantExperience applicantExperience) {
		log.info("inside @class ApplicantExperienceServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		applicantExperience.setWorkspaceId(workspaceId); // done done
		return applicantExperienceRepository.save(applicantExperience);
	}

	@Override
	public List<ApplicantExperience> createApplicantExperiencefromPrompt(
			List<Map<String, String>> applicantExperienceList, Applicant createdApplicant) {
		log.info("Inside ApplicantExperienceServiceImpl @method createApplicantExperiencefromPrompt");

		log.debug("Applicant Experience List applicantExperienceList : {}", applicantExperienceList);

		List<ApplicantExperience> savedApplicantExperiences = new ArrayList<>();

		try {
			for (Map<String, String> experienceMap : applicantExperienceList) {

				log.info("iterate for applicantExperienceList");

				ApplicantExperience applicantExperience = new ApplicantExperience();

				if (experienceMap.containsKey(COMPANY) && experienceMap.get(COMPANY) != null) {
					applicantExperience.setCompany(experienceMap.get(COMPANY));
				}
				if (experienceMap.containsKey("currentlyWorkHere")) {
					applicantExperience
							.setCurrentlyWorkHere(Boolean.parseBoolean(experienceMap.get("currentlyWorkHere")));
				}
				if (experienceMap.containsKey(DURATION)) {
					String durationValue = experienceMap.get(DURATION);
					if (durationValue != null) {
						applicantExperience.setDuration(experienceMap.get(DURATION));
					}
				}
				if (experienceMap.containsKey("occupation")) {
					applicantExperience.setOccupation(experienceMap.get("occupation"));
				}

				setWorkDateForApplicantExperience(experienceMap, applicantExperience);

				if (experienceMap.containsKey(SUMMARY)) {
					String summaryValue = experienceMap.get(SUMMARY);
					if (summaryValue != null) {
						applicantExperience.setSummary(experienceMap.get(SUMMARY));
					}
				}

				if (createdApplicant != null) {
					applicantExperience.setApplicant(createdApplicant);
				}
				log.debug("Applicant Experience is : {}", convertObjectToJson(applicantExperience));
				savedApplicantExperiences.add(applicantExperienceRepository.save(applicantExperience));
				log.debug("Applicant Experience set successfully savedApplicantExperiences : {}",
						savedApplicantExperiences);
			}
		} catch (Exception e) {
			log.error("Error while parsing response JSON for experiences", e);
		}

		return savedApplicantExperiences;
	}

	private void setWorkDateForApplicantExperience(Map<String, String> experienceMap,
			ApplicantExperience applicantExperience) {
		if (experienceMap.containsKey("startDate")) {

			Date date = (Date) commonUtils.parseDateInDayMonthYearFormat(experienceMap.get("startDate"));
			log.debug("startDate is : {}", date);

			if (date != null) {
				log.debug("set WorkStartDate is : {}", date);
				applicantExperience.setWorkStartDate(date);
			}
		}

		if (experienceMap.containsKey("endDate")) {

			Date date = (Date) commonUtils.parseDateInDayMonthYearFormat(experienceMap.get("endDate"));
			log.debug("endDate is : {}", date);

			if (date != null) {
				log.debug("set WorkEndDate is : {}", date);
				applicantExperience.setWorkEndDate(date);
			}
		}
	}

	private Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		try {
			return (Date) sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			log.error("Inside @Class ApplicantEducationServiceImpl error while parse the date:{}", e.getMessage());
		}
		return null;
	}

	@Override
	public List<ApplicantExperience> updateApplicantExperienceAfterResumeDetailsSet(
			List<ApplicantExperience> applicantExperienceList, Applicant updatedApplicant) {

		log.debug("Inside method updateApplicantExperienceAfterResumeDetailsSet applicantExperienceList is : {}",
				applicantExperienceList);
		try {
			List<ApplicantExperience> savedApplicantExperience = new ArrayList<>();

			if (applicantExperienceList != null && !applicantExperienceList.isEmpty()) {
				for (ApplicantExperience experience : applicantExperienceList) {
					experience.setApplicant(updatedApplicant);
					savedApplicantExperience.add(applicantExperienceRepository.save(experience));
				}
			}

			return savedApplicantExperience;
		} catch (Exception e) {

			log.error("error while updating applicant experience");
			throw new BusinessException("error while update applicant experience", e);
		}

	}

	@Override
	public List<ApplicantExperience> getExperienceForApplicant(Integer id) {
		log.info("Inside getExperienceForApplicant");
		try {
			log.debug("Inside getExperienceForApplicant customerId is : {}", commonUtils.getCustomerId());
			return applicantExperienceRepository.getExperienceForApplicant(id, commonUtils.getCustomerId());
		} catch (Exception e) {
			log.error("An error occurred while getting ApplicantExperience  by Applicant  Id : {}", e.getMessage());
			return null;
		}
	}

	private String convertObjectToJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Error while converting object to JSON", e);
			return "Error converting object to JSON";
		}
	}

}
