package com.libraryportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * GlobalExceptionHandler class provides centralized exception handling for the
 * application. It handles ResourceNotFoundException and other exceptions and
 * returns appropriate error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles BookNotFoundException & BorrowerNotFoundException and returns a
	 * ResponseEntity with a NOT_FOUND status code.
	 * 
	 * @param ex      The BookNotFoundException & BorrowerNotFoundException to be
	 *                handled.
	 * @param request The WebRequest object containing details of the request.
	 * @return A ResponseEntity containing error details and a NOT_FOUND status
	 *         code.
	 */
	@ExceptionHandler({ BookNotFoundException.class, BorrowerNotFoundException.class })
	public ResponseEntity<?> handleBookNotFoundException(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles DuplicateRegistrationException and returns a ResponseEntity with a
	 * BAD_REQUEST status code.
	 * 
	 * @param ex      The DuplicateRegistrationException to be handled.
	 * @param request The WebRequest object containing details of the request.
	 * @return A ResponseEntity containing error details and a BAD_REQUEST status
	 *         code.
	 */
	@ExceptionHandler(DuplicateRegistrationException.class)
	public ResponseEntity<String> handleDuplicateRegistrationException(DuplicateRegistrationException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles other exceptions and returns a ResponseEntity with an
	 * INTERNAL_SERVER_ERROR status code.
	 *
	 * @param ex      The Exception to be handled.
	 * @param request The WebRequest object containing details of the request.
	 * @return A ResponseEntity containing error details and an
	 *         INTERNAL_SERVER_ERROR status code.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
