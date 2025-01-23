package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.document.model.Document;
import com.enttribe.document.model.SubFolder;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.platform.customannotation.annotation.GenericAnnotation;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ApplicantController;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.wrapper.ApplicantPromptWrapper;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.EmployeePopulateDto;
import com.nouros.hrms.wrapper.ProfessionalSummaryWrapper;
import com.nouros.hrms.wrapper.WorkExperienceSummaryWrapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the ApplicantController
 * interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the ApplicantController. It is also annotated with @Api to provide metadata
 * for the Swagger documentation. The class also uses Lombok's @Slf4j annotation
 * to automatically generate a logger field named "log" that is used to log
 * method calls and results. The class fields include an instance of the
 * ApplicantService bean, which is injected by Spring using the @Autowired
 * annotation. The class implements the following methods: create(Applicant
 * Applicant): creates an Applicant and returns the newly created Applicant.
 * count(String filter): returns the number of Applicant that match the
 * specified filter. search(String filter, Integer offset, Integer size, String
 * orderBy, String orderType): returns a list of Applicant that match the
 * specified filter, sorted according to the specified orderBy and orderType.
 * update(Applicant Applicant): updates an Applicant and returns the updated
 * Applicant.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of Applicant with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/Applicant")

//@Tag(name="/Applicant",tags="Applicant",description="Applicant")
public class ApplicantControllerImpl implements ApplicantController {

	private static final Logger log = LogManager.getLogger(ApplicantControllerImpl.class); 

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private DocumentIntegrationService documentIntegrationService;

