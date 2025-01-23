package com.nouros.hrms;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.enttribe.core.generic.utils.CoreConstants;
import com.enttribe.product.infra.configuration.exception.ExceptionMessage;
import com.google.gson.Gson;



/**
 * This class acts as a global exception handler for the application. It extends {@link ResponseEntityExceptionHandler},
 * allowing it to handle exceptions and return appropriate responses as {@link ResponseEntity}.
 * The class is annotated with {@link ControllerAdvice}, making it a global exception handler that will be applied
 * to all controllers in the application.
 */
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String EXCEPTION_TYPE = "Exception-Type";
	private static final String LOGGER_MESS = "Getting issue on request:{}, issue:{}";

	/**
     * Converts the given exception to a {@link ResponseEntity} with JSON body and the specified status code.
     *
     * @param exception       The exception to be converted.
     * @param request         The current web request.
     * @param bodyOfResponse  The JSON string representing the response body.
     * @param status          The HTTP status code for the response.
     * @return A {@link ResponseEntity} containing the JSON body, headers, and status code.
     */
	private ResponseEntity<Object> toResponseEntity(Exception exception, WebRequest request, String bodyOfResponse,
			HttpStatus status) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(EXCEPTION_TYPE, exception.getClass().getName());

		return handleExceptionInternal(exception, bodyOfResponse, headers, status, request);
	}

	/**
     * Handles validation exceptions for method arguments. It logs the exception message, constructs a JSON response
     * containing the validation error details, and returns a {@link ResponseEntity} with a BAD_REQUEST status.
     *
     * @param ex       The validation exception thrown during method argument validation.
     * @param headers  The HTTP headers for the response.
     * @param status   The HTTP status code for the response.
     * @param request  The current web request.
     * @return A {@link ResponseEntity} with JSON response containing validation error details and BAD_REQUEST status.
     */
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Validation exception:{}", ex.getMessage());
		FieldError fieldError = ex.getBindingResult().getFieldError();
		String message="";
		if(fieldError!=null) {
		message = "\"" + fieldError.getRejectedValue() + "\" as " + fieldError.getField() + " of "
				+ fieldError.getObjectName() + " is invalid. As " + fieldError.getDefaultMessage() + ".";
		log.info(message);
		}
		String bodyOfResponse = "{\"status\":\"" + status.toString() + "\",\"message\":\"" + message + "\"}";
		return ResponseEntity.badRequest().body(bodyOfResponse);
	}

	 /**
	     * Handles uncaught exceptions of type {@link Exception}. It logs the stack trace of the exception,
	     * constructs a JSON response using {@link ExceptionMessage}, and returns a {@link ResponseEntity} with an
	     * INTERNAL_SERVER_ERROR status.
	     *
	     * @param exception  The uncaught exception.
	     * @param request    The current web request.
	     * @return A {@link ResponseEntity} with JSON response containing the error message and INTERNAL_SERVER_ERROR status.
	     */	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> toResponse(Exception exception, WebRequest request) {
		log.error("Uncaught exception:{}",exception.getMessage());
		ExceptionMessage exceptionMessage = new ExceptionMessage(CoreConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		String bodyOfResponse = new Gson().toJson(exceptionMessage);
		return toResponseEntity(exception, request, bodyOfResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Handles the {@link SQLGrammarException} by logging the issue details and constructing a JSON response containing
	 * an "Invalid Parameter" message. Returns a {@link ResponseEntity} with BAD_REQUEST status.
	 *
	 * @param exception The {@link SQLGrammarException} to be handled.
	 * @param request   The current web request.
	 * @return A {@link ResponseEntity} with JSON response containing the "Invalid Parameter" message and BAD_REQUEST status.
	 */
	@ExceptionHandler(SQLGrammarException.class)
	public ResponseEntity<Object> toResponse(SQLGrammarException exception, WebRequest request) {
		log.error(LOGGER_MESS	, request.getContextPath(), exception.getMessage());
		ExceptionMessage exceptionMessage = new ExceptionMessage("Invalid Parameter ");
		String bodyOfResponse = new Gson().toJson(exceptionMessage);
		return toResponseEntity(exception, request, bodyOfResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles the {@link DataIntegrityViolationException} by logging the issue details and constructing a JSON response
	 * with a message depending on the root cause of the exception. If the root cause contains "Duplicate entry", the response
	 * will contain "Duplicate Entry Not Allowed". If it contains "Cannot delete or update a parent row", the response will
	 * contain "Child Entity". Otherwise, the response will contain "Something went wrong". Returns a {@link ResponseEntity}
	 * with BAD_REQUEST status.
	 *
	 * @param exception The {@link DataIntegrityViolationException} to be handled.
	 * @param request   The current web request.
	 * @return A {@link ResponseEntity} with JSON response containing the appropriate message and BAD_REQUEST status.
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        String errorMessage = "Something went wrong";
        // Check for specific causes and customize the error message accordingly
        Throwable rootCause = exception.getRootCause();
        if (rootCause != null) {
            String rootCauseMessage = rootCause.getMessage();
            if (rootCauseMessage.contains("Duplicate entry")) {
                errorMessage = "Duplicate Entry Not Allowed";
            } else if (rootCauseMessage.contains("Cannot delete or update a parent row")) {
                errorMessage = "Child Entity";
            }
        }
        ExceptionMessage exceptionMessage = new ExceptionMessage(errorMessage);
        String bodyOfResponse = new Gson().toJson(exceptionMessage);
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
	/**
	 * Handles the {@link ConstraintViolationException} by logging the issue details and constructing a JSON response
	 * containing the exception message. Returns a {@link ResponseEntity} with BAD_REQUEST status.
	 *
	 * @param exception The {@link ConstraintViolationException} to be handled.
	 * @param request   The current web request.
	 * @return A {@link ResponseEntity} with JSON response containing the exception message and BAD_REQUEST status.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> toResponse(ConstraintViolationException exception, WebRequest request) {
		log.error("Uncaught exception:{}",exception.getMessage());
		String bodyOfResponse = exception.getMessage();
		return toResponseEntity(exception, request, bodyOfResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles the {@link JpaSystemException} by logging the issue details and constructing a JSON response containing
	 * the exception message. Returns a {@link ResponseEntity} with BAD_REQUEST status.
	 *
	 * @param exception The {@link JpaSystemException} to be handled.
	 * @param request   The current web request.
	 * @return A {@link ResponseEntity} with JSON response containing the exception message and BAD_REQUEST status.
	 */
	@ExceptionHandler(JpaSystemException.class)
	public ResponseEntity<Object> toResponse(JpaSystemException exception, WebRequest request) {
		log.error(LOGGER_MESS, request.getContextPath(), exception.getMessage());
		String bodyOfResponse = exception.getMessage();
		return toResponseEntity(exception, request, bodyOfResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles the {@link EmptyResultDataAccessException} by logging the issue details and constructing a JSON response
	 * with a "Data Not Exists!" message. Returns a {@link ResponseEntity} with BAD_REQUEST status.
	 *
	 * @param exception The {@link EmptyResultDataAccessException} to be handled.
	 * @param request   The current web request.
	 * @return A {@link ResponseEntity} with JSON response containing the "Data Not Exists!" message and BAD_REQUEST status.
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> toResponse(EmptyResultDataAccessException exception, WebRequest request) {
		log.error(LOGGER_MESS, request.getContextPath(), exception.getMessage());
		ExceptionMessage exceptionMessage = new ExceptionMessage("Data Not Exists! ");
		String bodyOfResponse = new Gson().toJson(exceptionMessage);
		return toResponseEntity(exception, request, bodyOfResponse, HttpStatus.BAD_REQUEST);
	}

}
