package com.nouros.hrms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.JobBrief;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.repository.JobBriefRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CompensationStructureService;
import com.nouros.hrms.service.JobBriefService;
import com.nouros.hrms.service.JobInterviewQuestionService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class JobBriefServiceImpl extends AbstractService<Integer,JobBrief> implements JobBriefService {

	private static final Logger log = LogManager.getLogger(JobBriefServiceImpl.class);

	private static final String LOG_JOB_BRIEF_SERVICE = "Inside @class JobBriefServiceImpl @Method ";
	String postingTitle = null;
	String departmentName = null;

	@Autowired
	private JobBriefRepository jobBriefRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private CompensationStructureService compensationStructureService;

	@Autowired
	private JobInterviewQuestionService jobInterviewQuestionService;

	public JobBriefServiceImpl(GenericRepository<JobBrief> repository) {
		super(repository, JobBrief.class);
	}

	@Override
	public Map<String, Object> importJobBriefData(MultipartFile wordFile) {
		log.info(LOG_JOB_BRIEF_SERVICE + "importJobBriefData");
		JobBrief jobBrief = null;
		try {
			String response = processWordFileAndExecutePrompt(wordFile);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response).get("response");
			String responseValue = jsonNode.toString();
			String unEscapeJson = unescapeJsonString(responseValue);
			log.debug("Inside @Class importJobBriefData @Method importJobBriefData unEscapeJson:{}", unEscapeJson);
			if (unEscapeJson != null && unEscapeJson.startsWith("\"") && unEscapeJson.endsWith("\"")) {
				unEscapeJson = unEscapeJson.substring(1, unEscapeJson.length() - 1);
			}
			if (unEscapeJson != null)
			{
				jobBrief = processPromptResponse(unEscapeJson);
			}
			return  getDescriptionData(jobBrief);
		} catch (IOException e) {
			String errorMessage = "IOException occurred during importJobBriefData in JobBriefServiceImpl";
		    throw new BusinessException(errorMessage, e);
		} catch (Exception e) {
			String errorMessage = "Exception occured during importJobBriefData in JobBriefServiceImpl";
		    throw new BusinessException(errorMessage, e);
		}

	}

	private String processWordFileAndExecutePrompt(MultipartFile wordFile) throws IOException {
		log.info(LOG_JOB_BRIEF_SERVICE + " processWordFile");
		String fileName = wordFile.getOriginalFilename();
		if (fileName != null) {
			if (fileName.toLowerCase().endsWith(".doc")) {
				String fileContent = extractTextFromDoc(wordFile.getInputStream());
				return vectorIntegrationService.executePrompt(generateJobBriefPrompt(fileContent));
			} else if (fileName.toLowerCase().endsWith(".docx")) {
				String fileContent = extractTextFromDocx(wordFile.getInputStream());
				return vectorIntegrationService.executePrompt(generateJobBriefPrompt(fileContent));
			}
		}
		throw new BusinessException();
	}

	@SuppressWarnings("resource")
	private String extractTextFromDoc(InputStream inputStream) throws IOException {
		log.info(LOG_JOB_BRIEF_SERVICE + " handleDocFile");
		HWPFDocument document = new HWPFDocument(inputStream);
		WordExtractor extractor = new WordExtractor(document);
		StringBuilder contentBuilder = new StringBuilder();
		for (String paragraph : extractor.getParagraphText()) {
			contentBuilder.append(paragraph);
		}
		document.close();
		return contentBuilder.toString();
	}

	private String extractTextFromDocx(InputStream inputStream) throws IOException {
		log.info(LOG_JOB_BRIEF_SERVICE + " handleDocxFile");
		XWPFDocument document = new XWPFDocument(inputStream);
		XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		String content = extractor.getText();
		content = content.trim();
		extractor.close();
		document.close();
		return content;
	}

	private JobBrief processPromptResponse(String promptResponse) {
		log.info(LOG_JOB_BRIEF_SERVICE + " processPromptResponse");
		double cumulativeSalaryRangeFrom = 0.0;
		double cumulativeSalaryRangeTo = 0.0;
		departmentName = extractDepartmentName(promptResponse);
		log.debug(LOG_JOB_BRIEF_SERVICE + "processPromptResponse : departmentName  {} ", departmentName);
		Department department = departmentRepository.findByName(departmentName);
		if (department != null) {
			log.debug(LOG_JOB_BRIEF_SERVICE + "processPromptResponse : department id {} department name {}",
					department.getId(), department.getName());
			List<CompensationStructure> compensationStructures = compensationStructureService
					.findByDepartmentIdAndTitle(department.getId(), department.getName());
			for (CompensationStructure compensationStructure : compensationStructures) {
				cumulativeSalaryRangeFrom = compensationStructure.getAnnualSalaryRangeFrom();
				cumulativeSalaryRangeTo = compensationStructure.getAnnualSalaryRangeTo();
			}
		}
		return addSalaryRangeToPromptResponse(promptResponse, cumulativeSalaryRangeFrom, cumulativeSalaryRangeTo);
	}

	private JobBrief addSalaryRangeToPromptResponse(String promptResponse, double salaryRangefrom,
			double salaryRangeTo) {
		String formattedSalaryRangefrom = (salaryRangefrom != 0.0) ? String.format("%.2f", salaryRangefrom) : "";
		String formattedSalaryRangeTo = (salaryRangeTo != 0.0) ? String.format("%.2f", salaryRangeTo) : "";
		String salaryRangeValue = formattedSalaryRangefrom + " - " + formattedSalaryRangeTo;

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(promptResponse);
			((ObjectNode) jsonNode).put("salaryRange", salaryRangeValue);
			((ObjectNode) jsonNode).putPOJO("interviewQuestion",
					jobInterviewQuestionService.createJobInterviewQuestion(jsonNode, departmentName));
			String updatedJsonString = objectMapper.writeValueAsString(jsonNode);
			return saveJobBrief(updatedJsonString);
		} catch (IOException e) {
			String errorMessage = "IOException occured during addSalaryRangeToPromptResponse in JobBriefServiceImpl";
		    throw new BusinessException(errorMessage, e);
			
		} catch (Exception e) {
			String errorMessage = "Exception occured during addSalaryRangeToPromptResponse in JobBriefServiceImpl";
		    throw new BusinessException(errorMessage, e);
		}

	}

	private JobBrief saveJobBrief(String updatedJsonString) {
		try {
			JobBrief jobBrief = new JobBrief();
			jobBrief.setDescription(updatedJsonString);
			jobBrief.setPostingTitle(postingTitle);
			return jobBriefRepository.save(jobBrief);
		} catch (Exception e) {
			String errorMessage = "Error while saving jobbrief entity in JobBriefServiceImpl";
		    throw new BusinessException(errorMessage, e);
		}

	}

	public static String generateJobBriefPrompt(String fileContent) {
		log.info(LOG_JOB_BRIEF_SERVICE + " generatePrompt");
		String prompt = "**THIS IS A DATA WHICH CONTAINS JOB DESCRIPTIONS** %s \n\nNOTE-: PLEASE GIVE ME A RESPONSE IN GIVEN JSON FORMAT ..ALSO EXTRACT ONLY A RELEVANT INFORMATION FROM ABOVE DETAILS AND PUT INTO BELOW SPECIFIED JSON ONLY\nDO NOT ADD ANY INFO IF IT IS NOT EXIST IN JOB DESCRIPTION\nSAMPLE_JSON -:\n\n{\n\"postingTitle\": \"\",\n\"jobOpeningStatus\": \"Active\",\n\"industry\": [],\n\"dateOpened\": \"\",\n\"jobType\": \"Full-time\",\n\"workExperience\": \"\",\n\"skills\": \"\",\n\"isRemote\": \"\",\n\"descriptionRequirements\": \"Key Responsibilities:\\nSkills and Qualifications:\\nPreferred nationality:\\nWorking Conditions (e.g. Travel requirements):\"\n}\n";

		return String.format(prompt, fileContent);
	}

	private String extractDepartmentName(String jsonInput) {
		JSONObject jobDescription = new JSONObject(jsonInput);
		postingTitle = jobDescription.optString("postingTitle", "");
		String[] postingTitleParts = postingTitle.split(",");
		if (postingTitleParts.length > 1) {
			departmentName = postingTitleParts[1].trim();
		}
		return departmentName;
	}

	public String unescapeJsonString(String jsonString) {
		return StringEscapeUtils.unescapeJson(jsonString);
	}

	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				jobBriefRepository.deleteById(list.get(i));

			}
		}
	}

	@Override
	public JobBrief searchByPostingTitle(String postingTitle) {
		return jobBriefRepository.findByPostingTitle(postingTitle);
	}

	@Override
	public Map<String, Object> getDescriptionFromJobBrief(String postingTitle) {
		try {
			JobBrief jobBrief = jobBriefRepository.findByPostingTitle(postingTitle);
			return getDescriptionData(jobBrief);
		} catch (Exception e) { 
			throw new BusinessException(e);
		}
	}

	private Map<String, Object> getDescriptionData(JobBrief jobBrief)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> resultMap = new HashMap<>();
		if (jobBrief != null) {
			JsonNode jsonNode = objectMapper.readTree(jobBrief.getDescription());
			jsonNode.fields().forEachRemaining(entry -> resultMap.put(entry.getKey(), entry.getValue()));
		} else {
			resultMap.put("errorMsg", "Data is not present for the specified postingTitle");
		}
		return resultMap;
	}

}
