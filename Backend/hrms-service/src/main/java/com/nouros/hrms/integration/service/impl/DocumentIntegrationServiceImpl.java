package com.nouros.hrms.integration.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.document.model.Document;
import com.enttribe.document.model.SubFolder;
import com.enttribe.document.rest.IDocumentIntegrationRest;
import com.enttribe.document.rest.IDocumentRest;
import com.enttribe.document.rest.IDocumentStreamRest;
import com.enttribe.document.wrapper.SubFolderWrapper;
import com.enttribe.document.wrapper.UploadWrapper;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.wrapper.HttpResponseWrapper;

/**
 * The DocumentIntegrationServiceImpl class is an implementation of the
 * DocumentIntegrationService interface. This class provides methods to manage
 * documents and attachments.
 *
 * It is marked with the Spring Framework's @Service annotation to indicate that
 * this class is a service component and should be automatically discovered and
 * registered as a Spring bean.
 *
 * It also uses the Lombok @Slf4j annotation to automatically generate a logger
 * field named "log". This logger can be used to log messages and simplify
 * logging throughout the class.
 */
@Service
public class DocumentIntegrationServiceImpl implements DocumentIntegrationService {

	private static final Logger log = LogManager.getLogger(DocumentIntegrationServiceImpl.class);

	@Autowired
	private IDocumentStreamRest documentStreamRest;

	@Autowired
	private IDocumentIntegrationRest documentIntegrationRest;

	@Autowired
	private IDocumentRest documentRest;

	public static final String ENTITY_STRING = "ENTITY";

	@Override
	public Document uploadDocument(MultipartFile file, String fileName, String referenceId, String applicatoinName,
			String entityName) {
		log.info("inside upload document");
		try {
			log.info("Getting Parent Folder");
			SubFolder parentfolderByValue = documentIntegrationRest
					.getSubFolderByReferenceValueAndType("APPLICATIONNAME", applicatoinName, null);

			if (parentfolderByValue == null) {
				throw new BusinessException("Application Folder Not Exist");
			}
			log.info("Getting Entity Folder");
			SubFolder entityfolderByValue = documentIntegrationRest.getSubFolderByReferenceValueAndType(ENTITY_STRING,
					entityName, parentfolderByValue.getId());

			if (entityfolderByValue == null) {
				throw new BusinessException("Entity Folder Not Exist");
			}

			String referenceValue = applicatoinName + "_" + entityName + "_" + referenceId;
			UploadWrapper uploadWrapper = new UploadWrapper();
			uploadWrapper.setFileName(fileName);
			uploadWrapper.setIsPublic(true);
			uploadWrapper.setIsRoot(false);
			uploadWrapper.setIsProcessedDocument(false);
			uploadWrapper.setUserIds(null);
			uploadWrapper.setIsVersioningRequired(true);
			uploadWrapper.setRefType(ENTITY_STRING);
			uploadWrapper.setRefValue(referenceValue);

			String folderReferenceValue = entityName + "_" + referenceId;

			log.info("Getting Entity Sub Folder");
			SubFolder entitytSubFolderByValue = documentIntegrationRest.getSubFolderByReferenceValueAndType(
					ENTITY_STRING, folderReferenceValue, entityfolderByValue.getId());

			if (entitytSubFolderByValue == null) {
				entitytSubFolderByValue = createSubFolder(referenceId, folderReferenceValue,
						entityfolderByValue.getId());
			}

			uploadWrapper.setFolderId(entitytSubFolderByValue.getId());
			JSONObject jsonObject = new JSONObject(uploadWrapper);
			log.info("uploadDocument Json Obj {}", jsonObject);
			log.info("uploadDocument Json Obj1 {}", jsonObject);
			if (log.isInfoEnabled()) {
				log.info("uploadDocument Obj2 {}", new Gson().toJson(uploadWrapper));
			}
			return documentStreamRest.uploadFile(file, jsonObject.toString());
		} catch (Exception ex) {
			log.error("", ex);
			throw new BusinessException(ex.getMessage());
		}
	}

