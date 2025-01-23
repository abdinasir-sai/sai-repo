package com.nouros.hrms.integration.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.document.model.Document;
import com.enttribe.document.model.SubFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
/**
 * The DocumentIntegrationService interface represents a service for managing documents and attachments.
 * This interface defines methods to upload, delete, download, and retrieve documents and their metadata.
 */
public interface DocumentIntegrationService {
	
	/**
     * Uploads a document file and associates it with a reference entity.
     *
     * @param file         The MultipartFile representing the document file to be uploaded.
     * @param fileName     The name of the document file.
     * @param referenceId  The reference ID of the entity to which the document is associated.
     * @param applicatoinName The name of the application to which the entity belongs.
     * @param entityName   The name of the entity to which the document is related.
     * @return The Document object representing the uploaded document and its metadata.
     */
	Document uploadDocument(MultipartFile file, String fileName, String referenceId,String applicatoinName, String entityName);
	
	/**
     * Deletes a document entity attachment based on its document ID.
     *
     * @param documentIdPk The primary key (ID) of the document to be deleted.
     * @return A String message indicating the success or failure of the deletion process.
     */
	String deleteFileEntityAttachment(Integer documentIdPk);

    /**
     * Downloads the attachment associated with a document entity based on its document ID.
     *
     * @param documentIdPk The primary key (ID) of the document to be downloaded.
     * @return A ResponseEntity representing the file attachment to be downloaded.
     */
	ResponseEntity<Document> downloadAttachment(Integer documentIdPk);

	/**
     * Retrieves the count of documents associated with a specific parent entity.
     *
     * @param parentId    The ID of the parent entity to which the documents are associated.
     * @param searchText  The search text used to filter documents by name or content.
     * @return The total count of documents matching the search criteria.
     */
	Integer countOfMyDocuments(Integer parentId, String searchText);
	
	/**
     * Retrieves a list of documents associated with a specific parent entity.
     *
     * @param parentId          The ID of the parent entity to which the documents are associated.
     * @param upperLimit        The upper limit of the result set for pagination.
     * @param lowerLimit        The lower limit of the result set for pagination.
     * @param modifiedTimeType  The type of modification time used for filtering (e.g., "before" or "after").
     * @param modificationtime  The modification time value used for filtering documents by their modified time.
     * @param searchText        The search text used to filter documents by name or content.
     * @return A list of Document objects representing the matching documents.
     */
	 List<Document> getMyDocuments(Integer parentId, Integer upperLimit, Integer lowerLimit,
				String modifiedTimeType, Long modificationtime, String searchText);
			
	/**
	     * Retrieves a SubFolder based on the provided reference type and value.
	     *
	     * @param referenceType  The type of reference (e.g., "folder" or "document") used for filtering.
	     * @param referenceValue The reference value (e.g., folder name or document ID) used for filtering.
	     * @return The SubFolder object representing the matching subfolder, or null if not found.
	     */			
	SubFolder getSubFolderByReferenceValueAndType(String referenceType, String referenceValue);
	
	 Integer uploadProtectedAndUnProtectedFileOnDocument(String fileName, MultipartFile unProtectedFile) 
		        throws IOException, JsonProcessingException, JsonMappingException;

}
