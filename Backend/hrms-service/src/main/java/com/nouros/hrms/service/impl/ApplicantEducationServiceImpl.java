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
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.repository.ApplicantEducationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantEducationService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

/**
 * This is a class named "ApplicantEducationServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "ApplicantEducationService" interface and it extends the
 * "AbstractService" class, which seems to be a generic class for handling CRUD
 * operations for entities. This class is annotated with @Service, indicating
 * that it is a Spring Service bean. This class is using Lombok's @Slf4j
 * annotation which will automatically generate an Slf4j based logger instance,
 * so it is using the Slf4j API for logging. The class has a constructor which
 * takes a single parameter of GenericRepository ApplicantEducation and is used
 * to call the superclass's constructor. This class have one public method
 * public byte[] export(List of ApplicantEducation ApplicantEducation) for
 * exporting the ApplicantEducation data into excel file by reading the template
 * and mapping the ApplicantEducation details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class ApplicantEducationServiceImpl extends AbstractService<Integer,ApplicantEducation>
		implements ApplicantEducationService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   ApplicantEducation entities.
	 */

	private static final Logger log = LogManager.getLogger(ApplicantEducationServiceImpl.class);

	public ApplicantEducationServiceImpl(GenericRepository<ApplicantEducation> repository) {
		super(repository, ApplicantEducation.class);
	}

	@Autowired
	private ApplicantEducationRepository applicantEducationRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				applicantEducationRepository.deleteById(list.get(i));

			}
		}
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param applicantEducation The applicantEducation object to create.
	 * @return The created vendor object.
	 */
	@Override
	public ApplicantEducation create(ApplicantEducation applicantEducation) {
		log.info("inside @class ApplicantEducationServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		applicantEducation.setWorkspaceId(workspaceId);
		return applicantEducationRepository.save(applicantEducation);
	}

	@Override
	public List<ApplicantEducation> createApplicationEducationfromPropt(
			List<Map<String, String>> applicantEducationList, Applicant applicant) {
		log.info("Inside ApplicantEducationServiceImpl @method setApplicantEducationFromPromptDetails");

		List<ApplicantEducation> savedApplicantEducations = new ArrayList<>();

		try {
			for (Map<String, String> educationMap : applicantEducationList) {
				ApplicantEducation applicantEducation = new ApplicantEducation();

//	            if (educationMap.containsKey("degreeStartDate")) {
//	                applicantEducation.setDegreeStartDate(parseDate(educationMap.get("degreeStartDate")));
//	            }

				if (educationMap.containsKey("degreeStartDate")) {

					Date date = (Date) commonUtils.parseDateInDayMonthYearFormat(educationMap.get("degreeStartDate"));
					log.debug("degreeStartDate is : {}", date);

					if (date != null) {
						applicantEducation.setDegreeStartDate(date);
					}
				}

				if (educationMap.containsKey("fieldOfStudy")) {
					applicantEducation.setFieldOfStudy(educationMap.get("fieldOfStudy"));
				}
				if (educationMap.containsKey("collegeName")) {
					applicantEducation.setSchoolName(educationMap.get("collegeName"));
					;
				}
//	            if (educationMap.containsKey("dateofCompletion")) {
//	                applicantEducation.setDateOfCompletion(parseDate(educationMap.get("dateofCompletion")));
//	            }

				if (educationMap.containsKey("dateofCompletion")) {

					Date date = (Date) commonUtils.parseDateInDayMonthYearFormat(educationMap.get("dateofCompletion"));
					log.debug("dateofCompletion is : {}", date);

					if (date != null) {
						applicantEducation.setDateOfCompletion(date);
					}
				}

				if (applicant != null) {
					applicantEducation.setApplicant(applicant);
				}
				log.info("Inside @method setApplicantEducationFromPromptDetails ApplicantEducation : {}",
						convertObjectToJson(applicantEducation));
				savedApplicantEducations.add(applicantEducationRepository.save(applicantEducation));

			}
		} catch (Exception e) {
			log.error("Error while parsing response JSON", e);
		}
		return savedApplicantEducations;
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
	public List<ApplicantEducation> updateApplicantEducationAfterResumeDetailsSet(
			List<ApplicantEducation> applicantEducationList, Applicant updatedApplicant) {

		log.debug("Inside method updateApplicantEducationAfterResumeDetailsSet applicantEducationList is : {}",
				applicantEducationList.toString());

		try {
			List<ApplicantEducation> savedApplicantEducations = new ArrayList<>();

			if (applicantEducationList != null && !applicantEducationList.isEmpty()) {
				for (ApplicantEducation education : applicantEducationList) {
					education.setApplicant(updatedApplicant);
					savedApplicantEducations.add(applicantEducationRepository.save(education));
				}
			}

			return savedApplicantEducations;

		} catch (Exception e) {
			throw new BusinessException("unable to update Applicant Education");
		}

	}

	@Override
	public List<ApplicantEducation> getEducationsForApplicant(Integer id) {
		log.info("Inside getEducationsForApplicant");
		try {
			log.debug("Inside getEducationsForApplicant customerId is : {}", commonUtils.getCustomerId());
			return applicantEducationRepository.getEducationsForApplicant(id, commonUtils.getCustomerId());
		} catch (Exception e) {
			log.error("An error occurred while getting ApplicantEducation  by applicant Id : {}", e.getMessage());
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
