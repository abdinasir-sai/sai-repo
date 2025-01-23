package com.nouros.hrms.integration.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.orchestrator.utility.controller.ICommentController;
import com.enttribe.orchestrator.utility.model.Comment;
import com.nouros.hrms.integration.service.CommentIntegrationService;


@Service 

public class CommentIntegrationServiceImpl implements CommentIntegrationService{

		@Autowired
	 private ICommentController commentController;
	
	@Override
	public Comment createComment(Comment comment) {
		try {
			return commentController.createComment(comment);

		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
	
	@Override
	public Comment updateComment(String referenceId, String comment) {
		try {
			return commentController.updateComment(referenceId, comment);

		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
	
		@Override
	public ResponseEntity<String> deleteCommentByReferenceId(String referenceId) {
		try {
		
                  commentController.deleteCommentByReferenceId(referenceId);
                 return null;
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}		
	

	@Override
	public Integer getSearchRecordsCount(String query) {	
		try {
		return  commentController.getSearchRecordsCount(query);

	} catch (Exception ex) {
		throw new BusinessException(ex.getMessage());
	}
		}

	@Override
	public List<Comment> searchComment(String query, Integer lowerLimit, Integer upperLimit, String orderBy,
			String orderType) {
		try {
				return  commentController.search(query, lowerLimit, upperLimit, orderBy, orderType);

			} catch (Exception ex) {
				throw new BusinessException(ex.getMessage());
			}
			}
}