	private SubFolder createSubFolder(String referenceId, String referenceValue, Integer parentId) {
		log.info("createSubFolder");
		SubFolderWrapper subFolder = new SubFolderWrapper();
		log.info("SUBFOLDER NAME {}", referenceValue);
		subFolder.setSubFolderName(referenceValue);
		subFolder.setIsPublic(true);
		subFolder.setIsRoot(false);
		subFolder.setIsProcessDocument(true);
		subFolder.setReferenceId((long) (Integer.parseInt(referenceId)));
		subFolder.setReferenceType(ENTITY_STRING);
		subFolder.setReferenceValue(referenceValue);
		subFolder.setParentSubFolderId(parentId);
		return documentIntegrationRest.createFolder(subFolder);
	}

	@Override
	public String deleteFileEntityAttachment(Integer documentIdPk) {
		log.info("deleteFileEntityAttachment documentIdPk:{}", documentIdPk);
		try {
			return documentRest.deleteDocument(documentIdPk);
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity downloadAttachment(Integer documentIdPk) {
		log.info("downloadAttachment documentIdPk:{}", documentIdPk);
		try {
			return documentStreamRest.fileDownload(documentIdPk);
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	@Override
	public Integer countOfMyDocuments(Integer parentId, String searchText) {
		log.info("countOfMyDocuments searchText:{}", searchText);
		try {
			return documentIntegrationRest.countOfMyDocuments(parentId, searchText);
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	@Override
	public List getMyDocuments(Integer parentId, Integer upperLimit, Integer lowerLimit, String modifiedTimeType,
			Long modificationtime, String searchText) {
		log.info("getMyDocuments searchText:{}", searchText);
		try {
			return documentIntegrationRest.getMyDocuments(parentId, upperLimit, lowerLimit, modifiedTimeType,
					modificationtime, searchText);
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	@Override
	public SubFolder getSubFolderByReferenceValueAndType(String referenceType, String referenceValue) {
		log.info("getSubFolderByReferenceIdAndType");
		return documentIntegrationRest.getSubFolderByReferenceValueAndType(referenceType, referenceValue, null);
	}

	
	@Override
	public Integer uploadProtectedAndUnProtectedFileOnDocument(String fileName, MultipartFile unProtectedFile)
			throws IOException, JsonProcessingException, JsonMappingException {
		log.info("Inside @method uploadProtectedAndUnProtectedFileOnDocument");
		String methodUrlDocument = ConfigUtils.getPlainString("METHOD_URL_DOCUMENT") + fileName;
		HttpResponseWrapper httpResponseWrapper = sendPostRequestWithFile(methodUrlDocument, unProtectedFile.getBytes(),
				unProtectedFile);
		String response = httpResponseWrapper.getResponse();
		log.debug("Response fetched is : {} ", response);
		ObjectMapper om = new ObjectMapper();
		JsonNode json = om.readTree(response);
		log.debug("json fetched is : {} ", json);
		Integer docId = json.get("id").asInt();
		log.debug("docId fetched is : {} ", docId);
		return docId;
	}

	public HttpResponseWrapper sendPostRequestWithFile(String methodUrl, byte[] bytes, MultipartFile file) {
		log.info("Inside @method sendPostRequestWithFile");
		HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(methodUrl);
			httpPost.setEntity(createMultipartEntity(bytes, file));
			parseResponseFromClient(httpResponseWrapper, httpClient, httpPost);
			log.debug("HTTP_RESPONSE_WRAPPER :{}", httpResponseWrapper);
		} catch (Exception e) {
			log.error("Exception occurred in sendPostRequestWithFile: {}", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("Error occurred while making POST call", e);
		}
		return httpResponseWrapper;
	}

	private org.apache.http.HttpEntity createMultipartEntity(byte[] bytes, MultipartFile file) {
		return MultipartEntityBuilder.create()
				.addBinaryBody("fileData", bytes, ContentType.APPLICATION_OCTET_STREAM, file.getOriginalFilename())
				.build();
	}

	private void parseResponseFromClient(HttpResponseWrapper httpResponseWrapper, HttpClient httpClient,
			HttpPost httpPost) {
		try {
			HttpResponse response = httpClient.execute(httpPost);
			httpResponseWrapper.setResponse(EntityUtils.toString(response.getEntity()));
			httpResponseWrapper.setStatusCode(response.getStatusLine().getStatusCode());
			log.debug("Response Object :{}", response);
		} catch (IOException e) {
			log.error("IOException occurred in parseResponseFromClient: {}", e.getMessage(), e);
			throw new BusinessException("Error occurred while processing HTTP response", e);
		}
	}

}
