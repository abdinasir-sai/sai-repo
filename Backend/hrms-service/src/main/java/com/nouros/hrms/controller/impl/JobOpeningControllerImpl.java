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
import com.nouros.hrms.controller.JobOpeningController;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.wrapper.JobOpeningAndApplicantReferralDto;
import com.nouros.hrms.wrapper.JobOpeningDto;
import com.nouros.hrms.wrapper.JobOpeningIdDto;
import com.nouros.hrms.wrapper.JobOpeningSchedulerDto;
import com.nouros.hrms.wrapper.JobOpeningWrapper;
import com.nouros.hrms.wrapper.JobOpeningsDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the JobOpeningController
 * interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the JobOpeningController. It is also annotated with @Api to provide metadata
 * for the Swagger documentation. The class also uses Lombok's @Slf4j annotation
 * to automatically generate a logger field named "log" that is used to log
 * method calls and results. The class fields include an instance of the
 * JobOpeningService bean, which is injected by Spring using the @Autowired
 * annotation. The class implements the following methods: create(JobOpening
 * JobOpening): creates an JobOpening and returns the newly created JobOpening.
 * count(String filter): returns the number of JobOpening that match the
 * specified filter. search(String filter, Integer offset, Integer size, String
 * orderBy, String orderType): returns a list of JobOpening that match the
 * specified filter, sorted according to the specified orderBy and orderType.
 * update(JobOpening JobOpening): updates an JobOpening and returns the updated
 * JobOpening.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of JobOpening with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/JobOpening")
//@Tag(name="/JobOpening",tags="JobOpening",description="JobOpening")
public class JobOpeningControllerImpl implements JobOpeningController {

	private static final Logger log = LogManager.getLogger(JobOpeningControllerImpl.class);

	@Autowired
	private JobOpeningService jobOpeningService;

	@Autowired
	private DocumentIntegrationService documentIntegrationService;

	@TriggerBPMN(entityName = "JobOpening", appName = "HRMS_APP_NAME")
	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	@GenericAnnotation(actionType = "CREATE", uniqEntityId = "id", annotationName = {
			"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "JobOpening", globalSearchData = "postingTitle, jobOpeningStatus, salaryRange", searchTitle = "jobId")
	public JobOpening create(JobOpening jobOpening) {
		log.info("inside @class JobOpeningControllerImpl @method create");
		return jobOpeningService.createWithNaming(jobOpening);
	}

	@Override
	public Long count(String filter) {
		return jobOpeningService.count(filter);
	}

	@Override
	public List<JobOpening> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return jobOpeningService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	@GenericAnnotation(actionType = "UPDATE", uniqEntityId = "id", annotationName = {
			"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "JobOpening", globalSearchData = "postingTitle, jobOpeningStatus, salaryRange", searchTitle = "jobId")
	public JobOpening update(JobOpening jobOpening) {
		return jobOpeningService.update(jobOpening);
	}

	@Override
	public JobOpening findById(Integer id) {
		return jobOpeningService.findById(id);
	}

	@Override
	public List<JobOpening> findAllById(List<Integer> id) {
		return jobOpeningService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		jobOpeningService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		jobOpeningService.softBulkDelete(list);
	}

	@Override
	public Document uploadDocument(MultipartFile file, String fileName, String referenceId) {
		return documentIntegrationService.uploadDocument(file, fileName, referenceId, "HRMS_APP_NAME", "JOBOPENING");
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
				.getWorkflowActions(id, "JobOpening");
	}

	@Override
	public List<Applicant> findSuitableApplicantByJobOpeningId(Integer id) {
		return jobOpeningService.findSuitableApplicantByJobOpeningId(id);
	}

	@Override
	public JobOpening findJobOpeningByUserLogin() {
		return jobOpeningService.findJobOpeningByUserLogin();
	}

	@Override
	public List<JobOpeningDto> getJobOpeningByActiveWorkFlowStage() {
		return jobOpeningService.getJobOpeningByActiveWorkFlowStage();
	}

	@Override
	public String referApplicantForJobopening(JobOpeningWrapper jobOpeningWrapper) {
		return jobOpeningService.referApplicantForJobopening(jobOpeningWrapper);
	}

	@Override
	public JobOpeningSchedulerDto getActiveJobOpening() {
		return jobOpeningService.getActiveJobOpening();
	}

	@Override
	public JobOpeningSchedulerDto getActiveJobOpeningByDepartment(Integer departmentId) {
		return jobOpeningService.getActiveJobOpeningByDepartment(departmentId);
	}

	@Override
	public JobOpening createJobOpening(JobOpeningAndApplicantReferralDto jobOpeningAndApplicantReferralDto) {
		return jobOpeningService.createJobOpening(jobOpeningAndApplicantReferralDto);
	}

	@Override
	public JobOpeningsDto getApplicantCountByActiveJobOpening(String isCritical,
			String departmentName, String postingTitleName, String postingTitleJobLevel) {
		return jobOpeningService.getApplicantCountByActiveJobOpening(isCritical,departmentName,postingTitleName,postingTitleJobLevel);
	}



			@Override
			public JobOpeningIdDto getJobOpeningDetailByJobId(String jobId) {
				return jobOpeningService.getJobOpeningDetailByJobId(jobId);
			}
			
}