	@TriggerBPMN(entityName = "Applicant", appName = "HRMS_APP_NAME")
	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "Create Applicant using naming")
	 @GenericAnnotation(actionType="CREATE",uniqEntityId="id",annotationName =
	 {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName =
	 "Applicant",globalSearchData="firstName,lastName,emailId,arabicFirstName,arabicMiddleName,arabicLastName",searchTitle="fullName")
	public Applicant create(Applicant applicant) {
		log.info("inside @class ApplicantControllerImpl @method create");
		return applicantService.create(applicant);
	}

	@Override
	public Long count(String filter) {
		return applicantService.count(filter);
	}

	@Override
	public List<Applicant> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return applicantService.search(filter, offset, size, orderBy, orderType);
	}

	@TriggerBPMN(entityName = "Applicant", appName = "HRMS_APP_NAME")
	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE Applicant")
	@GenericAnnotation(actionType = "UPDATE", uniqEntityId = "id", annotationName = {
			"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "Applicant", globalSearchData ="firstName,lastName,emailId,arabicFirstName,arabicMiddleName,arabicLastName", searchTitle = "fullName")
	public Applicant update(Applicant applicant) {
		return applicantService.update(applicant);
	}

	@Override
	public Applicant findById(Integer id) {
		return applicantService.findById(id);
	}

	@Override
	public List<Applicant> findAllById(List<Integer> id) {
		return applicantService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		applicantService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		applicantService.softBulkDelete(list);
	}

	
	@Override
	public Document uploadDocument(MultipartFile file, String fileName, String referenceId) {
		return documentIntegrationService.uploadDocument(file, fileName, referenceId, "HRMS_APP_NAME", "APPLICANT");
	}

	@Override
	public String deleteDocument(@NotNull(message = "Id can not be null") Integer primaryKey) {
		return documentIntegrationService.deleteFileEntityAttachment(primaryKey);
	}

	@Override
	public ResponseEntity<Document> fileDownload(
			@NotNull(message = "documentId can not be null") @Min(value = 0, message = "documentId must be greater than 0") Integer documentId) {
		return documentIntegrationService.downloadAttachment(documentId);
	}

	@Override
	public Integer countOfMyDocuments(Integer parentId, String searchText) {
		return documentIntegrationService.countOfMyDocuments(parentId, searchText);
	}

	@Override
	public List<Document> getMyDocuments(Integer parentId, Integer upperLimit, Integer lowerLimit,
			String modifiedTimeType, Long modificationtime, String searchText) {
		return documentIntegrationService.getMyDocuments(parentId, upperLimit, lowerLimit, modifiedTimeType,
				modificationtime, searchText);
	}

	@Override
	public SubFolder getSubFolderByReferenceValueAndType(String referenceType, String referenceValue) {
		return documentIntegrationService.getSubFolderByReferenceValueAndType(referenceType, referenceValue);
	}

	@Override
	public List<WorkflowActions> getActions(@Valid Integer id) {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "Applicant");
	}

	@Override
	public String compareApplicantsForJobOpening(@Valid Integer applicant1Id, @Valid Integer applicant2Id,
			@Valid Integer jobOpeningId) {
		return applicantService.compareApplicantsForJobOpening(applicant1Id, applicant1Id, jobOpeningId);
	}

	@Override
	public String applicantRecommendationForJobOpening(@Valid Integer applicantId, @Valid Integer jobOpeningId) {
		return applicantService.applicantRecommendationForJobOpening(applicantId, jobOpeningId);
	}

	@Override
	public List<JobOpening> findSuitableJobOpeningByApplicantId(@Valid Integer applicantId) {
		return applicantService.findSuitableJobOpeningByApplicantId(applicantId);
	}
	
	@Override
	public List<JobOpening> findSuitableJobOpeningByApplicantObject(ApplicantWrapper applicantWrapper) {
		return applicantService.findSuitableJobOpeningByApplicantObject(applicantWrapper);
	}

	@Override
	public List<Object[]> reviewTheSalaryForJobOfferPositionAndCandidate() {
		return applicantService.reviewTheSalaryForJobOfferPositionAndCandidate();
	}

	@Override
	public List<Object[]> reviewJobOfferForPositionAndCandidate() {
		return applicantService.reviewJobOfferForPositionAndCandidate();
	}

	@Override
	public List<Object[]> getApplicantIdByCreatedDate() {
		return applicantService.findApplicantByCreatedDate();
	}
	
	@Override
	public Applicant createApplicant(ApplicantWrapper applicantWrapper) {
        return applicantService.createApplicant(applicantWrapper);
}

	@Override
	public ApplicantPromptWrapper updateApplicant(Integer documentId,String jobId) {
		return applicantService.updateApplicantWithResume(documentId,jobId);
	}

	@Override
	public ApplicantPromptWrapper updateApplicantAfterResumeDetailsSet(ApplicantPromptWrapper applicantPromptWrapper) {
		return applicantService.updateApplicantAfterResumeDetailsSet(applicantPromptWrapper);
	}
	
	@Override
	public ApplicantPromptWrapper updateApplicantFromResumeFolder() {
		return applicantService.updateApplicantFromResumeFolder();
	}
	

	@Override
	public String regenerateProfessionalSummary(ProfessionalSummaryWrapper professionalSummaryWrapper) {
		return applicantService.regenerateProfessionalSummary(professionalSummaryWrapper);
	}

	@Override
	public String regenerateWorkExperienceSummary(WorkExperienceSummaryWrapper workExperienceSummaryWrapper) {
		return applicantService.regenerateWorkExperienceSummary(workExperienceSummaryWrapper);
	}
	
	@TriggerBPMN(entityName = "Employee", appName = "HRMS_APP_NAME")
	@GenericAnnotation(actionType = "CREATE", uniqEntityId = "id", annotationName = {
	"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "Employee", globalSearchData = "firstName, lastName, workEmailAddress , givenName, arabicFirstName, arabicLastName , arabicMiddleName ", searchTitle = "fullName")
	@Override
	public EmployeePopulateDto populateApplicantToEmployee(String processInstanceId, Integer applicantId) {
		return applicantService.populateApplicantToEmployee(processInstanceId, applicantId);
	}

	@Override
	public String deleteApplicantCorrespondingData(@Valid Integer id) {
		return applicantService.deleteApplicantCorrespondingData(id);
	}
	

	
	
	
}
