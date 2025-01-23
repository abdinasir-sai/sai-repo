package com.nouros.hrms.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.ai.chat.AiChatModel;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.document.rest.IDocumentStreamRest;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.model.ConfigurationScore;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobApplicationBatch;
import com.nouros.hrms.model.JobApplicationConfigurationScore;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.model.WeightageConfiguration;
import com.nouros.hrms.repository.ApplicantCertificationsRepository;
import com.nouros.hrms.repository.ApplicantExperienceRepository;
import com.nouros.hrms.repository.ApplicantRepository;
import com.nouros.hrms.repository.ApplicantResumeRepository;
import com.nouros.hrms.repository.ConfigurationScoreRepository;
import com.nouros.hrms.repository.JobApplicationConfigurationScoreRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.JobOpeningRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantCertificationsService;
import com.nouros.hrms.service.ApplicantEducationService;
import com.nouros.hrms.service.ApplicantExperienceService;
import com.nouros.hrms.service.ApplicantLanguageService;
import com.nouros.hrms.service.ApplicantResumeService;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.JobOpeningWeightageCriteriasService;
import com.nouros.hrms.service.WeightageConfigurationService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.CustomMultipartFile;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.ApplicantDto;
import com.nouros.hrms.wrapper.ApplicantExperienceDto;
import com.nouros.hrms.wrapper.JobApplicationDto;
import com.nouros.hrms.wrapper.KeyFactsDto;

import jakarta.validation.Valid;
import net.bull.javamelody.internal.common.LOG;

