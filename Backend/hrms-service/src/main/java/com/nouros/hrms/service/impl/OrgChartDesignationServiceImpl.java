package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.repository.DesignationRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.OrgChartDesignationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.OrgChartDesignationService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.DesignationSummaryWrapper;

import jakarta.transaction.Transactional;

@Service
public class OrgChartDesignationServiceImpl extends AbstractService<Integer,OrgChartDesignation>
		implements OrgChartDesignationService {

	protected OrgChartDesignationServiceImpl(GenericRepository<OrgChartDesignation> repository) {
		super(repository, OrgChartDesignation.class);
	}

	@Autowired
	private OrgChartDesignationRepository orgChartDesignationRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(OrgChartDesignationServiceImpl.class);

	public static final String RESPONSE = "response";

	@Override
	@TriggerBPMN(entityName = "OrgChartDesignation", appName = "HRMS_APP_NAME")
	public OrgChartDesignation create(OrgChartDesignation orgChartDesignation) {
		log.info("inside @class OrgChartDesignationServiceImpl @method create");
		try {
//			String orgChartDesignationName = orgChartDesignation.getName();
//			int orgChartDesignationDepartID = orgChartDesignation.getDepartment().getId();
//			Designation designation = designationRepository
//					.findDesignationByNameAndDepartmentId(orgChartDesignationName, orgChartDesignationDepartID);
//			if (designation != null && designation.getName() != null && designation.getDepartmentId().getId() != null) {
//				throw new BusinessException(
//						"Cannot create OrgChartDesignation It Already Present With Name In Designation");
//			}

			if (orgChartDesignation.getWorkflowStage() == null) {
				orgChartDesignation.setWorkflowStage("New");
			}

			return orgChartDesignationRepository.save(orgChartDesignation);
		} catch (BusinessException ex) {
			throw new BusinessException("error while create OrgChartDesignation", ex.getMessage());
		}
	}

	@Override
	public OrgChartDesignation update(OrgChartDesignation orgChartDesignation) {
		log.info("inside @class OrgChartDesignationServiceImpl @method update");

		try {
			if (orgChartDesignation.getParentDesignation() != null
					&& orgChartDesignation.getParentDesignation().getId() != null
					&& orgChartDesignation.getId().equals(orgChartDesignation.getParentDesignation().getId())) {

				log.error("OrgChartDesignation Id and ParentId cannot be same");
				throw new BusinessException("OrgChartDesignation Id and ParentId cannot be same");
			}

			return orgChartDesignationRepository.save(orgChartDesignation);

		} catch (BusinessException ex) {
			throw new BusinessException("error while update OrgChartDesignation", ex.getMessage());
		}
	}

	@Override
	public void softDelete(int id) {

		OrgChartDesignation orgChartDesignation = super.findById(id);

		if (orgChartDesignation != null) {
			OrgChartDesignation orgChartDesignation1 = orgChartDesignation;
			orgChartDesignationRepository.save(orgChartDesignation1);
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

	@Transactional
	public String deleteOrgChartDesignationById(Integer id) {
		log.info("inside @class OrgChartDesignationServiceImpl @method deleteOrgChartDesignationById");
		try {
			deleteChildDesignations(id);
			orgChartDesignationRepository.deleteById(id);
			log.info("Successfully deleted OrgChartDesignation with ID: {}", id);
			return APIConstants.SUCCESS_JSON;

		} catch (BusinessException ex) {
			String errorMessage = "Error while deleting OrgChartDesignation by given ID";
			log.error(errorMessage + " with ID: {}", id, ex);
			throw new BusinessException(errorMessage, ex.getMessage());
		}
	}

	@Transactional
	public String deleteOrgChartDesignationByDepartmentId(Integer departmentId) {
		log.info("Inside @class OrgChartDesignationServiceImpl @method deleteOrgChartDesignationByDepartmentId");
		try {
			log.debug(" Inside @deleteOrgChartDesignationByDepartmentId  customerId is : {}", commonUtils.getCustomerId());
			List<OrgChartDesignation> designations = orgChartDesignationRepository.findByDepartmentId(departmentId,commonUtils.getCustomerId());

			if (designations.isEmpty()) {
				log.debug("No designations found for department ID: {}", departmentId);
				return "No designations found for department ID " + departmentId;
			}
			for (OrgChartDesignation designation : designations) {
				deleteChildDesignations(designation.getId());
				orgChartDesignationRepository.deleteById(designation.getId());
				log.debug("Deleted designation with ID: {}", designation.getId());
			}

			return APIConstants.SUCCESS_JSON;
		} catch (BusinessException e) {
			log.error("Error inside deleteOrgChartDesignationByDepartmentId", e);
			throw new BusinessException("Error while deleting designations for department ID " + departmentId,
					e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error inside deleteOrgChartDesignationByDepartmentId", e);
			throw new RuntimeException(
					"Unexpected error while deleting designations for department ID " + departmentId);
		}
	}

	public void deleteChildDesignations(Integer parentId) {
		log.info("inside @class OrgChartDesignationServiceImpl @method deleteChildDesignations");
		try {
			log.debug(" Inside @deleteChildDesignations  customerId is : {}", commonUtils.getCustomerId());
			List<OrgChartDesignation> childDesignations = orgChartDesignationRepository
					.findAllByParentDesignation(parentId,commonUtils.getCustomerId());
			if (childDesignations.isEmpty()) {
				log.debug("No child designations found for parent ID: {}", parentId);
				return;
			}
			for (OrgChartDesignation child : childDesignations) {
				deleteChildDesignations(child.getId());
				orgChartDesignationRepository.deleteById(child.getId());
				log.debug("Deleted child designation with ID: {}", child.getId());
			}
		} catch (BusinessException e) {
			log.error("error inside delete deleteChildDesignations by ID");
			throw new BusinessException("error while deleting ChildDesignations by given ID");
		}
	}

	@Override
	public String generateJobTitleDescriptionByUserInput(DesignationSummaryWrapper designationSummaryWrapper) {
		log.debug("Inside method regenerateGenerateJobTitleByUserInput designationSummaryWrapper is : {}",
				designationSummaryWrapper);
		try {
			return regenerateJobTitleUsingPrompt(designationSummaryWrapper);
		} catch (Exception e) {
			log.error("Error while converting object to JSON", e);
			return "Error While Regenerating Jobtitle";
		}
	}

	private String regenerateJobTitleUsingPrompt(DesignationSummaryWrapper designationSummaryWrapper) {
		log.info("Inside method regenerateJobTitleUsingPrompt");
		String promptJobtitle = "";
		String responseSummary = "";
		try {
			promptJobtitle = getRegeneratedJobTitleDescription(designationSummaryWrapper.getDesignationTitle(),
					designationSummaryWrapper.getUserRequirement());
			responseSummary = vectorIntegrationService.executePrompt(promptJobtitle);
			log.debug("Response Summary is : {}", responseSummary);
		} catch (BusinessException be) {
			log.error("error while Regenerating JobTitleDescription");
		} catch (Exception e) {
			log.error("something went wrong", e);
		}
		String resumeResponseSummary = "";
		JSONObject fullResponseforSummary = new JSONObject(responseSummary);
		if (fullResponseforSummary.has(RESPONSE)) {
			try {
				JSONObject responseSummaryObj = fullResponseforSummary.getJSONObject(RESPONSE);
				if (responseSummaryObj.get("titleDescription") != null) {
					resumeResponseSummary = responseSummaryObj.getString("titleDescription");
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("responseText", resumeResponseSummary);
					return jsonResponse.toString();
				}
			} catch (Exception e) {
				log.error("Unable To Parse Response from Prompt after Regenerating Job Title Description", e);
			}
		}
		return APIConstants.FAILURE_JSON;
	}

	private String getRegeneratedJobTitleDescription(String jobTitle, String userInput) {
		log.info("Inside method getRegeneratedJobTitleDescription");
		String prompt = "You are a helpful AI Assistant designed to generate a detailed description for a job title based on the user query. Below, you will be provided with job title, and user query  in text format. Your task is to generate the job description as described above using the job title provided and change the description according to the user query (if provided by the user).\n"
				+ "Output Requirements:\n"
				+ "Generate a professional detailed description that highlights Key information about the Designation. Mandatory things to include in output is 'responsibilities', 'core capabilities', and 'skills required' in the given sequence in the description that you will generate. The summary should be concise, staying between 2000 to 3000 characters.\n"
				+ "Provide the response in pretty JSON format.\n"
				+ "Adjust it based on the user's input while maintaining a professional tone and third-person perspective.\n"
				+ "The output should be in JSON format with the key 'titleDescription' and the regenerated text as the value.\"\n"
				+ "Input:\n" + "job title : %s\n" + "User Query:  %s";
		return String.format(prompt, jobTitle, userInput);
	}

	@Override
	public List<Employee> getEmployeeByApprovedDesignation(Integer id) {
		log.info("Inside method getEmployeeByApprovedDesignation");

		try {

			if (id == null) {
				throw new BusinessException("OrgchartDesignation Id cannot be null");
			}

			OrgChartDesignation orgChartDesignation = orgChartDesignationRepository.findById(id).orElse(null);

			log.debug("OrgChartDesignation is : {}", orgChartDesignation);

			if (orgChartDesignation != null && orgChartDesignation.getName() != null) {
				log.debug("OrgChartDesignation name is : {}", orgChartDesignation.getName());

				String designationName = orgChartDesignation.getName();
				log.debug("Inside method getEmployeeByApprovedDesignation customerId is : {}", commonUtils.getCustomerId());
				List<Designation> designationList = designationRepository.findByName(designationName, commonUtils.getCustomerId());

				log.debug("designation  is : {}", designationList);

				if (designationList != null && !designationList.isEmpty()) {
					List<Employee> employeeList = new ArrayList<>();

					for (Designation designation : designationList) {
						if (designation != null && designation.getName() != null) {
							List<Employee> employees = employeeRepository.findByDesignationName(designation.getName());
							log.debug("Employees for designation {}: {}", designation.getName(), employees);
							employeeList.addAll(employees);
						}
					}

					log.debug("Final List of Employees is : {}", employeeList);
					return employeeList;
				}
			}

			return Collections.emptyList();

		} catch (BusinessException ex) {
			throw new BusinessException("Error while Fetching Employee By Approved Designation", ex.getMessage());
		}

	}

}
