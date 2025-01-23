package com.nouros.hrms.integration.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enttribe.orchestrator.utility.model.Comment;

/**
 * The CommentIntegrationService interface represents a service for managing comments.
 * This interface defines methods to create, update, delete, and search comments.
 */
@Service
public interface CommentIntegrationService  {
	
	/**
     * Creates a new comment.
     *
     * @param comment The Comment object containing the details of the comment to be created.
     * @return The Comment object representing the newly created comment.
     */
		Comment createComment(Comment comment);
	
		/**
	     * Updates an existing comment.
	     *
	     * @param referenceId The reference ID of the comment to be updated.
	     * @param comment     The updated comment content.
	     * @return The Comment object representing the updated comment.
	     */
		Comment updateComment(String referenceId, String comment);
		
		/**
	     * Deletes a comment based on its reference ID.
	     *
	     * @param referenceId The reference ID of the comment to be deleted.
	     * @return A ResponseEntity with a success message if the comment was deleted successfully,
	     *         or an error message if the comment was not found or couldn't be deleted.
	     */
		ResponseEntity<String> deleteCommentByReferenceId(String referenceId);
		
		/**
	     * Retrieves the total count of search records based on the provided query.
	     *
	     * @param query The search query used to filter comments.
	     * @return The total count of comments matching the search query.
	     */
		Integer getSearchRecordsCount(String query);
		
		/**
	     * Searches for comments based on the provided query and other optional parameters.
	     *
	     * @param query      The search query used to filter comments.
	     * @param lowerLimit The lower limit of the result set for pagination.
	     * @param upperLimit The upper limit of the result set for pagination.
	     * @param orderBy    The field used for sorting the results.
	     * @param orderType  The order type (e.g., "asc" for ascending, "desc" for descending).
	     * @return A list of Comment objects representing the search results.
	     */
		List<Comment> searchComment(String query, Integer lowerLimit, Integer upperLimit, String orderBy,
				String orderType);
	
}
