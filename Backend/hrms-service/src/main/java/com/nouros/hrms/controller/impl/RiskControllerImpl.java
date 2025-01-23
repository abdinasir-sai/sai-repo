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
import com.nouros.hrms.controller.RiskController;
import com.nouros.hrms.integration.service.CommentIntegrationService;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.model.Risk;
import com.nouros.hrms.service.RiskService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**

This class represents the implementation of the RiskController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the RiskController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the RiskService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Risk Risk): creates an Risk and returns the newly created Risk.
count(String filter): returns the number of Risk that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Risk that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Risk Risk): updates an Risk and returns the updated Risk.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Risk with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Risk")
//@Tag(name="/Risk",tags="Risk",description="Risk")
public class RiskControllerImpl implements RiskController {

	private static final Logger log = LogManager.getLogger(RiskControllerImpl.class);
  @Autowired
  private RiskService riskService;
  
  @Autowired
  CommentIntegrationService commentIntegrationService;

	@Autowired
	private DocumentIntegrationService documentIntegrationService;
	
  @TriggerBPMN(entityName = "Risk", appName = "HRMS_APP_NAME")
@Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
 //@GenericAnnotation(actionType="CREATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Risk",globalSearchData="riskId, riskOwner",searchTitle="riskTypeId")

  public Risk create(Risk risk) {
	  log.info("inside @class RiskControllerImpl @method create");
    return riskService.createWithNaming(risk);
  }

  @Override
  public Long count(String filter) {
    return riskService.count(filter);
  }

  @Override
  public List<Risk> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return riskService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
// @GenericAnnotation(actionType="UPDATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Risk",globalSearchData="riskId, riskOwner",searchTitle="riskTypeId")
  public Risk update(Risk risk) {
    return riskService.update(risk);
  }

  @Override
  public Risk findById(Integer id) {
    return riskService.findById(id);
  }

  @Override
  public List<Risk> findAllById(List<Integer> id) {
    return riskService.findAllById(id);
  }

 


			 @Override
			public Document uploadDocument(MultipartFile file, String fileName, String referenceId) {
				return documentIntegrationService.uploadDocument(file, fileName,referenceId,"HRMS_APP_NAME","RISK");
			}

			@Override
			public String deleteDocument(@NotNull(message = "Id can not be null") Integer primaryKey) {
				
				return documentIntegrationService.deleteFileEntityAttachment(primaryKey);
			}

			@Override
			public ResponseEntity fileDownload(
					@NotNull(message = "documentId can not be null") @Min(value = 0, message = "documentId must be greater than 0") Integer documentId) {
		
				return documentIntegrationService.downloadAttachment(documentId);
			}

			@Override
			public Integer countOfMyDocuments(Integer parentId, String searchText) {
				
				return documentIntegrationService.countOfMyDocuments(parentId, searchText) ;
			}

			@Override
			public List getMyDocuments(Integer parentId, Integer upperLimit, Integer lowerLimit,
					String modifiedTimeType, Long modificationtime, String searchText) {
				
				return documentIntegrationService.getMyDocuments(parentId, upperLimit, lowerLimit, modifiedTimeType, modificationtime, searchText);
			}
			
				@Override
			public SubFolder getSubFolderByReferenceValueAndType(String referenceType, String referenceValue) {
				return documentIntegrationService.getSubFolderByReferenceValueAndType(referenceType, referenceValue);
			}

		

			@Override
			public List<WorkflowActions> getActions(@NotEmpty Integer id) {
				
				 return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id,"Risk");
			}
   
   
   
   
   
}
