package com.libraryportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * BookNotFoundException is a custom exception class that represents the
 * scenario where a requested book is not found. It is annotated
 * with @ResponseStatus to automatically return a NOT_FOUND status code when
 * thrown.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new BookNotFoundException with the specified detail message.
	 *
	 * @param message The detail message.
	 */
	public BookNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new BookNotFoundException with the specified detail message and
	 * cause.
	 * 
	 * @param message the detail message.
	 * @param cause   the cause.
	 */
	public BookNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
