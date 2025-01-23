package com.nouros.hrms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.utils.ProductApiResponses;
import com.enttribe.document.model.Document;
import com.enttribe.document.model.SubFolder;
import com.enttribe.document.utils.Views;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonView;
import com.nouros.hrms.model.Risk;
import com.nouros.hrms.util.APIConstants;

import feign.Headers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * The interface RiskController defines several endpoints that can be accessed using the Spring Web's FeignClient to interact with the service related to Risk.

FeignClient annotation is used to create a client-side load balancer, in this case to call the Employee service, and the url attribute is populated with the {Risk.url} property.

ApiOperation annotation describes the operation the endpoint performs and details the response, authorizations and other information about the endpoint.

It defines the following endpoints:

create(@Valid @RequestBody Employee employee) : creates a new Risk with the given request body and returns the created Risk.
count(@RequestParam(name = APIConstants.QUERY, required = false) String filter) : returns the count of Risk with the RSQL query in the filter parameter.
search(@RequestParam(name = APIConstants.QUERY, required = false) String filter, @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset, @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size, @RequestParam(name = APIConstants.SORT, required = false) String sort): searches for the list of Risk based on the RSQL filter and returns the list of Risk matching the criteria with the pagination and sorting information.
findById(@PathVariable("id") Long id) : searches for the Risk by ID and returns the Risk if found.
update(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) : updates the existing Risk with the given id with the request body and returns the updated Risk.
delete(@PathVariable("id") Long id) : deletes the Risk with the given id and returns the deleted Risk.
It also includes some other annotations like @ApiResponse, @Parameter, @Authorization, @AuthorizationScope which are used to provide more details about the endpoint.
 * 
 * 
 * 
 * 
 * */
@ResponseBody
@FeignClient(name = "RiskController", url = "${hrms.url}", path = "/Risk", primary = false)
public interface RiskController {

  @Operation(summary = "Creates a new Risk",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
  Risk create(@Valid @RequestBody Risk risk);

  @Operation(summary = "To get the count with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "count")
  Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

  @Operation(summary = "To get the list of Risk with RSQL supported filter",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "search")
  List<Risk> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
      @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
      @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
      @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
      @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

  @Operation(summary = "To update the given Risk",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_WRITE"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
  Risk update(@Valid @RequestBody Risk risk);

 

  @Operation(summary = "To get Risk by Id",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findById")
  Risk findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

  @Operation(summary = "To get all Risk by given Ids",  security = {
      @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = {
          "ROLE_API_RISK_READ"})
      })
  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
  @GetMapping(path = "findAllById")
  List<Risk> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);


	@PostMapping(path = "uploadDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Headers("Content-Type: multipart/form-data")
	@ProductApiResponses
	Document uploadDocument(
			@RequestParam("filedata") MultipartFile file,
	          @RequestParam(required = false, name = "filename") String fileName,
	  		  @RequestParam(required = false, name = "referenceId") String referenceId);

	@GetMapping(value = "deleteFromUploadWindow/{id}")
	@Operation(summary = "Delete Document By DocumentId",  security = {
			@SecurityRequirement(name  = "default", scopes = {
					"ROLE_API_RISK_DOCUMENT_WRITE"}) })
	@ProductApiResponses
	String deleteDocument(@PathVariable(name = "id") @NotNull(message = "Id can not be null") Integer primaryKey);

	@GetMapping(value = "/fileDownload"/* , consumes = "application/octet-stream" */)
	@Operation(summary = "Download Single Document Common api",  security = {
			@SecurityRequirement(name = "default", scopes = {
					"ROLE_API_RISK_DOCUMENT_READ"}) })
	@ProductApiResponses
	ResponseEntity<Document> fileDownload(
			@RequestParam(required = false, name = "documentId") @NotNull(message = "documentId can not be null") @Min(value = 0, message = "documentId must be greater than 0") Integer documentId);

	@Operation(summary = "Get Total Count Of My Documents Folder And Files",  security = {
			@SecurityRequirement(name = "default", scopes = {
					"ROLE_API_RISK_DOCUMENT_READ"}) })
	@ProductApiResponses
	@GetMapping(value = "countOfMyDocuments")
	Integer countOfMyDocuments(@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(required = false, name = "searchText") String searchText);

	@Operation(summary = "Get My Documents Folder And Files",  security = {
			@SecurityRequirement(name = "default", scopes = {
					"ROLE_API_RISK_DOCUMENT_READ"}) })
	@ProductApiResponses
	@JsonView(value = { Views.NoView.class })
	@GetMapping(value = "getMyDocuments", produces = "application/json")
	List<Document> getMyDocuments(@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(required = false, name = "upperLimit") Integer upperLimit,
			@RequestParam(required = false, name = "lowerLimit") Integer lowerLimit,
			@RequestParam(required = false, name = "modifiedTimeType") String modifiedTimeType,
			@RequestParam(required = false, name = "modificationtime") Long modificationtime,
			@RequestParam(required = false, name = "searchText") String searchText);
			
			 @GetMapping(value = "getSubFolderByReferenceValueAndType")
	  @Operation(summary = "get folder by reference value and/or reference type",  security = {
	      @SecurityRequirement(name  = "default", scopes = {
	          "ROLE_API_RISK_DOCUMENT_READ"})
	  })
	  @ProductApiResponses
	  SubFolder getSubFolderByReferenceValueAndType(@RequestParam(name = "referenceType") String referenceType,
	      @RequestParam(name = "referenceValue") String referenceValue);
   
   
   	  @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
		  @GetMapping(path = "getActions")
		  List<WorkflowActions> getActions(
		      @Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);
   
     
     
   
   
   
   
     
}
