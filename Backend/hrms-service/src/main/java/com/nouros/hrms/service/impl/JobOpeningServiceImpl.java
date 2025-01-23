package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.model.ApplicantReferral;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.repository.ApplicantReferralRepository;
import com.nouros.hrms.repository.ApplicantRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.JobOpeningRepository;
import com.nouros.hrms.repository.JobOpeningWeightageCriteriasRepository;
import com.nouros.hrms.repository.WeightageConfigurationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantCertificationsService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.PlannedOrgChartService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.JobOpeningActiveDto;
import com.nouros.hrms.wrapper.JobOpeningAndApplicantReferralDto;
import com.nouros.hrms.wrapper.JobOpeningDto;
import com.nouros.hrms.wrapper.JobOpeningIdDto;
import com.nouros.hrms.wrapper.JobOpeningSchedulerDto;
import com.nouros.hrms.wrapper.JobOpeningWrapper;
import com.nouros.hrms.wrapper.JobOpeningsDto;
import com.nouros.hrms.wrapper.JobOpeningsWrapper;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * This is a class named "JobOpeningServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "JobOpeningService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository JobOpening and is used to call the superclass's
 * constructor. This class have one public method public byte[] export(List of
 * JobOpening JobOpening) for exporting the JobOpening data into excel file by
 * reading the template and mapping the JobOpening details into it. It's using
 * Apache POI library for reading and writing excel files, and has methods for
 * parsing the json files for column names and identities , and it also used
 * 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class JobOpeningServiceImpl extends AbstractService<Integer, JobOpening> implements JobOpeningService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   JobOpening entities.
	 */

	private static final Logger log = LogManager.getLogger(JobOpeningServiceImpl.class);

	private static final String JOB_OPENING = "JobOpening";
	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";
	public static final String ROW_JSON_FORMED_IS = "rowJson formed is : ";
	public static final String SOMETHING_WENT_WRONG = "Something Went Wrong : {} ";
	private static final String RECRUITMENT_BASE_URL = "/auth/realms/BNTV/protocol/openid-connect/auth?client_id=recruitment&redirect_uri=https%3A%2F%2F";
	private static final String SYSTEM_CONFIGURATION = "systemConfiguration";
	private static final String DEPARTMENT = "department";
	private static final String POSTING_TITLE= "postingTitle";

	public JobOpeningServiceImpl(GenericRepository<JobOpening> repository) {
		super(repository, JobOpening.class);
	}

	@Value("${WEB_DOMAIN}")
	private String webDomain;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private UserRest userRest;

	@Autowired
	private JobOpeningRepository jobOpeningRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private NotificationIntegration notificationIntegration;

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ApplicantReferralRepository applicantReferralRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private WeightageConfigurationRepository weightageConfigurationRepository;

	@Autowired
	private JobOpeningWeightageCriteriasRepository jobOpeningWeightageCriteriasRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	/**
	 * This method is responsible for soft-deleting an JobOpening record in the
	 * database. The method takes in an int id which represents the id of the
	 * JobOpening that needs to be soft-deleted. It uses the id to find the
	 * JobOpening by calling the JobOpeningRepository.findById method. If the
	 * JobOpening is found, it sets the "deleted" field to true, save the JobOpening
	 * in the repository, and saves it in the database
	 *
	 * @param id an int representing the id of the JobOpening that needs to be
	 *           soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		JobOpening jobOpening = super.findById(id);

		if (jobOpening != null) {

			JobOpening jobOpening1 = jobOpening;

			jobOpening1.setDeleted(true);
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			Integer workspaceId = customerInfo.getActiveUserSpaceId();

			jobOpening1.setWorkspaceId(workspaceId); // done done
			jobOpeningRepository.save(jobOpening1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple JobOpening records in
	 * the database in bulk. The method takes in a List of integers, each
	 * representing the id of an JobOpening that needs to be soft-deleted. It
	 * iterates through the list, calling the softDelete method for each id passed
	 * in the list.
	 *
	 * @param list a List of integers representing the ids of the JobOpening that
	 *             need to be soft-deleted
	 */
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	@Override
	public JobOpening createWithNaming(JobOpening jobOpening) {
		log.debug("JobOpening Object to be created is  : {} ", jobOpening);
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		if (jobOpening.getDepartment().getName() != null || jobOpening.getCity() != null) {
			String department = transformValue(jobOpening.getDepartment().getName());
			log.debug("department for rule is : {} ", department);
			mp.put(DEPARTMENT, department);
			String city = transformCity(jobOpening.getCity());
			log.debug("City for rule is : {} ", city);
			mp.put("city", city);
		}
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("jobOpeningWithLocation", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		jobOpening.setJobId(generatedName);
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		jobOpening.setWorkspaceId(workspaceId); // done done
		JobOpening jobOpeningResponse = jobOpeningRepository.save(jobOpening);
//		JSONObject rowJson = setJobOpeningDataFromObjects(jobOpeningResponse);
//		log.debug("ROW_JSON_FORMED_IS : {} ", rowJson);
//		try {
//			try {
//				String response = vectorIntegrationService.createVectorAndInsert(rowJson, JOB_OPENING);
//				log.debug("Response from createVectorAndInsert is : {} ", response);
//			} catch (BusinessException be) {
//				log.error("Caught error for creating and inserting Vector : {} ", be.getMessage());
//			} catch (Exception e) {
//				log.error(SOMETHING_WENT_WRONG, e);
//			}
//			JobInterviewQuestionService jobInterviewQuestionService = ApplicationContextProvider.getApplicationContext()
//					.getBean(JobInterviewQuestionService.class);
//			String interviewQuestion = jobInterviewQuestionService.createJobInterviewQuestion(jobOpeningResponse);
//			log.debug("interviewQuestion from createJobInterviewQuestion is : {} ", interviewQuestion);
//		} catch (BusinessException be) {
//			log.error("Caught error for Creating Job Interview Questions : {} ", be.getMessage());
//		} catch (Exception e) {
//			log.error(SOMETHING_WENT_WRONG, e);
//		}
		return jobOpeningResponse;

	}

	private static String transformValue(String name) {
		String[] words = name.split(" ");
		StringBuilder transformedString = new StringBuilder();
		if (words.length > 1) {
			for (int i = 0; i < Math.min(words.length, 3); i++) {
				transformedString.append(words[i].charAt(0));
			}
		} else {
			transformedString.append(name.substring(0, Math.min(name.length(), 3)));
		}
		transformedString = new StringBuilder(transformedString.toString().toUpperCase());
		return transformedString.toString();
	}

	private static String transformCity(String cityName) {
		String modifiedString = cityName.replaceAll("\\s", "").toUpperCase();
		if (modifiedString.length() > 10) {
			modifiedString = modifiedString.substring(0, 10);
		}
		return modifiedString;
	}

	@Override
	public JSONObject setJobOpeningDataFromObjects(JobOpening jobOpening) {
		log.debug(INSIDE_METHOD, "setJobOpeningDataFromObjects");
		JSONObject jsonObject = new JSONObject();
		addValueIfNotNull(jsonObject, "id", jobOpening.getId());
		addValueIfNotNull(jsonObject, "jobId", jobOpening.getJobId());
		addValueIfNotNull(jsonObject, "city", jobOpening.getCity());
		addValueIfNotNull(jsonObject, "country", jobOpening.getCountry());
		addValueIfNotNull(jsonObject, POSTING_TITLE, jobOpening.getPostingTitle().getName());
		addValueIfNotNull(jsonObject, "workExperience", jobOpening.getWorkExperience());
		addValueIfNotNull(jsonObject, "fromSalary", jobOpening.getFromSalary());
		addValueIfNotNull(jsonObject, "province", jobOpening.getProvince());
		addValueIfNotNull(jsonObject, "departmentName", jobOpening.getDepartment().getName());
		addValueIfNotNull(jsonObject, "skills", jobOpening.getSkills());
		addValueIfNotNull(jsonObject, "industry", jobOpening.getIndustry());
		addValueIfNotNull(jsonObject, "descriptionRequirements", jobOpening.getDescriptionRequirements());
		addValueIfNotNull(jsonObject, "jobType", jobOpening.getJobType());
		addValueIfNotNull(jsonObject, "openPositions", jobOpening.getOpenPositions().toString());
		String searchString = createSearchString(jobOpening);
		addValueIfNotNull(jsonObject, "searchstring", searchString);
		log.debug("JSONObject formed is : {} ", jsonObject);
		return jsonObject;
	}

	private void addValueIfNotNull(JSONObject jsonObject, String key, Object value) {
		if (value != null) {
			jsonObject.put(key, value);
		} else {
			jsonObject.put(key, "");
		}
	}

	@Override
	public String createSearchString(JobOpening jobOpening) {
		log.debug("Inside Method createSearchString");
		List<String> values = new ArrayList<>();
		values.add(String.valueOf(jobOpening.getId()));
		values.add(jobOpening.getJobId());
		values.add(jobOpening.getPostingTitle().getName());
		values.add(String.valueOf(jobOpening.getWorkExperience()));
		values.add(String.valueOf(jobOpening.getOpenPositions()));
		values.add(jobOpening.getDepartment().getName());
		values.add(jobOpening.getSkills());
		values.add(jobOpening.getIndustry());
		values.add(jobOpening.getDescriptionRequirements());
		values.add(jobOpening.getJobType());
		values.add(jobOpening.getFromSalary());
		return String.join(",", values);
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param jobOpening The jobOpening object to create.
	 * @return The created vendor object.
	 */
	@Override
	public JobOpening create(JobOpening jobOpening) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		jobOpening.setWorkspaceId(workspaceId); // done done
		return jobOpeningRepository.save(jobOpening);
	}

	@Override
	public List<Applicant> findSuitableApplicantByJobOpeningId(Integer id) {
		log.info(INSIDE_METHOD, "findSuitableApplicantByJobOpeningId");
		JobOpening jobOpeningOne = new JobOpening();
		JSONArray arrayOfResult = null;
		List<Applicant> applicantList = new ArrayList<>();
		JobOpening jobOpening = super.findById(id);
		if (jobOpening != null) {
			jobOpeningOne = jobOpening;
		}
		String searchString = createSearchString(jobOpeningOne);
		try {
			String responseFromVectorSearch = vectorIntegrationService.searchByVector(searchString, "Applicant");
			log.debug("Response from Searching By Vector is : {} ", responseFromVectorSearch);
			JSONObject fullResponse = new JSONObject(responseFromVectorSearch);
			arrayOfResult = fullResponse.getJSONArray("result");
			log.debug("Result of Id's is : {} ", arrayOfResult);
			List<Integer> applicantIdList = new ArrayList<>();
			for (int i = 0; i < arrayOfResult.length(); i++) {
				JSONObject jsonObject = arrayOfResult.getJSONObject(i);
				if (jsonObject.getDouble("distance") < 1) {
					int applicantId = jsonObject.getInt("id");
					applicantIdList.add(applicantId);
				}
			}
			log.debug("Applicants Id's are : {} ", applicantIdList);
			ApplicantRepository applicantRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantRepository.class);
			applicantList = applicantRepository.findAllById(applicantIdList);
			return applicantList;
		} catch (BusinessException be) {
			log.error("Caught error Searching By Vector : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		return applicantList;
	}

	@Override
	public Integer getCountOfJobOpeningByDepartmentId(Integer departmentId) {
		log.info(INSIDE_METHOD, "getCountOfJobOpeningByDepartmentId");
		try {
			return jobOpeningRepository.getCountOfJobOpeningByDepartmentId(departmentId);
		} catch (Exception e) {
			log.error("An error occurred while getting the count of job openings by department ID: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public Integer getCountOfJobOpeningByDepartmentIds(List<Integer> departmentIds) {
		log.info(INSIDE_METHOD, "getCountOfJobOpeningByDepartmentId");
		try {
			return jobOpeningRepository.getCountOfJobOpeningByDepartmentIds(departmentIds);
		} catch (Exception e) {
			log.error("An error occurred while getting the count of job openings by department ID: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public JobOpening getJobOpeningByReferenceId(String jobOpeningId) {
		log.info(INSIDE_METHOD, "getJobOpeningByReferenceId");
		try {
			return jobOpeningRepository.getJobOpeningByReferenceId(jobOpeningId);
		} catch (Exception e) {
			log.error("An error occurred while getting job opening by job Opening Id : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public JobOpening findJobOpeningByUserLogin() {
		log.info(INSIDE_METHOD, "findJobOpeningByUserLogin");
		try {
			Map<String, String> attributeMap = userRest.getAttributeMap(customerInfo.getUsername());
			log.debug("attributeMap fetched is : {}", attributeMap);
			String jobOpeningId = attributeMap.get("jobType");
			log.debug("jobOpeningId fetched from attributeMap is : {}", jobOpeningId);
			JobOpening jobOpening = getJobOpeningByReferenceId(jobOpeningId);
			log.debug("jobOpening fetched  through jobOpeningId is : {}", jobOpening);
			return jobOpening;
		} catch (Exception e) {
			log.error("An error occurred while find job opening by user login : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public List<JobOpeningDto> getJobOpeningByActiveWorkFlowStage() {
		log.info(INSIDE_METHOD, "getJobOpeningByActiveWorkFlowStage");

		List<JobOpeningDto> jobOpeningDtos = new ArrayList<>();
		try {
			List<JobOpening> list = jobOpeningRepository.getJobOpeningByWorkflowStage("Active");
			log.debug("list of job opening is : {}", list);
			for (JobOpening jobOpening : list) {
				JobOpeningDto dto = new JobOpeningDto();
				dto.setIndustry(jobOpening.getIndustry());
				dto.setJobId(jobOpening.getJobId());
				dto.setJobType(jobOpening.getJobType());
				dto.setJobOpeningStatus(jobOpening.getJobOpeningStatus());
				jobOpeningDtos.add(dto);
			}
		} catch (Exception e) {
			log.error("An error occurred while fetching job openings by active workflow stage", e.getMessage());
		}
		log.debug("final list of job opening is : {}", jobOpeningDtos);
		return jobOpeningDtos;

	}

	@Override
	public String referApplicantForJobopening(JobOpeningWrapper jobOpeningWrapper) {
		log.info(INSIDE_METHOD, "referApplicantForJobopening");
		try {
			User contextUser = getUserContext();
			log.debug("Referral information is referredByFullName {},referredEmail: {} ", contextUser.getFullName(),
					contextUser.getEmail());
			if (contextUser != null && contextUser.getFullName() != null && contextUser.getEmail() != null) {
				JSONObject notificationData = createCompletionNotificationData(jobOpeningWrapper);
				NotificationTemplate template = notificationIntegration.getTemplte("Applicant_refferal");
				notificationIntegration.sendEmail(template, notificationData, jobOpeningWrapper.getEmail(), null,
						Collections.emptyList());
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonResponse = objectMapper.writeValueAsString(jobOpeningWrapper);
				return jsonResponse;
			} else {
				return APIConstants.FAILURE_JSON;
			}
		} catch (BusinessException ex) {
			log.error("Error while refer to Applicant for Jobopening", ex.getMessage());
			throw new BusinessException(ex.getMessage());
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private JSONObject createCompletionNotificationData(JobOpeningWrapper jobOpeningWrapper) {
		log.info("inside @class Jobopeningserviceimpl @method createCompletionNotificationData");
		JSONObject object = new JSONObject();
		User contextUser = getUserContext();
		JobOpening jobOpening = jobOpeningRepository.getJobOpeningByReferenceId(jobOpeningWrapper.getJobId());
		log.debug("JobOpening by job id is : {} ", jobOpening);
		if (jobOpening.getJobId() != null) {

			if (jobOpeningWrapper != null && jobOpeningWrapper.getFullName() != null) {
				object.put("recipient", jobOpeningWrapper.getFullName());
			} else {
				log.error("Recipient FullName cannot be null");
				throw new BusinessException("Recipient FullName cannot be null");
			}

			if (jobOpening != null && jobOpening.getPostingTitle() != null
					&& jobOpening.getPostingTitle().getName() != null) {
				object.put("positionTitle", jobOpening.getPostingTitle().getName());
			} else {
				log.error("PostingTitle cannot be null");
				throw new BusinessException("PostingTitle cannot be null");
			}
			if (contextUser != null && contextUser.getFullName() != null) {
				object.put("senderFullName", contextUser.getFullName());
			} else {
				log.error("Sender FullName cannot be null");
				throw new BusinessException("Sender FullName cannot be null");
			}

			String link;

			if (jobOpeningWrapper.getApplicantType() != null
					&& jobOpeningWrapper.getApplicantType().equalsIgnoreCase("Head-Hunted")) {
				log.info("for Head-Hunted case");
				link = buildUrlForHeadHuntedCase(jobOpening.getJobId());
			} else {
				log.info("for normal/referral case");
				link = buildUrlForReferedCase(jobOpening.getJobId());
			}
			log.debug("link formed is : {} ", link);
			String clickableLink = "<a href=\"" + link + "\">Click here to complete your registration</a>";

			object.put("link", clickableLink);

			return object;
		} else {
			log.error("JobId cannot be null");
			throw new BusinessException("JobId cannot be null");
		}
	}

	public String buildUrlForReferedCase(String jobId) {
		log.info("inside @class Jobopeningserviceimpl @method buildUrl");
//		return "https://" + webDomain + RECRUITMENT_BASE_URL + webDomain
//				+ "%2Frecruitment%2F&state=d28440e4-6786-4431-9172-addf09034b2c&response_mode=fragment&response_type=code&scope=openid&nonce=d603ecb7-7824-4467-8945-b743ba10040b&jobType="
//				+ jobId;

		User contextUser = getUserContext();

		String referedByEmailId = contextUser.getEmail();
		log.debug("Email by login user is : {}", referedByEmailId);

		String referedBy = "referral";

		return "https://" + webDomain + "/recruitment?&jobType=" + jobId + "&referedBy=" + referedBy
				+ "&referedByEmailId=" + referedByEmailId;
	}

	public String buildUrlForHeadHuntedCase(String jobId) {
		log.info("inside @class Jobopeningserviceimpl @method buildUrl");
		User contextUser = getUserContext();

		String referedByEmailId = contextUser.getEmail();
		log.debug("Email by login user is : {}", referedByEmailId);

		String referedBy = "Head-Hunted";

		return "https://" + webDomain + "/recruitment?&jobType=" + jobId + "&referedBy=" + referedBy
				+ "&referedByEmailId=" + referedByEmailId;
	}

	@Override
	public JobOpeningSchedulerDto getActiveJobOpening() {
		log.info("inside @class Jobopeningserviceimpl @method getActiveJobOpening");
		JobOpeningSchedulerDto jobOpeningSchedulerDto = new JobOpeningSchedulerDto();
		List<JobOpeningsWrapper> jobOpeningsWrapperList = new ArrayList<>();
		try {
			List<JobOpening> list = jobOpeningRepository.getActiveAndCareerSiteJobOpenings();
			log.debug("list of job opening is : {}", list);
			if (list != null && !list.isEmpty()) {
				for (JobOpening jobOpening : list) {
					setJobOpeningDtoFields(jobOpeningsWrapperList, jobOpening);
				}
				jobOpeningSchedulerDto.setJobOpenings(jobOpeningsWrapperList);
			}
			return jobOpeningSchedulerDto;
		} catch (Exception e) {
			log.error("An error occurred while fetching job openings by active Status", e.getMessage());
		}
		return null;
	}

	@Override
	public JobOpeningSchedulerDto getActiveJobOpeningByDepartment(Integer departmentId) {
		log.info("inside @class Jobopeningserviceimpl @method getActiveJobOpeningByDepartment");
		JobOpeningSchedulerDto jobOpeningSchedulerDto = new JobOpeningSchedulerDto();
		List<JobOpeningsWrapper> jobOpeningsWrapperList = new ArrayList<>();
		try {
			List<JobOpening> list = jobOpeningRepository.getActiveJobOpeningByDepartment(departmentId);
			log.debug("list of job opening  for Department is : {}", list);
			if (list != null && !list.isEmpty()) {
				for (JobOpening jobOpening : list) {
					setJobOpeningDtoFields(jobOpeningsWrapperList, jobOpening);
				}
				jobOpeningSchedulerDto.setJobOpenings(jobOpeningsWrapperList);
			}
			return jobOpeningSchedulerDto;
		} catch (Exception e) {
			log.error("An error occurred while fetching job openings for Department by active Status ", e.getMessage());
		}
		return null;
	}

	/**
	 * @param jobOpeningsWrapperList
	 * @param jobOpening
	 */
	private void setJobOpeningDtoFields(List<JobOpeningsWrapper> jobOpeningsWrapperList, JobOpening jobOpening) {
		log.info("inside @class Jobopeningserviceimpl @method setJobOpeningDtoFields");
		JobOpeningsWrapper jobOpeningsWrapper = new JobOpeningsWrapper();
		if (jobOpening != null && jobOpening.getId() != null) {
			jobOpeningsWrapper.setId(jobOpening.getId());
		}
		if (jobOpening != null && jobOpening.getAddress() != null) {
			jobOpeningsWrapper.setLocation(jobOpening.getAddress());
		}
		if (jobOpening != null && jobOpening.getCity() != null) {
			jobOpeningsWrapper.setLocation(jobOpening.getCity());
		}
		if (jobOpening != null && jobOpening.getJobType() != null) {
			jobOpeningsWrapper.setEmploymentType(jobOpening.getJobType());
		}
		if (jobOpening != null && jobOpening.getPostingTitle() != null
				&& jobOpening.getPostingTitle().getName() != null) {
			jobOpeningsWrapper.setTitle(jobOpening.getPostingTitle().getName());
		}
		if (jobOpening != null && jobOpening.getDepartment() != null && jobOpening.getDepartment().getName() != null) {
			jobOpeningsWrapper.setDepartment(jobOpening.getDepartment().getName());
		}
		if (jobOpening != null && jobOpening.getDescriptionRequirements() != null) {
			jobOpeningsWrapper.setAbout(jobOpening.getDescriptionRequirements());
		}
		setJobOpeningSkillsInDto(jobOpening, jobOpeningsWrapper);
		if (jobOpening != null && jobOpening.getCoreCapabilities() != null) {
			jobOpeningsWrapper.setPreferredQualifications(jobOpening.getCoreCapabilities());
		}
		if (jobOpening != null && jobOpening.getResponsibilities() != null) {
			jobOpeningsWrapper.setResponsibilities(jobOpening.getResponsibilities());
		}
		if (jobOpening != null && jobOpening.getJobId() != null) {
			jobOpeningsWrapper.setJobId(jobOpening.getJobId());
		}
		jobOpeningsWrapperList.add(jobOpeningsWrapper);
	}

	private void setJobOpeningSkillsInDto(JobOpening jobOpening, JobOpeningsWrapper jobOpeningsWrapper) {
		if (jobOpening != null && jobOpening.getSkills() != null && jobOpening.getExperienceAndEducation() != null) {
			jobOpeningsWrapper.setRequirements(jobOpening.getSkills() + " " + jobOpening.getExperienceAndEducation());
		} else if (jobOpening != null && jobOpening.getSkills() == null
				&& jobOpening.getExperienceAndEducation() != null) {
			jobOpeningsWrapper.setRequirements(jobOpening.getExperienceAndEducation());
		} else if (jobOpening != null && jobOpening.getSkills() != null
				&& jobOpening.getExperienceAndEducation() == null) {
			jobOpeningsWrapper.setRequirements(jobOpening.getSkills());
		}
	}

	@Override
	public JobOpeningsDto getApplicantCountByActiveJobOpening(String isCritical, String departmentName,
			String postingTitleName, String postingTitleJobLevel) {
		log.info("inside @class Jobopeningserviceimpl @method getApplicantCountByActiveJobOpening");
		Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
		String systemConfiguration = hrmsSystemConfigMap.get(SYSTEM_CONFIGURATION);
		log.debug("inside @class Jobopeningserviceimpl systemConfiguration: {}", systemConfiguration);
		String[] configValues =null;
		if (systemConfiguration != null && !systemConfiguration.isEmpty()) {
		    configValues = systemConfiguration.split(",");
		    log.debug("configValues of designations are : {}",configValues);
		  }
		User contextUser = getUserContext();
		log.debug("contextUser user Id is : {}", contextUser.getUserid());
		Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
		log.debug("inside @class Jobopeningserviceimpl employee: {}",employee);
		JobOpeningsDto jobOpeningsDto = new JobOpeningsDto();
		List<JobOpening> list = new ArrayList<>();
		List<JobOpeningActiveDto> jobOpeningActiveDtoList = new ArrayList<>();
		try {
			if (isCritical == null && postingTitleName == null && departmentName == null
					&& postingTitleJobLevel == null) {
				log.debug("inside if condition when filter not apply , default case ");
				list = fetchJobOpeningListBasedOnConditions(configValues, contextUser, employee, list);	
			} else if (departmentName != null && departmentName.equalsIgnoreCase("null") && isCritical != null && isCritical.equalsIgnoreCase("null")
					&& postingTitleName.equalsIgnoreCase("null") && postingTitleJobLevel.equalsIgnoreCase("null")) {
				log.debug("inside else if condition when filter not apply , but null keys are added case ");
				list = fetchJobOpeningListBasedOnConditions(configValues, contextUser, employee, list);	
			} else {
				log.debug("inside else condition when parameters is Filter apply ");
				list = getFilteredJobOpenings(isCritical, departmentName, postingTitleName, postingTitleJobLevel);
			}
			log.debug("list of Active Job Opening is : {}", list);
			if (list != null && !list.isEmpty()) {
				for (JobOpening jobOpening : list) {
					setJobOpeningsDtoFields(jobOpeningActiveDtoList, jobOpening);
				}
				jobOpeningsDto.setJobOpenings(jobOpeningActiveDtoList);
				log.debug("size of Active Job Opening is : {}", list.size());
				jobOpeningsDto.setJobOpeningCount(list.size());
			}
			return jobOpeningsDto;
		} catch (BusinessException e) {
			throw new BusinessException("error while getting Application Count by Active JobOpening", e.getMessage());
		}
	}

	/**
	 * @param configValues
	 * @param contextUser
	 * @param employee
	 * @param list
	 * @return
	 */
	private List<JobOpening> fetchJobOpeningListBasedOnConditions(String[] configValues, User contextUser,
			Employee employee, List<JobOpening> list) {
		if(employee != null && employee.getDesignation() != null) {
			String designationName = employee.getDesignation().getName();
			log.debug("designationName : {}",designationName);
			boolean match = false;
			for (String configValue : configValues) {
				if (designationName != null && designationName.equalsIgnoreCase(configValue)) {
					match = true;
					list = jobOpeningRepository.getActiveJobOpening();
					break;
				}
			}
			log.debug("flag value match after system Config Designations : {}",match);
			
			if(! match && contextUser.getOrganisationRole() != null) {
				log.debug("context user Organization role : {}",contextUser.getOrganisationRole().getId());
				String orgRoleName= contextUser.getOrganisationRole().getRoleName();
				log.debug("context user Organization role name: {}",orgRoleName );
				if(orgRoleName.equalsIgnoreCase("head hunter")){
					list= jobOpeningRepository.getHeadHuntedEnabledAndActiveJobOpening(commonUtils.getCustomerId());
					if(list!=null && list.size()>1 && !list.isEmpty()){
						match = true;
						}
				}
			}
			log.debug("flag value match after head hunter condition : {}",match);
			if(! match) {
				Integer userId = contextUser.getUserid();
				log.debug("context user id : {}",userId);
				list= jobOpeningRepository.findByHiringManagerUserId(userId);
			}
		}
		return list;
	}

	@Override
	public List<JobOpening> getFilteredJobOpenings(String isCritical, String departmentName, String postingTitleName,
			String postingTitleJobLevel) {
		log.info("inside @class Jobopeningserviceimpl @method getFilteredJobOpenings");

		jakarta.persistence.criteria.CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<JobOpening> query = cb.createQuery(JobOpening.class);
		Root<JobOpening> jobOpening = query.from(JobOpening.class);

		List<Predicate> predicates = new ArrayList<>();
		log.info("inside @method getFilteredJobOpenings Apply filter for Active job openings");
		Predicate activeStatusPredicate = cb.equal(cb.lower(jobOpening.get("jobOpeningStatus")), "active");

		// Filter by department.name (like filter), combined with Active status filter
		if (departmentName != null && !departmentName.equalsIgnoreCase("null")) {
			log.info("inside @method getFilteredJobOpenings Apply filter on departmentName");
			Join<JobOpening, Department> department = jobOpening.join(DEPARTMENT, JoinType.INNER);
			Predicate departmentPredicate = cb.like(cb.lower(department.get("name")),
					"%" + departmentName.toLowerCase() + "%");
			predicates.add(cb.and(activeStatusPredicate, departmentPredicate)); // Combine with
																				// Active filter
		}

		// Filter by postingTitle.name (like filter), combined with Active status filter
		if (postingTitleName != null && !postingTitleName.equalsIgnoreCase("null")) {
			log.info("inside @method getFilteredJobOpenings Apply filter on postingTitleName");
			Join<JobOpening, Designation> designation = jobOpening.join(POSTING_TITLE, JoinType.INNER); // Correctly
																											// joining
																											// postingTitle
			Predicate postingTitlePredicate = cb.like(cb.lower(designation.get("name")),
					"%" + postingTitleName.toLowerCase() + "%"); // Correctly accessing name
			predicates.add(cb.and(activeStatusPredicate, postingTitlePredicate)); // Combine with
																					// Active filter
		}

		if (postingTitleJobLevel != null && !postingTitleJobLevel.equalsIgnoreCase("null")) {
			log.info("inside @method getFilteredJobOpenings Apply filter on postingTitleJobLevel");
			Join<JobOpening, Designation> designation = jobOpening.join(POSTING_TITLE, JoinType.INNER); // Correctly
																											// joining
																											// postingTitle
			Predicate jobLevelPredicate = cb.like(cb.lower(designation.get("jobLevel")),
					"%" + postingTitleJobLevel.toLowerCase() + "%"); // Correctly accessing jobLevel
			predicates.add(cb.and(activeStatusPredicate, jobLevelPredicate)); // Combine with
																				// Active filter
		}

		if (isCritical.equalsIgnoreCase("true")) {
			Boolean value = Boolean.TRUE;
			log.info("inside @method getFilteredJobOpenings Apply filter on isCritical for true case : {}", value);
			Predicate criticalPredicate = cb.equal(jobOpening.get("isCritical"), value);
			predicates.add(cb.and(activeStatusPredicate, criticalPredicate)); // Combine with
																				// Active filter
		}
		if (isCritical.equalsIgnoreCase("false")) {
			Boolean value = Boolean.FALSE;
			log.info("inside @method getFilteredJobOpenings Apply filter on isCritical for false case : {}", value);
			Predicate criticalPredicate = cb.equal(jobOpening.get("isCritical"), value);
			predicates.add(cb.and(activeStatusPredicate, criticalPredicate)); // Combine with
																				// Active filter
		}

		// Apply predicates to the query
		query.select(jobOpening).where(predicates.toArray(new Predicate[0])); // Correctly convert List<Predicate> to
																				// Predicate[]

		return entityManager.createQuery(query).getResultList();
	}

	private void setJobOpeningsDtoFields(List<JobOpeningActiveDto> jobOpeningActiveDtoList, JobOpening jobOpening) {
		log.info("inside @class Jobopeningserviceimpl @method setJobOpeningsDtoFields");
		JobOpeningActiveDto jobOpeningActiveDto = new JobOpeningActiveDto();

		log.debug("JobOpening is : {}", jobOpening);

		if (jobOpening != null && jobOpening.getPostingTitle() != null
				&& jobOpening.getPostingTitle().getName() != null) {
			log.debug("JobOpening title is  : {}", jobOpening.getPostingTitle().getName());
			jobOpeningActiveDto.setTitle(jobOpening.getPostingTitle().getName());
		}
		if (jobOpening != null && jobOpening.getCreatedTime() != null) {
			log.debug("JobOpening created time is  : {}", jobOpening.getCreatedTime());
			jobOpeningActiveDto.setCreatedTime(jobOpening.getCreatedTime());
		}
		if (jobOpening != null && jobOpening.getCreator() != null && jobOpening.getCreator().getFullName() != null) {
			log.debug("JobOpening creator name is  : {}", jobOpening.getCreator().getFullName());
			jobOpeningActiveDto.setCreatorFullName(jobOpening.getCreator().getFullName());
		}
		if (jobOpening != null && jobOpening.getJobOpeningStatus() != null) {
			jobOpeningActiveDto.setStatus(jobOpening.getJobOpeningStatus());
		}

		if (jobOpening != null && jobOpening.getJobId() != null) {
			log.debug("JobId is  : {}", jobOpening.getJobId());
			jobOpeningActiveDto.setJobId(jobOpening.getJobId());
		}

		if (jobOpening != null && jobOpening.getPostingTitle() != null
				&& jobOpening.getPostingTitle().getJobLevel() != null) {
			log.debug("NLevel is  : {}", jobOpening.getPostingTitle().getJobLevel());
			jobOpeningActiveDto.setNLevel(jobOpening.getPostingTitle().getJobLevel());
		}

		jobOpeningActiveDto.setIsCritical(jobOpening.getIsCritical());

		if (jobOpening != null && jobOpening.getDepartment() != null && jobOpening.getDepartment().getName() != null) {
			log.debug("Department name set  is  : {}", jobOpening.getDepartment().getName());
			jobOpeningActiveDto.setDepartmentName(jobOpening.getDepartment().getName());
		}

		JobApplicationRepository jobApplicationRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationRepository.class);

		String jobApplicationStatus = "Interview scheduled";
		log.debug("Inside setJobOpeningsDtoFields customerId is : {}", commonUtils.getCustomerId());
		if (jobOpening != null && jobOpening.getId() != null) {
			Integer count = jobApplicationRepository.getCountOfJobApplicationByStatus(jobApplicationStatus,
					jobOpening.getId(), commonUtils.getCustomerId());
			log.debug("Count of JobApplication is  : {}", count);
			jobOpeningActiveDto.setJobApplicationStatus(count);
		}
		if (jobOpening != null) {
			Integer countJobApplicantionByJobOpening = jobApplicationRepository
					.getCountOfJobApplicationByJobOpeningId(jobOpening.getId(), commonUtils.getCustomerId());
			log.debug("Count of JobApplication For JobOpening is : {}", countJobApplicantionByJobOpening);
			jobOpeningActiveDto.setJobApplicationCount(countJobApplicantionByJobOpening);
		}

		if (jobOpening != null && jobOpening.getId() != null) {
			Integer jobApplicationCountForToday = jobApplicationRepository
					.getCountOfJobApplicationByJobOpeningIdForToday(jobOpening.getId(), commonUtils.getCustomerId());
			log.debug("Count of JobApplication For Today is : {}", jobApplicationCountForToday);
			jobOpeningActiveDto.setJobApplicationCountForToday(jobApplicationCountForToday);
		}
		jobOpeningActiveDtoList.add(jobOpeningActiveDto);

	}

	@Override
	public JobOpening createJobOpening(JobOpeningAndApplicantReferralDto jobOpeningAndApplicantReferralDto) {
		log.info("inside @class Jobopeningserviceimpl @method createJobOpening");

		try {

			JobOpening jobOpening = new JobOpening();
			JobOpening jobOpeningCreated = null;

			log.debug("inside @method createJobOpening jobOpeningAndApplicantReferralDto is : {}",
					jobOpeningAndApplicantReferralDto);

			if (jobOpeningAndApplicantReferralDto != null
					&& jobOpeningAndApplicantReferralDto.getDesignation() != null) {
				log.debug("inside @method createJobOpening Designation is : {}",
						jobOpeningAndApplicantReferralDto.getDesignation());

				PlannedOrgChartService plannedOrgChartService = ApplicationContextProvider.getApplicationContext()
						.getBean(PlannedOrgChartService.class);

				Designation designation = plannedOrgChartService
						.createDesignationForSai(jobOpeningAndApplicantReferralDto.getDesignation());

				jobOpening.setPostingTitle(designation);
				jobOpening.setHeadHunting(jobOpeningAndApplicantReferralDto.getHeadHunted());
				jobOpening.setCareerWebsite(jobOpeningAndApplicantReferralDto.getJobRequisition());
				jobOpening.setDepartment(jobOpeningAndApplicantReferralDto.getDesignation().getDepartment());
				jobOpening.setHiringManager(jobOpeningAndApplicantReferralDto.getHiringManager());
				jobOpening.setJobOpeningStatus("Active");
				jobOpening.setCity(jobOpeningAndApplicantReferralDto.getCity());
				jobOpening.setAddress(jobOpeningAndApplicantReferralDto.getCity());
				if (jobOpeningAndApplicantReferralDto.getDesignation().getJobDescription() != null) {
					log.debug("Going to set JobDescription   is : {}",
							jobOpeningAndApplicantReferralDto.getDesignation().getJobDescription());
					jobOpening.setDescriptionRequirements(
							jobOpeningAndApplicantReferralDto.getDesignation().getJobDescription());
				}

				if (jobOpeningAndApplicantReferralDto.getDesignation().getStartCompensationRange() != null) {
					Integer fromSalary = jobOpeningAndApplicantReferralDto.getDesignation().getStartCompensationRange();

					log.debug("Going to set Salary : {}",
							jobOpeningAndApplicantReferralDto.getDesignation().getStartCompensationRange());
					jobOpening.setFromSalary(fromSalary.toString());
				}

				if (jobOpeningAndApplicantReferralDto.getDesignation().getEndCompensationRange() != null) {
					log.debug("Going to set EndSalary : {}",
							jobOpeningAndApplicantReferralDto.getDesignation().getEndCompensationRange());
					Integer endSalary = jobOpeningAndApplicantReferralDto.getDesignation().getEndCompensationRange();
					jobOpening.setEndSalary(endSalary.toString());
				}

				if (jobOpeningAndApplicantReferralDto.getDesignation().getCreator() != null) {
					log.debug("Going to set creator  is : {}",
							jobOpeningAndApplicantReferralDto.getDesignation().getCreator());
					jobOpening.setCreator(jobOpeningAndApplicantReferralDto.getDesignation().getCreator());
				}

				if (jobOpeningAndApplicantReferralDto.getDesignation().getLastModifier() != null) {
					jobOpening.setLastModifier(jobOpeningAndApplicantReferralDto.getDesignation().getLastModifier());
				}

				if (jobOpeningAndApplicantReferralDto.getDesignation().getDepartment() != null) {
					log.debug("inside @method createJobOpening Department is : {}",
							jobOpeningAndApplicantReferralDto.getDesignation().getDepartment());
					jobOpening.setDepartment(jobOpeningAndApplicantReferralDto.getDesignation().getDepartment());

					if (jobOpeningAndApplicantReferralDto.getDesignation().getDepartment().getName() != null) {
						Map<String, String> mp = new HashMap<>();
						String generatedName = null;
						NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
						if (jobOpeningAndApplicantReferralDto.getDesignation().getDepartment().getName() != null
								&& jobOpeningAndApplicantReferralDto.getCity() != null) {
							String department = transformValue(
									jobOpeningAndApplicantReferralDto.getDesignation().getDepartment().getName());
							log.debug("department for rule is : {} ", department);
							mp.put(DEPARTMENT, department);
							String city = transformCity(jobOpeningAndApplicantReferralDto.getCity());
							log.debug("City for rule is : {} ", city);
							mp.put("city", city);
						}
						nameGenerationWrapperV2 = customNumberValuesRest
								.generateNameAndFriendlyName("jobOpeningWithLocation", mp, Status.ALLOCATED);
						log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
						generatedName = nameGenerationWrapperV2.getGeneratedName();
						jobOpening.setJobId(generatedName);
						CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext()
								.getBean(CustomerInfo.class);
						Integer workspaceId = customerInfo.getActiveUserSpaceId();
						jobOpening.setWorkspaceId(workspaceId);

					}

				}

				jobOpeningCreated = jobOpeningRepository.save(jobOpening);

			} else {
				log.error("Designation obj Cannot be null");
				throw new BusinessException("Designation obj Cannot be null");
			}

			createApplicantReferrals(jobOpeningAndApplicantReferralDto.getReferralList(), jobOpeningCreated);
			createJobOpeningWeightageCriterias(jobOpeningAndApplicantReferralDto, jobOpeningCreated);

			log.debug("inside @method createJobOpening JobOpeningSave is  : {}", jobOpeningCreated);

			return jobOpeningCreated;

		} catch (BusinessException ex) {
			log.error("Error while creating jobopening");
			throw new BusinessException("Error while creating jobopening", ex.getMessage());
		}
	}

	public void createApplicantReferrals(List<Map<String, String>> referralList, JobOpening jobOpeningCreated) {
		log.info("inside @class Jobopeningserviceimpl @method createApplicantReferrals");

		if (referralList != null && !referralList.isEmpty()) {

			for (Map<String, String> referral : referralList) {

				log.debug("inside @method createApplicantReferrals referralList is : {}", referralList);

				ApplicantReferral applicantReferral = new ApplicantReferral();

				applicantReferral.setFirstName(referral.get("firstName"));
				applicantReferral.setLastName(referral.get("lastName"));
				if (jobOpeningCreated != null) {
					log.debug("inside @method createApplicantReferrals jobId is : {}", jobOpeningCreated.getJobId());
					applicantReferral.setJobId(jobOpeningCreated.getJobId());
				}
				applicantReferral.setEmailId(referral.get("email"));
				applicantReferral.setStatus("Applied");
				applicantReferral.setApplicantType("Referred");
				log.debug("inside @method createApplicantReferrals applicantReferral going to save is  : {}",
						applicantReferral);
				applicantReferralRepository.save(applicantReferral);

				log.info("Going to create jobOpeningWrapper to send email");
				JobOpeningWrapper jobOpeningWrapper = new JobOpeningWrapper();
                if(jobOpeningCreated != null) {
				jobOpeningWrapper.setJobId(jobOpeningCreated.getJobId());
				log.debug("JobId is coming : {}", jobOpeningCreated.getJobId());
                }
				log.debug("Email is coming : {}", applicantReferral.getEmailId());
				jobOpeningWrapper.setEmail(applicantReferral.getEmailId());
				String fName = applicantReferral.getFirstName();
				String lName = applicantReferral.getLastName();
				if (fName != null && lName != null) {
					log.debug("Name to be set is  : {}", applicantReferral.getEmailId());
					String fullName = fName + " " + lName;
					jobOpeningWrapper.setFullName(fullName);
				}

				referApplicantForJobopening(jobOpeningWrapper);

			}

		}

	}

	@Override
	public JobOpeningIdDto getJobOpeningDetailByJobId(String jobId) {
		log.debug("Inside getJobOpeningDetailByJobId jobId: {}", jobId);
		JobOpeningIdDto jobOpeningIdDto = new JobOpeningIdDto();
		if (jobId == null) {
			log.error("Job Id is null");
			throw new BusinessException("Job Id is null");
		}
		JobOpening jobOpening = jobOpeningRepository.getJobOpeningByReferenceId(jobId);
		if (jobOpening != null) {
			log.debug("Inside getJobOpeningDetailByJobId jobOpening: {}", jobOpening.getId());
			jobOpeningIdDto.setId(jobOpening.getId());
			jobOpeningIdDto.setJobId(jobOpening.getJobId());
			jobOpeningIdDto.setResponsibilities(jobOpening.getResponsibilities());
			jobOpeningIdDto.setRequirements(jobOpening.getSkills() + " " + jobOpening.getExperienceAndEducation());
			if (jobOpening.getPostingTitle() != null) {
				log.debug("jobOpening posting title: {}", jobOpening.getPostingTitle().getId());
				jobOpeningIdDto.setTitle(jobOpening.getPostingTitle().getName());
			}
			jobOpeningIdDto.setEmploymentType(jobOpening.getJobType());
			jobOpeningIdDto.setPreferredQualifications(jobOpening.getCoreCapabilities());
			if (jobOpening.getDepartment() != null) {
				jobOpeningIdDto.setDepartment(jobOpening.getDepartment().getName());
			}

			jobOpeningIdDto.setAbout(jobOpening.getDescriptionRequirements());
			jobOpeningIdDto.setLocation(jobOpening.getAddress());
		}
		log.debug("jobOpeningIdDto : {}", jobOpeningIdDto);
		return jobOpeningIdDto;
	}

	public void createJobOpeningWeightageCriterias(JobOpeningAndApplicantReferralDto jobOpeningAndApplicantReferralDto,
			JobOpening jobOpeningCreated) {
		log.info("inside @class Jobopeningserviceimpl @method createJobOpeningWeightageCriterias");

		if (jobOpeningAndApplicantReferralDto.getConfigurationList() != null) {

			for (Map<String, String> configMap : jobOpeningAndApplicantReferralDto.getConfigurationList()) {

				JobOpeningWeightageCriterias jobOpeningWeightageCriterias = new JobOpeningWeightageCriterias();

				String configurationName = configMap.get("configurationName");
				String weightageScore = configMap.get("weightageScore");
				double weightageScoreDouble = Double.parseDouble(weightageScore);

				log.debug("configurationName name is : {}", configurationName);

				log.debug("inside @class Jobopeningserviceimpl @method createJobOpeningWeightageCriterias customerId is : {}",
						commonUtils.getCustomerId());
				WeightageConfiguration weightageConfiguration = weightageConfigurationRepository
						.findbyConfigurationName(configurationName, commonUtils.getCustomerId());
				log.debug("Inside @createJobOpeningWeightageCriterias weightageConfiguration is  : {}",
						weightageConfiguration);

				if (weightageConfiguration != null) {
					jobOpeningWeightageCriterias.setConfigurationId(weightageConfiguration);
					jobOpeningWeightageCriterias.setJobOpening(jobOpeningCreated);
					jobOpeningWeightageCriterias.setWeightage(weightageScoreDouble);
					log.debug("jobOpeningWeightageCriterias to be saved is : {}", jobOpeningWeightageCriterias);
					jobOpeningWeightageCriteriasRepository.save(jobOpeningWeightageCriterias);
				}

			}

		}

	}
	
	@Override
	public  List<JobOpening> getJobOpeningByDesignation(Integer designationId)
	   {
		   try
		   {
			   log.debug("Inside @class JobOpeningServiceImpl @method getJobOpeningByDesignation desingation Id :{} ",designationId);
			   List<JobOpening> jobOpenings = jobOpeningRepository.getJobOpeningFromDesignationId(designationId);
			   log.debug("Size of JobOpenings :{} ",jobOpenings.size());
			   return jobOpenings;
		   }
		   catch(Exception e)
		   {
			   log.error("Error inside @JobOpeningServiceImpl @method getJobOpeningByDesignation :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			   throw new BusinessException(); 
		   }
	   }

}
