package com.nouros.hrms.service.impl;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.CoreConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.JobInterviewQuestion;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.repository.JobInterviewQuestionRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.JobInterviewQuestionService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class JobInterviewQuestionServiceImpl extends AbstractService<Integer,JobInterviewQuestion>
		implements JobInterviewQuestionService {

	public JobInterviewQuestionServiceImpl(GenericRepository<JobInterviewQuestion> repository) {
		super(repository, JobInterviewQuestion.class);
	}
 
	private static final Logger log = LogManager.getLogger(JobInterviewQuestionServiceImpl.class);

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private JobInterviewQuestionRepository jobInterviewQuestionRepoSitory;

	@Override
	@Transactional
	public String createJobInterviewQuestion(JsonNode jobBrief, String departmentName) {
		try {
			String interviewQuestions = null; 
			log.info(
					"Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion with departmentName as parameter");
			String industry = getValue(jobBrief, "industry", "No industry specified");
			String skills = getValue(jobBrief, "skills", "No skills specified");
			String descriptionRequirements = getValue(jobBrief, "descriptionRequirements",
					"No description requirements specified");

			String response = vectorIntegrationService
					.executePrompt(generatePrompt(departmentName, industry, skills, descriptionRequirements));
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response).get("response");
			String responseValue = jsonNode.toString();
			String unEscapeJson = unescapeJsonString(responseValue);
			log.debug(
					"Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion unEscapeJson:{}",
					unEscapeJson);
			if (unEscapeJson.startsWith("\"") && unEscapeJson.endsWith("\"")) {
				unEscapeJson = unEscapeJson.substring(1, unEscapeJson.length() - 1);
			}

			ObjectMapper objectMapperNew = new ObjectMapper();
			JsonNode jsonNodeArray = objectMapperNew.readTree(unEscapeJson);

			interviewQuestions = jsonNodeArray.get("interviewQuestion").asText();
			log.info("Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion");
			return interviewQuestions;
		} catch (Exception e) {
			 String errorMessage = "Error inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion";
			    throw new BusinessException(errorMessage, e);
		}

	}

	@Override
	@Transactional
	public String createJobInterviewQuestion(JobOpening jobOpening) {
		try {
			log.info(
					"Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion with job opening as parameter");
			String response = vectorIntegrationService
					.executePrompt(generatePrompt(jobOpening.getDepartment().getName(), jobOpening.getIndustry(),
							jobOpening.getSkills(), jobOpening.getDescriptionRequirements()));
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response).get("response");
			String responseValue = jsonNode.toString();
			String unEscapeJson = unescapeJsonString(responseValue);
			log.debug(
					"Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion unEscapeJson:{}",
					unEscapeJson);
			if (unEscapeJson.startsWith("\"") && unEscapeJson.endsWith("\"")) {
				unEscapeJson = unEscapeJson.substring(1, unEscapeJson.length() - 1);
			}
 
			ObjectMapper objectMapperNew = new ObjectMapper();
			JsonNode jsonNodeArray = objectMapperNew.readTree(unEscapeJson);
			JsonNode questionsNode = jsonNodeArray.get("questions");
			if (questionsNode == null || !questionsNode.isArray()) {
		            log.error("No 'questions' field found or it is not an array");
		         return CoreConstants.FAILURE_JSON;
		    }
			for (JsonNode questionNode : questionsNode) { 
				JobInterviewQuestion interviewQuestion = new JobInterviewQuestion();
				interviewQuestion.setQuestion(questionNode.get("question").asText());
				interviewQuestion.setJobOpening(jobOpening);
				jobInterviewQuestionRepoSitory.save(interviewQuestion);
				log.info("Inside @Class JobInterviewQuestionServiceImpl @Method createJobInterviewQuestion");
		      	}
			return CoreConstants.SUCCESS_JSON;
		} catch (Exception e) {
		 throw new BusinessException("An error occurred while processing the job interview question.inside @class JobInterviewQuestionServiceImpl  ", e);
		}
		
	}

	public static String unescapeJsonString(String jsonString) {
		return StringEscapeUtils.unescapeJson(jsonString);
	}

	public static String generatePrompt(String department, String domain, String skill, String jobDescription) {
		log.info("Inside @Class JobInterviewQuestionServiceImpl @Method generatePrompt");
		String prompt = "Given the following information: Department: %s Domain: %s Skills: %s Job Description: %s Identify Skill set and job description based on provided information and provide interview questions. Output Format: Generate a JSON object, like the example below: {\n\"interviewQuestion\": \"<ul>\n<li>Can you explain your experience with data governance and how you have established and governed key areas within a corporate data strategy?</li>\n<li>Have you rolled out and managed an enterprise-wide data governance framework before? If so, can you provide examples of how you achieved this?</li>\n<li>How have you led the adoption of corporate data standards and guidelines in previous roles?</li>\n<li> ADD MORE QUESTIONS </ul>\",\n\"questions\": [\n{\"question\": \"\", \"skill_match\": []},\n{\"question\": \"\", \"skill_match\": [\"\"]}\n]\n}\nExample of Qs: \n1. Did you face stress and pressure situations during your work at XXX? How did you handle it and stayed motivated?\n2. Based on your experience and skills, how will you approach [specific task or responsibility mentioned in the job brief]?\n3. Can you provide examples of projects where you successfully applied [relevant technical skill] to achieve significant results?\n4. How have your past experiences at XXX prepared you for the responsibilities outlined in the job brief?\n5. Can you share an example of a time at XXX when you had to adapt to a new company culture, and how did you handle it?\nNOTE: ATLEAST 10-15 DIMENSIONS ARE NECESSARY. OUTPUT SHOULD BE IN THE FORM OF BULLET POINTS WITH HTML FORMATTING. OVERALL SKILLS MATCH IS MANDATORY IN DIMENSIONS. REMEMBER interviewQuestion KEY WILL HAVE THE QUESTION GENERATED BASED ON SKILL SET AND JOB DESCRIPTION BASED ON PROVIDED INFORMATION AND PROVIDED INTERVIEW QUESTIONS.";

		
		return String.format(prompt, department, domain, skill, jobDescription);
	}

	private static String getValue(JsonNode jsonNode, String fieldName, String fallbackValue) {
		JsonNode fieldNode = jsonNode.get(fieldName);
		return (fieldNode != null && fieldNode.isValueNode()) ? fieldNode.asText() : fallbackValue;
	}
}
