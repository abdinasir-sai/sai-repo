package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.platform.customannotation.annotation.GenericAnnotation;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.AssetController;
import com.nouros.hrms.model.Asset;
import com.nouros.hrms.service.AssetService;

import jakarta.validation.Valid;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**

This class represents the implementation of the AssetController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the AssetController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the AssetService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Asset Asset): creates an Asset and returns the newly created Asset.
count(String filter): returns the number of Asset that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Asset that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Asset Asset): updates an Asset and returns the updated Asset.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Asset with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Asset")

//@Tag(name="/Asset",tags="Asset",description="Asset")
public class AssetControllerImpl implements AssetController {

  private static final Logger log = LogManager.getLogger(AssetControllerImpl.class);

  @Autowired
  private AssetService assetService;
  

	
  @TriggerBPMN(entityName = "Asset", appName = "HRMS_APP_NAME")
@Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
 @GenericAnnotation(actionType="CREATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Asset",globalSearchData="deviceName, deviceSerialNumber",searchTitle="text4")

  public Asset create(Asset asset) {
	log.info("inside @class AssetControllerImpl @method create");
    return assetService.create(asset);
  }

  @Override
  public Long count(String filter) {
    return assetService.count(filter);
  }

  @Override
  public List<Asset> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return assetService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
 @GenericAnnotation(actionType="UPDATE",uniqEntityId="id",annotationName = {"GlobleSearch"}, appName = "HRMS_APP_NAME", entityName = "Asset",globalSearchData="deviceName, deviceSerialNumber",searchTitle="text4")
  public Asset update(Asset asset) {
    return assetService.update(asset);
  }

  @Override
  public Asset findById(Integer id) {
    return assetService.findById(id);
  }

  @Override
  public List<Asset> findAllById(List<Integer> id) {
    return assetService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    assetService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    assetService.softBulkDelete(list);
  }


		

			@Override
			public List<WorkflowActions> getActions(@Valid Integer id) {
				 return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id,"Asset");
			}
   
   
   
   
   
}