@Service
public class JobApplicationServiceImpl extends AbstractService<Integer, JobApplication>
		implements JobApplicationService {

	private static final String JOB_APPLICATION_ID = "jobApplicationId";

	private static final String PINNED_APPLICATION = "pinnedApplication";

	private static final String RANKING = "ranking";

	private static final String OTHERS = "others";

	private static final String TOP_RANKED = "topRanked";

	private static final String APPLICANT = "Applicant";

	private static final String WORK_EXPERIENCE = "workExperience";

	private static final String SKILLS = "skills";

	private static final String POSTING_TITLE = "postingTitle";

	private static final String CORE_CAPABILITIES = "coreCapabilities";

	private static final String EXPERIENCE_AND_EDUCATION = "experienceAndEducation";

	private static final String DESCRIPTION_REQUIREMENTS = "descriptionRequirements";

	private static final String JOB_TYPE = "jobType";

	private static final String JOB_APPLICATION_NOT_FOUND = "No Job Applications found for the provided IDs";

	private static final String JOB_OPENING_NOT_FOUND = "No JobOpening Found for this jobId";

	private static final Logger log = LogManager.getLogger(JobApplicationServiceImpl.class);

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	@Autowired
	private JobOpeningRepository jobOpeningRepository;

	@Autowired
	private JobOpeningService jobOpeningService;

	@Autowired
	private ApplicantResumeRepository applicantResumeRepository;

	@Autowired
	private ApplicantResumeService applicantResumeService;

	@Autowired
	private IDocumentStreamRest documentStreamRest;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private UserRest userRest;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private JobApplicationConfigurationScoreRepository jobApplicationConfigurationScoreRepository;

	@Autowired
	private ApplicantExperienceRepository applicantExperienceRepository;

	@Autowired
	private ApplicantCertificationsRepository applicantCertificationsRepository;

	@Autowired
	private ConfigurationScoreRepository configurationScoreRepository;

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	AiChatModel aiChatModel;

	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";
	public static final String SOMETHING_WENT_WRONG = "Something Went Wrong : {} ";
	public static final String RESPONSE = "response";
	public static final String RESPONSE_ARRAY = "responseArray";
	public static final String EVALUATIONS = "evaluations";
	public static final String RESPONSE_FROM_PROMPT_CURL = "Response from PromptCurl is : {}";

	public JobApplicationServiceImpl(GenericRepository<JobApplication> repository) {
		super(repository, JobApplication.class);
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

	/**
	 * Creates a new vendor.
	 *
	 * @param jobApplication The jobApplication object to create.
	 * @return The created vendor object.
	 */
	@Override
	public JobApplication create(JobApplication jobApplication) {
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		if (jobApplication.getJobOpening().getCity() != null
				|| jobApplication.getJobOpening().getDepartment().getName() != null) {
			String department = transformValue(jobApplication.getJobOpening().getDepartment().getName());
			log.debug("department for rule is : {} ", department);
			mp.put("department", department);
			String city = transformCity(jobApplication.getJobOpening().getCity());
			log.debug("City for rule is : {} ", city);
			mp.put("city", city);
		}
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("jobApplicationWithLocation", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		jobApplication.setJobApplicationId(generatedName);
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		jobApplication.setWorkspaceId(workspaceId); // done done
		try {
			String jsonResumeSummary = summariseResumeByJobOpeningForJobApplication(jobApplication);
			log.debug("Response from summariseResumeByJobOpeningForJobApplication is : {} ", jsonResumeSummary);
			jobApplication.setApplicantResumeSummary(jsonResumeSummary);
		} catch (BusinessException be) {
			log.error("Caught error for summarising Resume By Job Opening For Job Application : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		return jobApplicationRepository.save(jobApplication);
	}

	@Override
	public JobApplication findByApplicantId(Integer id) {
		try {
			return jobApplicationRepository.findByApplicantId(id);
		} catch (Exception e) {
			log.error("No jobApplication found for given applicant : {}", id);
			return null;
		}

	}

	@Override
	public String summariseResumeByJobOpeningWithInputFile(Integer jobApplicationId, MultipartFile file) {
		log.info(INSIDE_METHOD, "summariseResumeByJobOpeningWithInputFile");
		JobOpening jobOpening1 = new JobOpening();
		JobApplication jobApplication1 = new JobApplication();
		JSONObject result = new JSONObject();
		String stringResponse = "";
		String response = "";
		JobApplication jobApplication = super.findById(jobApplicationId);
		if (jobApplication != null) {
			jobApplication1 = jobApplication;
		}
		JobOpening jobOpening = jobOpeningService.findById(jobApplication1.getJobOpening().getId());
		if (jobOpening != null) {
			jobOpening1 = jobOpening;
		}
		log.debug("jobOpening1 is : {} ", jobOpening1);
		ResponseEntity<byte[]> responseEntity = convertMultipartFileToResponseEntity(file);
		String fileName = responseEntity.getHeaders().getContentDisposition().getFilename();
		byte[] input = responseEntity.getBody();
		CustomMultipartFile files = new CustomMultipartFile(input, fileName);
		String prompt = "";
		prompt = setDataForSummarization(jobOpening1);
		try {
			response = vectorIntegrationService.uploadAndExecutePrompt(prompt, input, files);
			log.debug(RESPONSE_FROM_PROMPT_CURL, response);
		} catch (BusinessException be) {
			log.error("Caught error for summarise Resume By Job Opening For Job Application Prompt  : {} ",
					be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		JSONObject fullResponse = new JSONObject(response);
		if (fullResponse.getString(RESPONSE) != null) {
			String response1 = fullResponse.getString(RESPONSE);
			log.debug("Response key  from fullResponse is : {} ", response1);
		}
		stringResponse = fullResponse.getString(RESPONSE);
		if (stringResponse == null) {
			result.put("result", "failure");
		} else {
			result.put("result", "success");
			result.put(RESPONSE, stringResponse);
		}
		return result.toString();
	}

	public ResponseEntity<byte[]> convertMultipartFileToResponseEntity(MultipartFile multipartFile) {
		try {
			byte[] fileContent = IOUtils.toByteArray(multipartFile.getInputStream());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentLength(fileContent.length);
			headers.setContentDispositionFormData("file", multipartFile.getOriginalFilename());
			return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
		} catch (IOException e) {
			log.error("Generates the IOException :{}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String summariseResumeByJobOpeningForJobApplication(JobApplication jobApplication) {
		log.info(INSIDE_METHOD, "summariseResumeByJobOpeningForJobApplication");
		JobOpening jobOpening1 = new JobOpening();
		ApplicantResume applicantResume1 = new ApplicantResume();
		String stringResponse = "";
		String response = "";
		JobOpening jobOpening = jobOpeningService.findById(jobApplication.getJobOpening().getId());
		if (jobOpening != null) {
			jobOpening1 = jobOpening;
		}
		log.debug("jobOpening1 is : {} ", jobOpening1);
		ApplicantResume applicantResume = applicantResumeService.findById(jobApplication.getApplicantResume().getId());
		if (applicantResume != null) {
			applicantResume1 = applicantResume;
		}
		log.debug("applicantResume1 is : {} ", applicantResume1);
		int documentIdOfResume = fetchDocumentIdFromApplicantResume(applicantResume1);
		ResponseEntity<byte[]> responseEntity = documentStreamRest.fileDownload(documentIdOfResume);
		String fileName = responseEntity.getHeaders().getContentDisposition().getFilename();
		byte[] input = responseEntity.getBody();
		CustomMultipartFile files = new CustomMultipartFile(input, fileName);
		String prompt = "";
		prompt = setDataForSummarization(jobOpening1);
		try {
			response = vectorIntegrationService.uploadAndExecutePrompt(prompt, input, files);
			log.debug(RESPONSE_FROM_PROMPT_CURL, response);
		} catch (BusinessException be) {
			log.error("Caught error for summarise Resume By Job Opening For Job Application Prompt  : {} ",
					be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		JSONObject fullResponse = new JSONObject(response);
		if (fullResponse.getString(RESPONSE) != null) {
			String response1 = fullResponse.getString(RESPONSE);
			log.debug("Response key  from fullResponse is : {} ", response1);
		}
		stringResponse = fullResponse.getString(RESPONSE);
		return stringResponse;
	}

	private int fetchDocumentIdFromApplicantResume(ApplicantResume applicantResume) {
		log.info(INSIDE_METHOD, "fetchDocumentIdFromApplicantResume");
		String jsonString = applicantResume.getResumeAttachment();
		JSONObject jsonObject = new JSONObject(jsonString);
		int id = 0;
		if (jsonObject.has("ids")) {
			JSONArray idsArray = jsonObject.getJSONArray("ids");
			log.debug("id's coming from ResumeAttachment is : {} ", idsArray);
			for (int i = 0; i < idsArray.length(); i++) {
				id = idsArray.getInt(i);
			}
		}
		return id;
	}

	private JSONObject setDataForJobOpening(JobOpening jobOpening) {
		log.info(INSIDE_METHOD, "setDataForJobOpening");
		JSONObject jsonObject = new JSONObject();
		addValueIfNotNull(jsonObject, "Title", jobOpening.getPostingTitle());
		addValueIfNotNull(jsonObject, DESCRIPTION_REQUIREMENTS,
				removeHtmlTags(jobOpening.getDescriptionRequirements()));
		addValueIfNotNull(jsonObject, JOB_TYPE, jobOpening.getJobType());
		addValueIfNotNull(jsonObject, "salaryRange", jobOpening.getFromSalary());
		addValueIfNotNull(jsonObject, SKILLS, jobOpening.getSkills());
		addValueIfNotNull(jsonObject, WORK_EXPERIENCE, jobOpening.getWorkExperience());
		addValueIfNotNull(jsonObject, "industry", jobOpening.getIndustry());
		return jsonObject;
	}

	private static String removeHtmlTags(String input) {
		return input.replaceAll("<[^>]*+>", "");
	}

	private void addValueIfNotNull(JSONObject jsonObject, String key, Object value) {
		if (value != null) {
			jsonObject.put(key, value);
		} else {
			jsonObject.put(key, "");
		}
	}

	private String setDataForSummarization(JobOpening jobOpening1) {
		log.info(INSIDE_METHOD, "setDataForRecommendationForObjects");
		JSONObject jobOpening1Data = setDataForJobOpening(jobOpening1);
		log.debug("jobOpening1Data JSONObject is : {} ", jobOpening1Data);
		String prompt = generatePromptForSummaryGeneration(jobOpening1Data.toString());
		log.debug("FinalObject on basis of Job Opening is : {} ", prompt);
		return prompt;
	}

	public static String generatePromptForSummaryGeneration(String variable1) {
		log.info(INSIDE_METHOD, "generatePrompt");

		String prompt = "You are an expert in judging candidate profile and defining its strength and weakness please For Below job position:\n\n**Job Position:**\n%s\n\nBelow is the resume content for a candidate, Please generate a json with Strength and weakness analysis**Applicants Resume:**\n{fileContent}\n\nNote: Output is required in below JSON FORMAT that can be displayed in tabular structure\n\n{\"strength\":[{\"header\":\"\",\"content\":\"\"}],\"weakness\":[{\"header\":\"\",\"content\":\"\"}]}";

		return String.format(prompt, variable1);
	}

	@Override
	public List<JobApplication> applicantByApplicationStatus(String name) {
		return jobApplicationRepository.findAllByApplicationStatus(name);

	}

	@Override
	public Integer getCountOfJobApplicationByDepartmentId(Integer departmentId) {
		log.info(INSIDE_METHOD, "getCountOfJobApplicationByDepartmentId");
		try {
			return jobApplicationRepository.getCountOfJobApplicationByDepartmentId(departmentId);
		} catch (Exception e) {
			log.error("An error occurred while getting the count of Job Applications by department ID: {}",
					e.getMessage());
			return null;
		}
	}

	@Override
	public Integer getCountOfJobApplicationByDepartmentIds(List<Integer> departmentIds) {
		log.info(INSIDE_METHOD, "getCountOfJobApplicationByDepartmentIds");
		try {
			return jobApplicationRepository.getCountOfJobApplicationByDepartmentId(departmentIds);
		} catch (Exception e) {
			log.error("An error occurred while getting the count of Job Applications by department ID: {}",
					e.getMessage());
			return null;
		}
	}

	@Override
	public JobApplication createJobApplicationForApplicantWithoutPrioritization(Applicant applicant, String jobId) {
		log.info(INSIDE_METHOD, "createJobApplicationForApplicant");
		JobApplication jobApplication = new JobApplication();
		if (applicant != null && applicant.getEmailId() != null && applicant.getProfileSummary() != null) {
			jobApplication.setApplicantResumeSummary(applicant.getProfileSummary());
			jobApplication.setEmailId(applicant.getEmailId());
		}
		jobApplication.setApplicationStatus("Applied");
		log.debug("Inside createJobApplicationForApplicantWithoutPrioritization jobOpeningId jobId found is : {}",
				jobId);
		JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobOpeningService.class);
		JobOpening jobOpening = jobOpeningService.getJobOpeningByReferenceId(jobId);
		log.debug("jobOpening fetched  through jobOpeningId is : {}", jobOpening);
		jobApplication.setJobOpening(jobOpening);
		jobApplication.setApplicantResumeSummary("{}");
		jobApplication.setWorkflowStage("Submitted");
		log.debug("Inside createJobApplicationForApplicantWithoutPrioritization jobApplication To be saved is : {}",
				convertObjectToJson(jobApplication));
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("staticJobApplication", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2 formed is : {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		jobApplication.setJobApplicationId(generatedName);
		jobApplication.setApplicationStatus("NEW");
		jobApplication.setApplicant(applicant);
		log.debug("going To saved job application  is : {}", convertObjectToJson(jobApplication));
		JobApplication jobApplicationsaved = jobApplicationRepository.save(jobApplication);
		return jobApplicationsaved;
	}

	@Override
	public JobApplication createJobApplicationForApplicant(Applicant applicant, String jobId) {
		log.info(INSIDE_METHOD, "createJobApplicationForApplicant");
		JobApplication jobApplication = new JobApplication();
		JobOpening jobOpening = null;
		List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList = new ArrayList<>();
		jobApplication.setApplicant(applicant);
		if (applicant != null && applicant.getEmailId() != null && applicant.getProfileSummary() != null) {
			jobApplication.setApplicantResumeSummary(applicant.getProfileSummary());
			jobApplication.setEmailId(applicant.getEmailId());
		}
		jobApplication.setApplicationStatus("Applied");
		if (jobId != null) {
			log.debug("jobOpeningId fetched from Applicant Method as a param is : {}", jobId);
			JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
					.getBean(JobOpeningService.class);
			jobOpening = jobOpeningService.getJobOpeningByReferenceId(jobId);
			log.debug("Inside createJobApplicationForApplicant jobOpening fetched  through jobOpeningId is : {}",
					jobOpening);
		} else {
			Map<String, String> attributeMap = userRest.getAttributeMap(customerInfo.getUsername());
			log.debug("attributeMap fetched is : {}", attributeMap);
			String jobOpeningId = attributeMap.get(JOB_TYPE);
			log.debug("jobOpeningId fetched from attributeMap is : {}", jobOpeningId);
			JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
					.getBean(JobOpeningService.class);
			jobOpening = jobOpeningService.getJobOpeningByReferenceId(jobOpeningId);
		}
		log.debug("jobOpening fetched  through jobOpeningId as param or user attribute is : {}", jobOpening);
		jobApplication.setJobOpening(jobOpening);
		jobApplication.setApplicantResumeSummary("{}");
		jobApplication.setWorkflowStage("Submitted");
		log.debug("Inside createJobApplicationForApplicant jobApplication To be saved is : {}",
				convertObjectToJson(jobApplication));
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("staticJobApplication", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2 formed is : {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		jobApplication.setJobApplicationId(generatedName);
		jobApplication.setApplicationStatus("NEW");
		JobApplication jobApplicationsaved = jobApplicationRepository.save(jobApplication);
		log.debug("JobApplication saved is : {}", convertObjectToJson(jobApplicationsaved));
		try {
			JobOpeningWeightageCriteriasService jobOpeningWeightageCriteriasService = ApplicationContextProvider
					.getApplicationContext().getBean(JobOpeningWeightageCriteriasService.class);
			if (jobOpening != null && jobOpening.getId() != null) {
				jobOpeningWeightageCriteriasList = jobOpeningWeightageCriteriasService
						.getAllWeightageCriteriasByJobOpening(jobOpening.getId());
				log.debug("Get All jobOpeningWeightageCriteriasList : {}", jobOpeningWeightageCriteriasList);
			} else {
				log.error(JOB_OPENING_NOT_FOUND);
				throw new BusinessException(JOB_OPENING_NOT_FOUND);
			}
			if (jobOpeningWeightageCriteriasList != null && !jobOpeningWeightageCriteriasList.isEmpty()) {
				iterateJobOpeningWeightageCriterias(jobApplicationsaved, jobOpeningWeightageCriteriasList, applicant,
						jobOpening);
			} else {
				log.error("No WeightageCriteria Found for this Specific JobOpening with jobId");

				throw new BusinessException("No WeightageCriteria Found for this Specific JobOpening with jobId : "
						+ jobOpening.getJobId());
			}
		} catch (Exception e) {
			log.error("getting error while fetching and saving score of Criteria");
		}
		log.debug("going to change/update Score for JobApplication : {}", convertObjectToJson(jobApplication));
		JobApplication jobApplicationUpdated = calculateOverallScoreForJobApplication(jobApplicationsaved);
		log.debug("changed/updated  JobApplication  is : {}", convertObjectToJson(jobApplicationUpdated));
		return jobApplicationUpdated;
	}

	private void iterateJobOpeningWeightageCriterias(JobApplication jobApplicationsaved,
			List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList, Applicant applicant,
			JobOpening jobOpening) {
		log.info(INSIDE_METHOD, "iterateJobOpeningWeightageCriterias");
		WeightageConfigurationService weightageConfigurationService = ApplicationContextProvider.getApplicationContext()
				.getBean(WeightageConfigurationService.class);
		for (JobOpeningWeightageCriterias jc : jobOpeningWeightageCriteriasList) {
			if (jc.getConfigurationId() != null) {
				WeightageConfiguration weightageConfiguration = weightageConfigurationService
						.getWeightageConfigurationById(jc.getConfigurationId().getId());
				log.debug("weightageConfiguration is : {}", weightageConfiguration);
				iterateFieldsForPrompt(jobApplicationsaved, weightageConfiguration, applicant, jobOpening,
						jc.getWeightage());
			}
		}
	}

	private void iterateFieldsForPrompt(JobApplication jobApplicationsaved, WeightageConfiguration wc,
			Applicant applicant, JobOpening jobOpening, Double weightage) {
		log.info(INSIDE_METHOD, "iterateFieldsForPrompt");
		String response = "";
		JSONObject applicantJson = new JSONObject();
		JSONObject jobOpeningJson = new JSONObject();

		if (wc != null && wc.getConfigurationJson() != null) {
			JSONObject jsonObject = new JSONObject(wc.getConfigurationJson());
			String applicantFields = jsonObject.optString("applicantFields", null);
			if (applicantFields != null && !applicantFields.isEmpty()) {
				log.debug("Applicant Fields are : {}", applicantFields);
				applicantJson = iterateFieldsforApplicant(applicantFields, applicant);
				log.debug("applicantJson  formed for applicant fields is  : {}", applicantJson);
			} else {
				log.info("No applicant fields available");
			}
			String applicantLanguageFields = jsonObject.optString("applicantLanguageFields", null);
			if (applicantLanguageFields != null && !applicantLanguageFields.isEmpty()) {
				log.debug("applicantLanguage Fields  are : {}", applicantLanguageFields);
				iterateFieldsforApplicantLanguage(applicantLanguageFields, applicantJson, applicant);
				log.debug("applicantJson  formed after setting Languages completely is : {}", applicantJson);

			} else {
				log.info("No applicant Languages fields available");
			}
			setApplicantSkillSetDetail(applicant, applicantJson, jsonObject);
			String jobOpeningFields = jsonObject.optString("jobOpeningFields", null);
			if (jobOpeningFields != null && !jobOpeningFields.isEmpty() && jobOpening != null) {
				log.debug("jobOpening Fields are : {}", jobOpeningFields);
				jobOpeningJson = iterateFieldsforJobOpening(jobOpeningFields, jobOpening);
			}
			log.debug("jobOpeningJson  formed is  : {}", jobOpeningJson);
		} else {
			log.info("No job opening fields available");
		}
		log.debug("final applicantJson formed after setting everything is : {}", applicantJson);
//		String prompt = "";
//
//		prompt = getIndividualScoreForDetails(wc.getConfigurationName(),wc.getConfigurationQuestions() ,
//				jobOpeningJson.toString(), applicantJson.toString());
//		try {
//			response = vectorIntegrationService.executePrompt(prompt);
//			log.debug(RESPONSE_FROM_PROMPT_CURL, response);
//		} catch (BusinessException be) {
//			log.error("Caught error for scoring fields based on Applicant and JobOpening Details from prompt  : {} ",
//					be.getMessage());
//		} catch (Exception e) {
//			log.error(SOMETHING_WENT_WRONG, e);
//		}
//		JSONObject jsonObject = new JSONObject(response);
//		log.debug("jsonObject as a response is : {}", jsonObject);
//		JSONObject responseScoreJson = jsonObject.getJSONObject(RESPONSE);
//		log.debug("responseScoreJson as a response of jsonObject is : {}", responseScoreJson);
//		JSONArray scoreDetails = responseScoreJson.getJSONArray(RESPONSE_ARRAY);

		JSONArray scoreDetails = new JSONArray();
		try {

			Map<String, Object> inputMap = new HashMap<>();
			inputMap.put("wightageConfigurationName", wc.getConfigurationName());
			inputMap.put("configurationQuestions", wc.getConfigurationQuestions());
			inputMap.put("jobOpeningJson", jobOpeningJson.toString());
			inputMap.put("applicantJson", applicantJson.toString());
			response = aiChatModel.chatCompletion("HRMS_APP_NAME-JobApplication-Prioritization_Of_Job_Application-v-1",
					inputMap, String.class);
			log.debug("Response from AI Chat Model is : {}", response);

			String formattedReponse = extractJsonObjectString(response);
			log.debug("formattedReponse after formating is : {}", formattedReponse);
			JSONObject jsonObject = new JSONObject(formattedReponse);
			log.debug("JSONObject as a response is : {}", jsonObject);
			scoreDetails = jsonObject.getJSONArray(EVALUATIONS);
			log.debug("Score details array from jsonObject is : {}", scoreDetails);

		} catch (BusinessException be) {
			log.error("Caught error while scoring fields based on Applicant and JobOpening Details: {}",
					be.getMessage());
		}

		try {
			setScoreFetchedForWightageConfiguration(jobApplicationsaved, wc, scoreDetails, weightage);
		} catch (Exception be) {
			log.error("Caught error While setting scores in JobApplicationConfigurationScore entity  : {}",
					be.getMessage());
		}
	}

	private static String extractJsonObjectString(String inputString) {
        int firstIndex = inputString.indexOf('{');
        int lastIndex = inputString.lastIndexOf('}');
        if (firstIndex != -1 && lastIndex != -1 && firstIndex < lastIndex) {
            inputString = inputString.substring(firstIndex, lastIndex + 1);
        }
 
        return inputString;
    }
	
	private void setApplicantSkillSetDetail(Applicant applicant, JSONObject applicantJson, JSONObject jsonObject) {
		String applicantEducationFields = jsonObject.optString("applicantEducationFields", null);
		if (applicantEducationFields != null && !applicantEducationFields.isEmpty()) {
			log.debug("applicantEducation Fields  are : {}", applicantEducationFields);
			iterateFieldsforApplicantEducation(applicantEducationFields, applicantJson, applicant);
			log.debug("applicantJson  formed after setting Education completely is : {}", applicantJson);

		} else {
			log.info("No applicant Education fields available");
		}
		String applicantCertificationFields = jsonObject.optString("applicantCertificationsFields", null);
		if (applicantCertificationFields != null && !applicantCertificationFields.isEmpty()) {
			log.debug("applicantCertification Fields  are : {}", applicantCertificationFields);
			iterateFieldsforApplicantCertification(applicantCertificationFields, applicantJson, applicant);
			log.debug("applicantJson  formed after setting Certifications completely is : {}", applicantJson);

		} else {
			log.info("No applicant Certifications fields available");
		}
		String applicantExperienceFields = jsonObject.optString("applicantExperienceFields", null);
		if (applicantExperienceFields != null && !applicantExperienceFields.isEmpty()) {
			log.debug("applicantExperience Fields  are : {}", applicantExperienceFields);
			iterateFieldsforApplicantExperience(applicantExperienceFields, applicantJson, applicant);
			log.debug("applicantJson  formed after setting Experience completely is : {}", applicantJson);

		} else {
			log.info("No applicant Experience fields available");
		}
	}

	private void setScoreFetchedForWightageConfiguration(JobApplication jobApplicationsaved, WeightageConfiguration wc,
			JSONArray scoreDetails, Double weightagePercentage) {
		log.info(INSIDE_METHOD, "setScoreFetchedForWightageConfiguration");
		JobApplicationConfigurationScore jobApplicationConfigurationScore = new JobApplicationConfigurationScore();
		if (jobApplicationsaved != null) {
			jobApplicationConfigurationScore.setJobApplication(jobApplicationsaved);
		}
		if (wc != null) {
			jobApplicationConfigurationScore.setConfigurationId(wc);
		}
		JobApplicationConfigurationScoreRepository jobApplicationConfigurationScoreRepository = ApplicationContextProvider
				.getApplicationContext().getBean(JobApplicationConfigurationScoreRepository.class);
		ConfigurationScoreRepository configurationScoreRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(ConfigurationScoreRepository.class);
		double totalScore = 0;
		int scoreCount = 0;
		for (int i = 0; i < scoreDetails.length(); i++) {
			JSONObject scoreDetail = scoreDetails.getJSONObject(i); // Read each JSON object
			Double score = scoreDetail.getDouble("subScore"); // Get subScore
			String question = scoreDetail.getString("question"); // Get question
			String answer = scoreDetail.getString("answer"); // Get answer
			log.debug("score fetched from response is : {}", score);
			ConfigurationScore configurationScore = new ConfigurationScore();
			configurationScore.setSubScore(score);
			configurationScore.setQuestion(question);
			configurationScore.setAnswer(answer);
			configurationScore.setWeightageConfiguration(wc);
			configurationScore.setJobApplication(jobApplicationsaved);
			ConfigurationScore configurationScoreSaved = configurationScoreRepository.save(configurationScore);
			log.debug("configurationScoreSaved Saved is : {}", configurationScoreSaved);
			totalScore += score;
			log.debug("totalScore calculated  is : {}", totalScore);
			scoreCount++;
		}
		log.debug("scoreCount evaluated  is : {}", scoreCount);
		double averageScore = scoreCount > 0 ? totalScore / scoreCount : 0;
		log.debug("Average score calculated : {}", averageScore);
		log.debug("weightagePercentage is  : {}", weightagePercentage);
		double weightedScore = averageScore * (weightagePercentage / 100.0);
		log.debug("Average score calculated : {}", weightedScore);
		log.debug("weighted Score calculated after fomating its value : {}",
				Double.parseDouble(String.format("%.2f", weightedScore)));
		jobApplicationConfigurationScore.setIndividualScore(Double.parseDouble(String.format("%.2f", weightedScore)));
		JobApplicationConfigurationScore jobApplicationConfigurationScoresaved = jobApplicationConfigurationScoreRepository
				.save(jobApplicationConfigurationScore);
		log.debug("jobApplicationConfigurationScoresaved saved is : {}", jobApplicationConfigurationScoresaved);
	}

	private void iterateFieldsforApplicantEducation(String fields, JSONObject applicantJson, Applicant applicant) {
		log.info(INSIDE_METHOD, "iterateFieldsforApplicantEducation");
		JSONArray applicantEducationArray = new JSONArray();
		String[] fieldArray = fields.split(",");
		ApplicantEducationService applicantEducationService = ApplicationContextProvider.getApplicationContext()
				.getBean(ApplicantEducationService.class);
		List<ApplicantEducation> applicantEducationList = applicantEducationService
				.getEducationsForApplicant(applicant.getId());
		log.debug("applicantEducationList found is : {}", applicantEducationList);
		for (ApplicantEducation ae : applicantEducationList) {
			JSONObject applicantEducationJson = new JSONObject();
			for (String field : fieldArray) {
				String trimmedField = field.trim();
				if (trimmedField == null || trimmedField.isEmpty()) {
					continue;
				}
				switch (trimmedField) {
				case "fieldOfStudy":
					applicantEducationJson.put("fieldOfStudy",
							ae.getFieldOfStudy() != null ? ae.getFieldOfStudy() : JSONObject.NULL);
					break;
				case "degreeDiploma":
					applicantEducationJson.put("degreeDiploma",
							ae.getDegreeDiploma() != null ? ae.getDegreeDiploma() : JSONObject.NULL);
					break;
				default:
					applicantEducationJson.put(trimmedField, "");
					break;
				}
			}
			applicantEducationArray.put(applicantEducationJson);
		}
		applicantJson.put("applicantEducation", applicantEducationArray);
	}

	private void iterateFieldsforApplicantCertification(String fields, JSONObject applicantJson, Applicant applicant) {
		log.info(INSIDE_METHOD, "iterateFieldsforApplicantCertification");
		JSONArray applicantCertificationArray = new JSONArray();
		String[] fieldArray = fields.split(",");
		ApplicantCertificationsService applicantCertificationsService = ApplicationContextProvider
				.getApplicationContext().getBean(ApplicantCertificationsService.class);
		List<ApplicantCertifications> applicantCertificationsList = applicantCertificationsService
				.getCertificationsForApplicant(applicant.getId());
		log.debug("applicantEducationList found is : {}", applicantCertificationsList);
		for (ApplicantCertifications ac : applicantCertificationsList) {
			JSONObject applicantCertificationJson = new JSONObject();
			for (String field : fieldArray) {
				String trimmedField = field.trim();
				if (trimmedField == null || trimmedField.isEmpty()) {
					continue;
				}
				switch (trimmedField) {
				case "certificationName":
					applicantCertificationJson.put("certificationName",
							ac.getCertificationName() != null ? ac.getCertificationName() : JSONObject.NULL);
					break;
				case "issuingInstitution":
					applicantCertificationJson.put("issuingInstitution",
							ac.getIssuingInstitution() != null ? ac.getIssuingInstitution() : JSONObject.NULL);
					break;
				default:
					applicantCertificationJson.put(trimmedField, "");
					break;
				}
			}
			applicantCertificationArray.put(applicantCertificationJson);
		}
		applicantJson.put("applicantCertification", applicantCertificationArray);
	}

	private void iterateFieldsforApplicantExperience(String fields, JSONObject applicantJson, Applicant applicant) {
		log.info(INSIDE_METHOD, "iterateFieldsforApplicantExperience");
		JSONArray applicantExperienceArray = new JSONArray();
		String[] fieldArray = fields.split(",");
		ApplicantExperienceService applicantExperienceService = ApplicationContextProvider.getApplicationContext()
				.getBean(ApplicantExperienceService.class);
		List<ApplicantExperience> applicantExperienceList = applicantExperienceService
				.getExperienceForApplicant(applicant.getId());
		log.debug("applicantExperienceList found is : {}", applicantExperienceList);
		for (ApplicantExperience ae : applicantExperienceList) {
			JSONObject applicantExperienceJson = new JSONObject();
			for (String field : fieldArray) {
				String trimmedField = field.trim();
				if (trimmedField == null || trimmedField.isEmpty()) {
					continue;
				}
				switch (trimmedField) {
				case "summary":
					applicantExperienceJson.put("summary", ae.getSummary() != null ? ae.getSummary() : JSONObject.NULL);
					break;
				case "occupation":
					applicantExperienceJson.put("occupation",
							ae.getOccupation() != null ? ae.getOccupation() : JSONObject.NULL);
					break;
				case "duration":
					applicantExperienceJson.put("duration",
							ae.getDuration() != null ? ae.getDuration() : JSONObject.NULL);
					break;
				default:
					applicantExperienceJson.put(trimmedField, "");
					break;
				}
			}
			applicantExperienceArray.put(applicantExperienceJson);
		}
		applicantJson.put("applicantExperience", applicantExperienceArray);
	}

	private void iterateFieldsforApplicantLanguage(String fields, JSONObject applicantJson, Applicant applicant) {
		log.info(INSIDE_METHOD, "iterateFieldsforApplicantLanguage");
		JSONArray applicantLanguageArray = new JSONArray();
		String[] fieldArray = fields.split(",");
		ApplicantLanguageService applicantLanguageService = ApplicationContextProvider.getApplicationContext()
				.getBean(ApplicantLanguageService.class);
		List<ApplicantLanguage> applicantLanguageList = applicantLanguageService
				.getLanguagesForApplicant(applicant.getId());
		log.debug("applicantLanguageList found is : {}", applicantLanguageList);
		for (ApplicantLanguage al : applicantLanguageList) {
			JSONObject applicantLanguageJson = new JSONObject();
			for (String field : fieldArray) {
				String trimmedField = field.trim();
				if (trimmedField == null || trimmedField.isEmpty()) {
					continue;
				}
				switch (trimmedField) {
				case "languageLevel":
					applicantLanguageJson.put("languageLevel",
							al.getLanguageLevel() != null ? al.getLanguageLevel() : JSONObject.NULL);
					break;
				case "languageName":
					applicantLanguageJson.put("languageName",
							al.getLanguageName() != null ? al.getLanguageName() : JSONObject.NULL);
					break;
				default:
					applicantLanguageJson.put(trimmedField, "");
					break;
				}
			}
			applicantLanguageArray.put(applicantLanguageJson);
		}
		applicantJson.put("applicantLanguage", applicantLanguageArray);
	}

	public static JSONObject iterateFieldsforApplicant(String fields, Applicant applicant) {
		log.info(INSIDE_METHOD, "iterateFieldsforApplicant");
		String[] fieldArray = fields.split(",");
		JSONObject applicantJson = new JSONObject();
		for (String field : fieldArray) {
			String trimmedField = field.trim();
			if (trimmedField == null || trimmedField.isEmpty()) {
				continue;
			}
			switch (trimmedField) {
			case "experienceInYears":
				applicantJson.put("experienceInYears",
						applicant.getExperienceInYears() != null ? applicant.getExperienceInYears() : JSONObject.NULL);
				break;
			case "skillSet":
				applicantJson.put("skillSet",
						applicant.getSkillSet() != null ? applicant.getSkillSet() : JSONObject.NULL);
				break;
			case "citizenshipStatus":
				applicantJson.put("citizenshipStatus",
						applicant.getCitizenshipStatus() != null ? applicant.getCitizenshipStatus() : JSONObject.NULL);
				break;
			case "eligibleToWorkInSaudi":
				applicantJson.put("eligibleToWorkInSaudi", applicant.isEligibleToWorkInSaudi());
				break;
			case "willingToRelocate":
				applicantJson.put("willingToRelocate", applicant.isWillingToRelocate());
				break;
			case "noticePeriod":
				applicantJson.put("noticePeriod",
						applicant.getNoticePeriod() != null ? applicant.getNoticePeriod() : JSONObject.NULL);
				break;
			case "preferredLocation":
				applicantJson.put("preferredLocation",
						applicant.getPreferredLocation() != null ? applicant.getPreferredLocation() : JSONObject.NULL);
				break;
			default:
				applicantJson.put(trimmedField, "");
				break;
			}
		}
		return applicantJson;
	}

	public static JSONObject iterateFieldsforJobOpening(String fields, JobOpening jobOpening) {
		String[] fieldArray = fields.split(",");
		JSONObject jobOpeningJson = new JSONObject(); // Create a new JSON object
		for (String field : fieldArray) {
			String trimmedField = field.trim();
			if (trimmedField == null || trimmedField.isEmpty()) {
				continue;
			}
			switch (trimmedField) {
			case WORK_EXPERIENCE:
				jobOpeningJson.put(WORK_EXPERIENCE,
						jobOpening.getWorkExperience() != null ? jobOpening.getWorkExperience() : JSONObject.NULL);
				break;
			case DESCRIPTION_REQUIREMENTS:
				jobOpeningJson.put(DESCRIPTION_REQUIREMENTS,
						jobOpening.getDescriptionRequirements() != null ? jobOpening.getDescriptionRequirements()
								: JSONObject.NULL);
				break;
			case SKILLS:
				jobOpeningJson.put(SKILLS, jobOpening.getSkills() != null ? jobOpening.getSkills() : JSONObject.NULL);
				break;
			case POSTING_TITLE:
				jobOpeningJson.put(POSTING_TITLE,
						(jobOpening.getPostingTitle() != null && jobOpening.getPostingTitle().getName() != null)
								? jobOpening.getPostingTitle().getName()
								: JSONObject.NULL);
				break;
			case CORE_CAPABILITIES:
				jobOpeningJson.put(CORE_CAPABILITIES,
						jobOpening.getCoreCapabilities() != null ? jobOpening.getCoreCapabilities() : JSONObject.NULL);
				break;
			case EXPERIENCE_AND_EDUCATION:
				jobOpeningJson.put(EXPERIENCE_AND_EDUCATION,
						jobOpening.getExperienceAndEducation() != null ? jobOpening.getExperienceAndEducation()
								: JSONObject.NULL);
				break;
			default:
				jobOpeningJson.put(trimmedField, "");
				break;
			}
		}
		return jobOpeningJson;
	}

	private String getIndividualScoreForDetails(String variable1, String variable2, String variable3,
			String variable4) {
		log.info(INSIDE_METHOD, "generatePrompt");

		String prompt = """
				You are an expert AI evaluator responsible for scoring a candidates qualifications across a fixed category, in this case which is %s For every category, you will answer five predefined questions and assign a score between 0 and 100. The scoring must be based on objective criteria, a critical analysis of the candidates fit for the role, and an evaluation of the detailed information provided about the candidate. You have to compare the job opening requirements given to you as information in jobOpeningJson against the information of applicant provided to you in applicantJson , which will be given as input to you.
				Scoring Guidelines:
				0-30: The candidate does not meet the criteria or shows minimal alignment with the job requirements.
				31-60: The candidate partially meets the criteria, but there are significant gaps or lack of evidence supporting their qualifications.
				61-80: The candidate meets most of the criteria, showing solid alignment with the role, but there are areas for improvement.
				81-100: The candidate exceeds the criteria, demonstrating strong alignment with the role and a proven track record.

				Rules for Evaluation:
				Be Self-Critical: Evaluate the candidate with a high level of scrutiny. Avoid giving high scores unless there is clear evidence that the candidate has consistently met or exceeded the job requirements.
				Be Objective: Use the information provided to make an unbiased judgment. Consider all relevant details, but do not assume or infer beyond the information presented.
				Use Evidence-Based Analysis: Scores should be justified by examples from the candidates work history, skill set, and education. Avoid generic reasoning and focus on specific, factual evidence.
				Be Balanced: Consider both the strengths and weaknesses of the candidate. A strong background in one area should not automatically result in a high score if other areas are lacking.

				Five questions which needs to be answered by you with sample answers is given below:

				%s

				Both output and input format will be provided to you.

				Use the postingTitle, workExperience, skills, coreCapabilities, experienceAndEducation and descriptionRequirements key from jobOpeningJson provided in the input block. Use applicantJson to get the applicant data and compare it against the requirement provided in the above keys.

				Input:
				***Find the job requirements below in the jobOpeningJson:***

				%s

				***Find the information on applicant below in applicantJson:***

				 %s

				Output requirements:
				1. While performing the analysis use the complete information provided in the applicantJson to compare and score the applicant based on the required criteria.
				2. Give a sub score for each of the five questions based on your evaluation
				3. "question" key in the output will be the questions provided above to you in "Five questions which needs to be answered by you with sample answers is given below" section.
				4. "answer" key in the output will be the evaluation which you will be doing on each question based on the question and details provided to you on job opening in jobOpeningJson and details of applicant from applicantJson.
				5. "subScore" key in the output will be the score that you will be giving to the applicant for each question's evaluation based on the scoring guidelines provided to you.
				6. Do not copy the response and subScore from the sample output given to you below and give answers as well as subScore by analyzing it every time as new.
				7. Strictly follow the sample output format given below and give the output format in a clearly formatted JSON which can be parsed by ObjectMapper method in Java:

				Output Guidelines:
				No Extra Text: The output should only contain the JSON object. Do not include any additional commentary, explanations, or prompts in the response.
				Do Not Reuse: Every answer and score must be freshly generated based on the specific input data provided. Do not copy responses from previous evaluations.
				Error-Free: Ensure that the JSON is valid and can be parsed without errors. Pay attention to correct formatting and syntax.

				### **Output Example**

				```json
				{{
				  "responseArray": [
				    {{
				      \"question\": \"<Question One from the five>\",
				      \"answer\": \"<Your evaluation based on jobOpeningJson and applicantJson>\",
				      \"subScore\": \"<Your score for this question based on the guidelines>\"
				    }},
				    {{
				      \"question\": \"<Question Two from the five>\",
				      \"answer\": \"<Your evaluation based on jobOpeningJson and applicantJson>\",
				      \"subScore\": \"<Your score for this question based on the guidelines>\"
				    }},
				    {{
				      \"question\": \"<Question Three from the five>\",
				      \"answer\": \"<Your evaluation based on jobOpeningJson and applicantJson>\",
				      \"subScore\": \"<Your score for this question based on the guidelines>\"
				    }},
				    {{
				      \"question\": \"<Question Four from the five>\",
				      \"answer\": \"<Your evaluation based on jobOpeningJson and applicantJson>\",
				      \"subScore\": \"<Your score for this question based on the guidelines>"
				    }},
				    {{
				      \"question\": \"<Question Five from the five>\",
				      \"answer\": \"<Your evaluation based on jobOpeningJson and applicantJson>\",
				      \"subScore\": \"<Your score for this question based on the guidelines>\"
				    }}
				  ]
				}}
				```

				#### **the output should be in exactly in json format which is surrounded by flower brackets**
				#### **Respond only with JSON data without any additional lines or explanations**
				""";

		return String.format(prompt, variable1, variable2, variable3, variable4);
	}

	@Override
	public JobApplication updateJobApplicationForApplicant(Applicant applicant, JobApplication jobApplication) {
		log.info(INSIDE_METHOD, "updateJobApplicationForApplicant");
		jobApplication.setApplicant(applicant);
		List<JobOpeningWeightageCriterias> jobOpeningWeightageCriteriasList = new ArrayList<>();
		if (applicant != null && applicant.getEmailId() != null) {
			jobApplication.setEmailId(applicant.getEmailId());
		}
		jobApplication.setApplicationStatus("Interview to be scheduled");
		Map<String, String> attributeMap = userRest.getAttributeMap(customerInfo.getUsername());
		log.debug("attributeMap fetched is : {}", attributeMap);
		String jobOpeningId = attributeMap.get(JOB_TYPE);
		log.debug("jobOpeningId fetched from attributeMap is : {}", jobOpeningId);
		JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobOpeningService.class);
		JobOpening jobOpening = jobOpeningService.getJobOpeningByReferenceId(jobOpeningId);
		log.debug("Inside updateJobApplicationForApplicantjobOpening fetched  through jobOpeningId is : {}",
				jobOpening);
		jobApplication.setJobOpening(jobOpening);
		log.debug("Inside updateJobApplicationForApplicantjobOpening jobApplication To be saved is : {}",
				convertObjectToJson(jobApplication));
		JobApplication jobApplicationsaved = jobApplicationRepository.save(jobApplication);
		try {
			JobOpeningWeightageCriteriasService jobOpeningWeightageCriteriasService = ApplicationContextProvider
					.getApplicationContext().getBean(JobOpeningWeightageCriteriasService.class);
			if (jobOpening != null && jobOpening.getId() != null) {
				jobOpeningWeightageCriteriasList = jobOpeningWeightageCriteriasService
						.getAllWeightageCriteriasByJobOpening(jobOpening.getId());
			} else {
				throw new BusinessException(JOB_OPENING_NOT_FOUND);
			}
			if (jobOpeningWeightageCriteriasList != null && !jobOpeningWeightageCriteriasList.isEmpty()) {
				log.debug("going to delete ConfigurationScore , saved for JobApplication with is : {}",
						jobApplication.getId());
				deleteConfigurationScoreByJobApplicationId(jobApplication.getId());
				log.info("configurationScores Deleted Successfully , now going to calculate fresh scores again...");
				iterateJobOpeningWeightageCriterias(jobApplicationsaved, jobOpeningWeightageCriteriasList, applicant,
						jobOpening);
			} else {
				throw new BusinessException("No WeightageCriteria Found for this Specific JobOpening with jobId : "
						+ jobOpening.getJobId());
			}
		} catch (Exception e) {
			log.error("getting error while fetching and saving score of Criteria");
		}
		log.debug("going to change/update Score for JobApplication : {}", convertObjectToJson(jobApplication));
		JobApplication jobApplicationUpdated = calculateOverallScoreForJobApplication(jobApplicationsaved);
		log.debug("changed/updated  JobApplication  is : {}", convertObjectToJson(jobApplicationUpdated));
		return jobApplicationUpdated;
	}

	@Override
	public JobApplication findJobApplicantByUserContext() {
		log.info(INSIDE_METHOD, "findJobApplicantByUserContext");
		try {
			ApplicantService applicantService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantService.class);
			log.debug("Customer Info user Id is : {}", customerInfo.getUserId());
			Applicant applicant = applicantService.getApplicantByUserId(customerInfo.getUserId());
			if (applicant != null) {
				log.debug("Applicant get from customerinfo is  : {}", applicant.toString());
				JobApplication jobApplication = jobApplicationRepository.findByApplicantId(applicant.getId());
				return (jobApplication != null ? jobApplication : null);
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new BusinessException("error while finding job applicant by user context", ex.getMessage());
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

	@Override
	public JobApplication calculateOverallScoreForJobApplication(JobApplication jobApplication) {
		log.info(INSIDE_METHOD, "calculateOverallScoreForJobApplication");
		try {
			if (jobApplication != null) {
				log.debug("inside calculateOverallScoreForJobApplication jobApplication  is : {}", jobApplication);
				List<JobApplicationConfigurationScore> jobApplicationConfigurationScoreList = jobApplicationConfigurationScoreRepository
						.findAllJobApplicationConfigurationScore(jobApplication.getId());
				log.debug("inside JobApplicationserviceImpl jobApplicationConfigurationScoreList is : {}",
						jobApplicationConfigurationScoreList);
				if (jobApplicationConfigurationScoreList != null && !jobApplicationConfigurationScoreList.isEmpty()) {
					double totalScore = 0.0;
					for (JobApplicationConfigurationScore score : jobApplicationConfigurationScoreList) {
						log.info("Iterate for jobApplicationConfigurationScoreList");
						if (score != null && score.getIndividualScore() != null) {
							totalScore += score.getIndividualScore();
						}
					}
					log.debug("inside calculateOverallScoreForJobApplication Overallscore  is : {}", totalScore);
					jobApplication.setOverallScore(totalScore);
					return jobApplicationRepository.save(jobApplication);
				}
			} else {
				throw new BusinessException("JobApplication cannot be null");
			}
		} catch (BusinessException ex) {
			throw new BusinessException("error while calculating overall score for JobApplication", ex.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, Object> getTopRankedAndTopReferralApplicantByJobId(@Valid String jobId) {
		log.info(INSIDE_METHOD, "getTopRankedAndTopReferralApplicantByJobId");

		try {
			if (jobId == null) {
				log.error("jobId cannot be null");
				return null;
			}
			JobOpening jobOpening = jobOpeningRepository.getJobOpeningByReferenceId(jobId);
			if (jobOpening != null) {
				setRankingForJobAppicationsById(jobOpening.getId());
				log.debug("Job Opening id is : {}", jobOpening.getId());
			}

			List<JobApplication> jobApplications = new ArrayList<>();
			if (jobOpening != null && jobOpening.getId() != null) {
//				jobApplications = jobApplicationRepository.findJobApplicationsByJobOpeningId(jobOpening.getId());
				log.debug("Inside getTopRankedAndTopReferralApplicantByJobId customerId is : {}",
						commonUtils.getCustomerId());
				jobApplications = jobApplicationRepository.findJobApplicationsByJobOpeningId(jobOpening.getId(), "NEW",
						commonUtils.getCustomerId());
				log.debug("Job Applications by job opening id : {}", jobApplications);
				log.debug("Job Applications size is : {}", jobApplications.size());
			}

			log.debug("Fetch top job Applications");

//			List<JobApplication> topJobApplications = jobApplications.stream()
//					.filter(jobApplication -> jobApplication.getApplicant() != null)
//					.filter(jobApplication -> "Regular".equals(jobApplication.getApplicant().getApplicantType()))
//					.filter(jobApplication -> jobApplication.getOverallScore() != null)
//					.sorted(Comparator.comparingDouble(JobApplication::getOverallScore).reversed()).limit(3)
//					.collect(Collectors.toList());

			List<JobApplication> topJobApplications = jobApplications.stream()
					.filter(jobApplication -> jobApplication.getApplicant() != null) // Ensure applicant is not null
					.filter(jobApplication -> "Regular".equals(jobApplication.getApplicant().getApplicantType())) // Filter
																													// for
																													// applicantType
																													// "Regular"
					.filter(jobApplication -> jobApplication.getRanking() != null) // Ensure ranking is not null
					.sorted(Comparator.comparingInt(JobApplication::getRanking)) // Sort by ranking in ascending order
					.limit(3) // Limit to top 3
					.collect(Collectors.toList());

			log.debug("Top JobApplication list is: {}", topJobApplications);
			log.debug("Top JobApplication list size: {}", topJobApplications.size());

			List<ApplicantDto> applicantDtos = new ArrayList<>();
			List<Integer> topRankedApplicantIds = new ArrayList<>();

			ApplicantRepository applicantRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantRepository.class);

			iterateForTopJobApplication(topJobApplications, applicantDtos, topRankedApplicantIds, applicantRepository);

			List<ApplicantDto> referredApplicantDtos = new ArrayList<>();
			List<Integer> topReferredApplicantIds = new ArrayList<>();

//			List<JobApplication> referredJobApplications = jobApplications.stream()
//					.filter(jobApplication -> jobApplication.getApplicant() != null
//							&& "Internal Referral".equals(jobApplication.getApplicant().getApplicantSource()))
//					.filter(jobApplication -> jobApplication.getOverallScore() != null
//							&& !topRankedApplicantIds.contains(jobApplication.getApplicant().getId()))
//					.sorted(Comparator.comparingDouble(JobApplication::getOverallScore).reversed()).limit(3)
//					.collect(Collectors.toList());

			List<JobApplication> referredJobApplications = jobApplications.stream()
					.filter(jobApplication -> jobApplication.getApplicant() != null)
					.filter(jobApplication -> jobApplication.getApplicant().getApplicantType() != null
							&& ("Referred".equals(jobApplication.getApplicant().getApplicantType())
									|| "Head-Hunted".equals(jobApplication.getApplicant().getApplicantType())))
					.filter(jobApplication -> jobApplication.getOverallScore() != null
							&& !topRankedApplicantIds.contains(jobApplication.getApplicant().getId()))
					.sorted(Comparator.comparingDouble(JobApplication::getOverallScore).reversed()).limit(3)
					.collect(Collectors.toList());

			log.debug("Referred JobApplications list is: {}", referredJobApplications);
			log.debug("Referred JobApplications list size: {}", referredJobApplications.size());

			iterateForReferredJobApplications(applicantRepository, referredApplicantDtos, referredJobApplications,
					topReferredApplicantIds);

			List<JobApplication> otherJobApplications = jobApplications.stream()
					.filter(jobApplication -> jobApplication.getApplicant() != null)
					.filter(jobApplication -> !topRankedApplicantIds.contains(jobApplication.getApplicant().getId())
							&& !topReferredApplicantIds.contains(jobApplication.getApplicant().getId()))
					.sorted(Comparator.comparingDouble(JobApplication::getOverallScore).reversed()).limit(7)
					.collect(Collectors.toList());

			log.debug("others JobApplications list is: {}", otherJobApplications);
			log.debug("othes JobApplications list size: {}", otherJobApplications.size());

			List<ApplicantDto> otherApplicantDtos = new ArrayList<>();

			iterateForOthersJobApplications(applicantRepository, otherApplicantDtos, otherJobApplications);

			Map<String, Object> response = new HashMap<>();
			Map<String, List<ApplicantDto>> topRanked = new HashMap<>();
			topRanked.put(APPLICANT, applicantDtos);
			response.put(TOP_RANKED, topRanked);

			Map<String, List<ApplicantDto>> topReferred = new HashMap<>();
			topReferred.put(APPLICANT, referredApplicantDtos);
			response.put("topReferred", topReferred);

			response.put(OTHERS, Map.of(APPLICANT, otherApplicantDtos));

			return response;
		} catch (BusinessException ex) {
			throw new BusinessException("error while get top ranked and top referral Applicant", ex.getMessage());

		}

	}

	private void iterateForReferredJobApplications(ApplicantRepository applicantRepository,
			List<ApplicantDto> referredApplicantDtos, List<JobApplication> referredJobApplications,
			List<Integer> topReferredApplicantIds) {
		for (JobApplication jobApplication : referredJobApplications) {
			if (jobApplication.getApplicant() != null) {
				log.debug("Inside iterateForReferredJobApplications customerId is : {}", commonUtils.getCustomerId());
				Applicant applicant = applicantRepository.findApplicantById(jobApplication.getApplicant().getId(),
						commonUtils.getCustomerId());
				log.debug("Applicant for Refferal Job Application is: {}", applicant);

				if (applicant != null) {
					ApplicantDto applicantDto = new ApplicantDto();
					setInApplicantDtoForJobApplications(jobApplication, applicant, applicantDto);

					KeyFactsDto keyFactsDto = new KeyFactsDto();
					if (applicant.getUniversity() != null) {
						keyFactsDto.setCollege(applicant.getUniversity());
					}
					if (applicant.getExperienceInYears() != null) {
						keyFactsDto.setYearsOfExperience(applicant.getExperienceInYears());
					}
					List<String> certificates = applicantCertificationsRepository
							.getCertificatesNameByApplicantId(applicant.getId(), commonUtils.getCustomerId());
					if (certificates != null && !certificates.isEmpty()) {
						keyFactsDto.setCertificates(certificates);
					} else {
						keyFactsDto.setCertificates(Collections.emptyList());
					}

					applicantDto.setKeyFacts(Collections.singletonList(keyFactsDto));

					Integer applicantId = jobApplication.getApplicant().getId();
					List<ApplicantExperience> experiences = applicantExperienceRepository
							.getExperienceForApplicant(applicantId, commonUtils.getCustomerId());

					List<ApplicantExperienceDto> experienceDtos = new ArrayList<>();
					iterateForReferredApplicantExperience(experiences, experienceDtos);

					applicantDto.setApplicantExperienceDto(experienceDtos);
					referredApplicantDtos.add(applicantDto);
					topReferredApplicantIds.add(applicant.getId());
				}
			}
		}
	}

	private void setInApplicantDtoForJobApplications(JobApplication jobApplication, Applicant applicant,
			ApplicantDto applicantDto) {
		if (applicant.getFullName() != null) {
			applicantDto.setFullName(applicant.getFullName());
		}
		if (applicant.getApplicantType() != null) {
			applicantDto.setType(applicant.getApplicantType());
		}
		if (jobApplication.getOverallScore() != null) {
			applicantDto.setEducationScore(jobApplication.getOverallScore());
		}

		if (jobApplication.getRanking() != null) {
			applicantDto.setRanking(jobApplication.getRanking());
		}

		if (jobApplication.getJobApplicationId() != null) {
			applicantDto.setJobApplicationId(jobApplication.getJobApplicationId());
		}

		if (jobApplication.getId() != null) {
			applicantDto.setId(jobApplication.getId());
		}

		if (jobApplication.getJobApplicationBatch() != null) {
			applicantDto.setJobApplicationBatch(jobApplication.getJobApplicationBatch());
		}

		log.debug("Inside setInApplicantDtoForJobApplications ApplicationStatus is : {}",
				jobApplication.getApplicationStatus());
		applicantDto.setJobApplicationStatus(jobApplication.getApplicationStatus());

		applicantDto.setPinnedApplication(jobApplication.isPinnedApplication());

	}

	private void iterateForReferredApplicantExperience(List<ApplicantExperience> experiences,
			List<ApplicantExperienceDto> experienceDtos) {
		for (ApplicantExperience experience : experiences) {
			log.debug("Set Reffered Applicant experiences: {}", experience);
			ApplicantExperienceDto experienceDto = new ApplicantExperienceDto();

			if (experience.getWorkStartDate() != null) {
				experienceDto.setWorkStartDate(experience.getWorkStartDate());
			}
			if (experience.getWorkEndDate() != null) {
				experienceDto.setWorkEndDate(experience.getWorkEndDate());
			}

			StringBuilder previousExperienceBuilder = new StringBuilder();

			if (experience.getOccupation() != null && !experience.getOccupation().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getOccupation());
			}
			if (!experience.getOccupation().equalsIgnoreCase("null")
					&& !experience.getCompany().equalsIgnoreCase("null") && experience.getOccupation() != null
					&& experience.getCompany() != null) {
				previousExperienceBuilder.append(" at ");
			}
			if (experience.getCompany() != null && !experience.getCompany().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getCompany());
			}

			if (previousExperienceBuilder.length() > 0) {
				experienceDto.setPreviousExperience(previousExperienceBuilder.toString());
			}

			experienceDtos.add(experienceDto);
		}
	}

	private void iterateForTopJobApplication(List<JobApplication> topJobApplications, List<ApplicantDto> applicantDtos,
			List<Integer> topRankedApplicantIds, ApplicantRepository applicantRepository) {
		for (JobApplication jobApplication : topJobApplications) {
			log.info("Iterating for topJobApplications: {}", topJobApplications);

			if (jobApplication.getApplicant() != null) {
				Applicant applicant = applicantRepository.findApplicantById(jobApplication.getApplicant().getId(),
						commonUtils.getCustomerId());
				log.debug("Applicant for Top Ranked Job Application is: {}", applicant);

				if (applicant != null) {
					ApplicantDto applicantDto = new ApplicantDto();

					setInApplicantDtoForJobApplications(jobApplication, applicant, applicantDto);

					KeyFactsDto keyFactsDto = new KeyFactsDto();
					if (applicant.getUniversity() != null) {
						keyFactsDto.setCollege(applicant.getUniversity());
					}
					if (applicant.getExperienceInYears() != null) {
						keyFactsDto.setYearsOfExperience(applicant.getExperienceInYears());
					}
					log.debug("Inside iterateForTopJobApplication customerId is : {}", commonUtils.getCustomerId());

					List<String> certificates = applicantCertificationsRepository
							.getCertificatesNameByApplicantId(applicant.getId(), commonUtils.getCustomerId());
					if (certificates != null && !certificates.isEmpty()) {
						keyFactsDto.setCertificates(certificates);
					} else {
						keyFactsDto.setCertificates(Collections.emptyList());
					}

					applicantDto.setKeyFacts(Collections.singletonList(keyFactsDto));

					Integer applicantId = jobApplication.getApplicant().getId();
					List<ApplicantExperience> experiences = applicantExperienceRepository
							.getExperienceForApplicant(applicantId, commonUtils.getCustomerId());

					List<ApplicantExperienceDto> experienceDtos = new ArrayList<>();
					iterateForTopApplicantExperience(experiences, experienceDtos);

					applicantDto.setApplicantExperienceDto(experienceDtos);
					applicantDtos.add(applicantDto);
					topRankedApplicantIds.add(applicant.getId());
				}
			}
		}
	}

	private void iterateForTopApplicantExperience(List<ApplicantExperience> experiences,
			List<ApplicantExperienceDto> experienceDtos) {
		for (ApplicantExperience experience : experiences) {
			log.debug("Set Top Applicant Experience : {}", experience);
			ApplicantExperienceDto experienceDto = new ApplicantExperienceDto();

			if (experience.getWorkStartDate() != null) {
				experienceDto.setWorkStartDate(experience.getWorkStartDate());
			}
			if (experience.getWorkEndDate() != null) {
				experienceDto.setWorkEndDate(experience.getWorkEndDate());
			}

			StringBuilder previousExperienceBuilder = new StringBuilder();

			if (experience.getOccupation() != null && !experience.getOccupation().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getOccupation());
			}
			if (!experience.getOccupation().equalsIgnoreCase("null")
					&& !experience.getCompany().equalsIgnoreCase("null") && experience.getOccupation() != null
					&& experience.getCompany() != null) {
				previousExperienceBuilder.append(" at ");
			}
			if (experience.getCompany() != null && !experience.getCompany().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getCompany());
			}

			if (previousExperienceBuilder.length() > 0) {
				experienceDto.setPreviousExperience(previousExperienceBuilder.toString());
			}

			experienceDtos.add(experienceDto);
		}
	}

	private void iterateForOthersJobApplications(ApplicantRepository applicantRepository,
			List<ApplicantDto> otherApplicantDtos, List<JobApplication> othersJobApplications) {
		for (JobApplication jobApplication : othersJobApplications) {
			if (jobApplication.getApplicant() != null) {

				Applicant applicant = applicantRepository.findApplicantById(jobApplication.getApplicant().getId(),
						commonUtils.getCustomerId());
				log.debug("Applicant for others Job Application is: {}", applicant);

				if (applicant != null) {
					ApplicantDto applicantDto = new ApplicantDto();
					setInApplicantDtoForJobApplications(jobApplication, applicant, applicantDto);

					KeyFactsDto keyFactsDto = new KeyFactsDto();
					if (applicant.getUniversity() != null) {
						keyFactsDto.setCollege(applicant.getUniversity());
					}
					if (applicant.getExperienceInYears() != null) {
						keyFactsDto.setYearsOfExperience(applicant.getExperienceInYears());
					}
					log.debug("Inside iterateForOthersJobApplications customerId is : {}", commonUtils.getCustomerId());

					List<String> certificates = applicantCertificationsRepository
							.getCertificatesNameByApplicantId(applicant.getId(), commonUtils.getCustomerId());
					if (certificates != null && !certificates.isEmpty()) {
						keyFactsDto.setCertificates(certificates);
					} else {
						keyFactsDto.setCertificates(Collections.emptyList());
					}

					applicantDto.setKeyFacts(Collections.singletonList(keyFactsDto));

					Integer applicantId = jobApplication.getApplicant().getId();
					List<ApplicantExperience> experiences = applicantExperienceRepository
							.getExperienceForApplicant(applicantId, commonUtils.getCustomerId());

					List<ApplicantExperienceDto> experienceDtos = new ArrayList<>();
					iterateForOthersApplicantExperience(experiences, experienceDtos);

					applicantDto.setApplicantExperienceDto(experienceDtos);
					otherApplicantDtos.add(applicantDto);
				}
			}
		}
	}

	private void iterateForOthersApplicantExperience(List<ApplicantExperience> experiences,
			List<ApplicantExperienceDto> experienceDtos) {
		for (ApplicantExperience experience : experiences) {
			log.debug("Set others Applicant experiences: {}", experience);
			ApplicantExperienceDto experienceDto = new ApplicantExperienceDto();

			if (experience.getWorkStartDate() != null) {
				experienceDto.setWorkStartDate(experience.getWorkStartDate());
			}
			if (experience.getWorkEndDate() != null) {
				experienceDto.setWorkEndDate(experience.getWorkEndDate());
			}

			StringBuilder previousExperienceBuilder = new StringBuilder();

			if (experience.getOccupation() != null && !experience.getOccupation().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getOccupation());
			}
			if (!experience.getOccupation().equalsIgnoreCase("null")
					&& !experience.getCompany().equalsIgnoreCase("null") && experience.getOccupation() != null
					&& experience.getCompany() != null) {
				previousExperienceBuilder.append(" at ");
			}
			if (experience.getCompany() != null && !experience.getCompany().equalsIgnoreCase("null")) {
				previousExperienceBuilder.append(experience.getCompany());
			}

			if (previousExperienceBuilder.length() > 0) {
				experienceDto.setPreviousExperience(previousExperienceBuilder.toString());
			}

			experienceDtos.add(experienceDto);
		}
	}

	@Override
	public String deleteConfigurationScoreByJobApplicationId(Integer id) {
		log.debug("Inside method deleteJobApplicationConfigurationScoreById id is : {}", id);

		try {
			log.debug("Inside method deleteJobApplicationConfigurationScoreById customerId is : {}",
					commonUtils.getCustomerId());
			List<JobApplicationConfigurationScore> jobApplicationConfigScore = jobApplicationConfigurationScoreRepository
					.findByJobApplicationId(id, commonUtils.getCustomerId());

			log.debug("JobApplicationConfigScore's are  : {}", jobApplicationConfigScore);
			if (!jobApplicationConfigScore.isEmpty() && jobApplicationConfigScore != null) {
				jobApplicationConfigurationScoreRepository.deleteAll(jobApplicationConfigScore);
			}
			List<ConfigurationScore> configurationScore = configurationScoreRepository.findByJobApplicationId(id,
					commonUtils.getCustomerId());

			log.debug("ConfigScore's are  : {}", configurationScore);
			if (configurationScore != null && !configurationScore.isEmpty()) {
				configurationScoreRepository.deleteAll(configurationScore);
			}

			log.info("Going to delete JobApplication");
			jobApplicationRepository.deleteById(id);

			return APIConstants.SUCCESS_JSON;
		} catch (BusinessException ex) {
			throw new BusinessException("Error while deleting Job Application Configurations Scores", ex.getMessage());
		}

	}

	@Override
	public List<JobApplication> setRankingForJobAppicationsById(Integer id) {
		try {
			log.debug("Inside @class  JobApplicationServiceImpl @method setRankingForJobAppicationsById :{}", id);
			if (id != null) {
				log.debug("Inside @class JobApplicationServiceImpl setRankingForJobAppicationsById customerId is : {}",
						commonUtils.getCustomerId());
				List<JobApplication> jobApplicationList = jobApplicationRepository.getJobApplicationById(id,
						commonUtils.getCustomerId());
				List<JobApplication> pinnedJobApplicationList = jobApplicationRepository.getPinnedJobApplicationById(id,
						commonUtils.getCustomerId());
				Integer rank = pinnedJobApplicationList.size() + 1;
				log.debug("The size of Job Application List :{} ", jobApplicationList.size());
				log.debug("The size of pinnedJobApplicationList List :{} ", pinnedJobApplicationList.size());
				List<JobApplication> updatedJobApplications = new ArrayList<>();

//				List<JobApplication> otherJobApplications = jobApplicationList.stream()
//						.filter(jobApplication -> jobApplication.getApplicant() != null)
//						.filter(jobApplication -> jobApplication.getApplicant().getApplicantType() != null)
//						.filter(jobApplication -> "Regular".equals(jobApplication.getApplicant().getApplicantType()))
//						.filter(jobApplication -> !jobApplication.isPinnedApplication()) // Filter for not pinned
//						.filter(jobApplication -> jobApplication.getOverallScore() != null)
//						.filter(jobApplication -> jobApplication.getApplicationStatus() != null)
//						.filter(jobApplication -> !"Interview to be scheduled".equals(jobApplication.getApplicationStatus())
//						.sorted(Comparator.comparing(JobApplication::getOverallScore)).collect(Collectors.toList());

				List<JobApplication> otherJobApplications = jobApplicationList.stream()
						.filter(jobApplication -> jobApplication.getApplicant() != null)
						.filter(jobApplication -> jobApplication.getApplicant().getApplicantType() != null)
						.filter(jobApplication -> "Regular".equals(jobApplication.getApplicant().getApplicantType()))
						.filter(jobApplication -> !jobApplication.isPinnedApplication()) // Filter for not pinned
						.filter(jobApplication -> jobApplication.getOverallScore() != null)
						.filter(jobApplication -> jobApplication.getApplicationStatus() != null)
						.filter(jobApplication -> !"Interview to be scheduled"
								.equals(jobApplication.getApplicationStatus())) // Missing closing parenthesis fixed
																				// here
						.sorted(Comparator.comparing(JobApplication::getOverallScore)) // Sorting step
						.collect(Collectors.toList());

				log.debug("The List is :{} ", otherJobApplications);

				for (int i = otherJobApplications.size() - 1; i >= 0; i--) {

					otherJobApplications.get(i).setRanking(rank);
					;
					rank++;
					log.debug("The ID and Rank is :{} :{} ", otherJobApplications.get(i).getId(),
							otherJobApplications.get(i).getRanking());
					JobApplication jobApplicationupdated = jobApplicationRepository.save(otherJobApplications.get(i));
					updatedJobApplications.add(jobApplicationupdated);

				}
				return updatedJobApplications;

			} else {
				throw new BusinessException("JobOpening Id not Received");
			}
		} catch (Exception e) {
			log.error("Error Inside @class JobApplicationServiceImpl @method setRankingForJobAppicationsById :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public String setOverallScoresAndRankingForJobApplication() {

		log.info("Inside Method @setOverallScoresAndRankingForJobApplication");

		try {
			log.debug(
					"Inside @class JobApplicationServiceImpl setOverallScoresAndRankingForJobApplication customerId is : {}",
					commonUtils.getCustomerId());
			List<JobOpening> jobOpeningList = jobOpeningRepository
					.getActiveJobOpeningsByCreationDate(commonUtils.getCustomerId());

			log.debug("JobOpeningList is : {}", jobOpeningList);

			if (jobOpeningList != null && !jobOpeningList.isEmpty()) {
				for (JobOpening jobOpening : jobOpeningList) {
					log.debug("JobOpening is : {}", jobOpening.getId());
					List<JobApplication> jobApplicationList = jobApplicationRepository
							.findJobApplicationsByJobOpeningId(jobOpening.getId(), "NEW", commonUtils.getCustomerId());

					log.debug("JobApplicationList is : {}", jobApplicationList);
					log.debug("JobApplicationList size is : {}", jobApplicationList.size());

					List<JobApplication> overallScoreNullList = jobApplicationList.stream()
							.filter(jobApplication -> jobApplication.getOverallScore() == null)
							.collect(Collectors.toList());

					log.debug("overallScoreNullList is : {}", overallScoreNullList);
					log.debug("overallScoreNullList size  is : {}", overallScoreNullList.size());

					if (overallScoreNullList != null && !overallScoreNullList.isEmpty()) {
						for (JobApplication jobApplication : overallScoreNullList) {
							log.debug("JobApplication having  Null OverallScore id is  : {}", jobApplication.getId());
							calculateOverallScoreForJobApplication(jobApplication);
						}
					}

					setRankingForJobAppicationsById(jobOpening.getId());
//
//					List<JobApplication> jobApplicationList1 = jobApplicationRepository
//							.findJobApplicationsByJobOpeningId(jobOpening.getId());
//					log.debug("jobApplicationList1 is : {}", jobApplicationList1);
//					log.debug("jobApplicationList1 is : {}", jobApplicationList1.size());
//
//					if (jobApplicationList1 != null && !jobApplicationList1.isEmpty()) {
//						for (JobApplication jobApplication : jobApplicationList1) {
//							log.info("Iterate for jobApplication to set Ranking");
//
//							setRankingForJobAppicationsById(jobApplication.getId());
//
//						}
//					}

				}
				return APIConstants.SUCCESS_JSON;

			}

		} catch (BusinessException ex) {
			throw new BusinessException("Error Inside method  @setOverallScoresAndRankingForJobApplication",
					ex.getMessage());

		}
		return APIConstants.FAILURE_JSON;
	}

	@Override
	public Map<String, Object> setTopRankedAndTopReferralApplicantByJobId(String jobId, String jSon) {
		log.info("Inside Method @setTopRankedAndTopReferralApplicantByPostingTitle");

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Parse the JSON string into a JsonNode
			JsonNode rootNode = objectMapper.readTree(jSon);

			log.debug("rootNode is : {}", rootNode);

			// Persist the updated data to the DB (assuming repository logic)
			persistToDatabase(rootNode);

		} catch (JsonProcessingException e) {
//	            e.printStackTrace();
			log.error("JsonProcessingException : ", e.getMessage());
			throw new BusinessException("JsonProcessingException : " + e.getMessage());
		}

		return getTopRankedAndTopReferralApplicantByJobId(jobId);
	}

	private void persistToDatabase(JsonNode rootNode) {
		log.info("Inside Method @persistToDatabase");

		// Referrred
//	    	 JsonNode topReferredApplicants = rootNode.path("topReferred").path("Applicant");
//	    	    if (topReferredApplicants != null && topReferredApplicants.isArray() && topReferredApplicants.size() > 0) {
//	    	        updateDatabaseForApplicants(topReferredApplicants);
//	    	    }

		JsonNode topRankedApplicants = rootNode.path(TOP_RANKED).path(APPLICANT);
		log.debug("topRankedApplicants is : {}", topRankedApplicants);

		if (topRankedApplicants != null && topRankedApplicants.isArray() && topRankedApplicants.size() > 0) {
			updateDatabaseForApplicants(topRankedApplicants);
		}

		JsonNode otherApplicants = rootNode.path(OTHERS).path(APPLICANT);
		log.debug("otherApplicants is : {}", otherApplicants);
		if (otherApplicants != null && otherApplicants.isArray() && otherApplicants.size() > 0) {
			updateDatabaseForApplicants(otherApplicants);
		}
	}

	private void updateDatabaseForApplicants(JsonNode applicantArray) {
		log.info("Inside Method @updateDatabaseForApplicants");

		if (applicantArray.isArray()) {
			for (JsonNode applicantNode : applicantArray) {
				// Handle null or missing jobApplicationId
				if (applicantNode.has(JOB_APPLICATION_ID) && !applicantNode.get(JOB_APPLICATION_ID).isNull()) {
					String jobApplicationId = applicantNode.path(JOB_APPLICATION_ID).asText();
					log.debug("jobApplicationId is : {}", jobApplicationId);

					JobApplication oldJobApplication = jobApplicationRepository
							.findJobApplicationsByJobApplicationId(jobApplicationId);

					if (oldJobApplication != null) {
						log.debug("oldJobApplication is : {}", oldJobApplication);

						try {
							// Handle null or missing ranking
							setRankingAndPinnedApplicant(applicantNode, jobApplicationId, oldJobApplication);

							// Save updated job application to the database
							jobApplicationRepository.save(oldJobApplication);
						} catch (Exception e) {
							log.error("Error processing job application {}: {}", jobApplicationId, e.getMessage());
							throw new BusinessException("Error updating Job Application with ID: " + jobApplicationId,
									e);
						}
					} else {
						log.error("Job Application not found for jobApplicationId: {}", jobApplicationId);
					}
				} else {
					log.warn("Missing jobApplicationId for applicant: {}", applicantNode);
				}
			}
		} else {
			log.warn("Invalid or missing applicantArray: {}", applicantArray);
		}
	}

	private void setRankingAndPinnedApplicant(JsonNode applicantNode, String jobApplicationId,
			JobApplication oldJobApplication) {
		if (applicantNode.has(RANKING) && !applicantNode.get(RANKING).isNull()) {
			int ranking = applicantNode.path(RANKING).asInt();
			oldJobApplication.setRanking(ranking);
			log.debug("setRanking is : {}", ranking);
		} else {
			log.warn("Missing or null ranking for jobApplicationId: {}", jobApplicationId);
		}

		// Handle null or missing pinnedApplication
		if (applicantNode.has(PINNED_APPLICATION) && !applicantNode.get(PINNED_APPLICATION).isNull()) {
			boolean pinnedApplication = applicantNode.path(PINNED_APPLICATION).asBoolean();
			oldJobApplication.setPinnedApplication(pinnedApplication);
			log.debug("setPinnedApplication is : {}", pinnedApplication);
		} else {
			log.warn("Missing or null pinnedApplication for jobApplicationId: {}", jobApplicationId);
		}
	}

	@Override
	public List<JobApplication> updateBatchForJobApplication(JobApplicationDto jobApplicationDto) {
		log.info("Inside Method @updateBatchForJobApplication");

		try {
			JobApplicationBatch jobApplicationBatch = jobApplicationDto.getJobApplicationBatch();
			List<JobApplication> updatedJobApplications = new ArrayList<>();

			log.debug("Inside Method @updateBatchForJobApplication jobApplicationBatch : {}", jobApplicationBatch);
			List<Integer> jobApplicationIds = jobApplicationDto.getJobApplicationIds();
			log.debug("Inside Method @updateBatchForJobApplication jobApplicationIds : {}", jobApplicationIds);

			List<JobApplication> jobApplications = jobApplicationRepository.findAllById(jobApplicationIds);

			if (jobApplicationBatch == null && jobApplications != null && !jobApplications.isEmpty()) {

				for (int i = 0; i < jobApplications.size(); i++) {
					log.debug("Update Batch Id to Null for Applicants ; {}", jobApplications.get(i));

					JobApplication jobApplication = jobApplications.get(i);
					jobApplication.setJobApplicationBatch(null);
					JobApplication updateJobApplicationBatch = jobApplicationRepository.save(jobApplication);
					updatedJobApplications.add(updateJobApplicationBatch);
				}
				return updatedJobApplications;

			}

			log.debug("JobApplications are : {}", jobApplications);

			if (jobApplications.isEmpty()) {
				log.error(JOB_APPLICATION_NOT_FOUND);
				throw new BusinessException(JOB_APPLICATION_NOT_FOUND);
			}

			if (jobApplications != null && !jobApplications.isEmpty()) {

				for (int i = 0; i < jobApplications.size(); i++) {
					JobApplication jobApplication = jobApplications.get(i);
					jobApplication.setJobApplicationBatch(jobApplicationBatch);
					log.debug("Inside Method @updateBatchForJobApplication jobApplication to be updated : {}",
							jobApplication);
					JobApplication jobApplicationUpdated = jobApplicationRepository.save(jobApplication);
					updatedJobApplications.add(jobApplicationUpdated);

				}
			}

			log.debug("Batch ID set for JOB APPLICATION : {}", updatedJobApplications);
			return updatedJobApplications;

		} catch (BusinessException ex) {

			log.error("Error while updating Batch for JobApplication");
			throw new BusinessException("Error while updating Batch for JobApplication", ex.getMessage());
		}

	}

	@Override
	public List<JobApplication> updateJobApplicationStatus(JobApplicationDto jobApplicationDto) {
		log.info("Inside Method @updateJobApplicationStatus");

		try {

			List<JobApplication> updatedJobApplications = new ArrayList<>();

			List<Integer> jobApplicationIds = jobApplicationDto.getJobApplicationIds();
			log.debug("Inside Method @updateJobApplicationStatus jobApplicationIds : {}", jobApplicationIds);

			List<JobApplication> jobApplications = jobApplicationRepository.findAllById(jobApplicationIds);

			log.debug("JobApplications are : {}", jobApplications);

			if (jobApplications.isEmpty()) {
				log.error(JOB_APPLICATION_NOT_FOUND);
				throw new BusinessException(JOB_APPLICATION_NOT_FOUND);
			}

			if (jobApplications != null && !jobApplications.isEmpty()) {

				for (int i = 0; i < jobApplications.size(); i++) {
					JobApplication jobApplication = jobApplications.get(i);

					if (jobApplicationDto.getApplicantStatus() != null) {
						log.debug("Applicant status is : {}", jobApplicationDto.getApplicantStatus());
						jobApplication.setApplicationStatus(jobApplicationDto.getApplicantStatus());

						if (jobApplication.getApplicant() != null) {

							log.debug("Set Applicant Status for Id : {} ", jobApplication.getApplicant().getId());
							Applicant applicant = jobApplication.getApplicant();
							applicant.setApplicantStatus(jobApplicationDto.getApplicantStatus());
							applicantRepository.save(applicant);

						}

					}

					log.debug("Inside Method @updateBatchForJobApplication jobApplication to be updated : {}",
							jobApplication);
					JobApplication jobApplicationUpdated = jobApplicationRepository.save(jobApplication);
					updatedJobApplications.add(jobApplicationUpdated);
				}

			}

			return updatedJobApplications;

		} catch (BusinessException ex) {

			log.error("Error while updateJobApplicationStatus for JobApplication");
			throw new BusinessException("Error while updateJobApplicationStatus for JobApplication", ex.getMessage());
		}

	}

	@Override
	public String setRankingForJobAppicationsOnRegularBasis() {
		log.info("Inside method setRankingForJobAppicationsOnRegularBasis");
		List<JobApplication> allUpdatedApplications = new ArrayList<>();
		try {
			List<JobOpening> jobOpeningList = jobOpeningRepository.getActiveJobOpening();
			log.debug("Inside method setRankingForJobAppicationsOnRegularBasis jobOpeningList: {} ", jobOpeningList);
			if (jobOpeningList != null && !jobOpeningList.isEmpty()) {
				log.debug("Inside method setRankingForJobAppicationsOnRegularBasis list: {}", jobOpeningList.size());
				for (JobOpening jobOpening : jobOpeningList) {
					if (jobOpening != null) {
						log.debug(" setRankingForJobAppicationsOnRegularBasis jobOpening id : {}", jobOpening.getId());
						List<JobApplication> updatedApplications = setRankingForJobAppicationsById(jobOpening.getId());
						if (updatedApplications != null && !updatedApplications.isEmpty()) {
							log.debug("setRankingForJobAppicationsOnRegularBasis updatedApplications size : {}",
									updatedApplications.size());
							allUpdatedApplications.addAll(updatedApplications);
						}
					}

				}
			}
			log.info("setRankingForJobAppicationsOnRegularBasis total updated job applications: {}",
					allUpdatedApplications.size());
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error("Error occured while setRankingForJobAppicationsOnRegularBasis: {}", e);
		}

		return APIConstants.FAILURE_JSON;
	}

	@Override
	public String setRankingForJobAppicationsPostTwoDays() {
		log.info("Inside method setRankingForJobAppicationsPostTwoDays");
		List<JobApplication> allUpdatedApplications = new ArrayList<>();
		try {
			List<JobOpening> jobOpeningList = jobOpeningRepository
					.getActiveJobOpeningsByCreationDate(commonUtils.getCustomerId());
			log.debug("Inside method setRankingForJobAppicationsPostTwoDays jobOpeningList: {} ", jobOpeningList);
			if (jobOpeningList != null && !jobOpeningList.isEmpty()) {
				log.debug("Inside method setRankingForJobAppicationsPostTwoDays list: {}", jobOpeningList.size());
				for (JobOpening jobOpening : jobOpeningList) {
					if (jobOpening != null) {
						log.debug("setRankingForJobAppicationsPostTwoDays jobOpening id : {}", jobOpening.getId());
						List<JobApplication> updatedApplications = setRankingForJobAppicationsById(jobOpening.getId());
						if (updatedApplications != null && !updatedApplications.isEmpty()) {
							log.debug("setRankingForJobAppicationsPostTwoDays updatedApplications size : {}",
									updatedApplications.size());
							allUpdatedApplications.addAll(updatedApplications);
						}
					}

				}
			}
			log.info("setRankingForJobAppicationsPostTwoDays total updated job applications: {}",
					allUpdatedApplications.size());
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error("Error occured while setRankingForJobAppicationsPostTwoDays: {}", e);
		}

		return APIConstants.FAILURE_JSON;
	}

}
