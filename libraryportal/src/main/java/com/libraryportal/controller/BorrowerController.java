package com.libraryportal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.exception.ErrorResponse;
import com.libraryportal.service.BorrowerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

	private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);

	private final BorrowerService borrowerService;

	@Autowired
	public BorrowerController(BorrowerService borrowerService) {
		this.borrowerService = borrowerService;
	}

	/**
	 * Endpoint to register a new borrower.
	 * 
	 * @param borrower The borrower object to register.
	 * @return ResponseEntity containing the registered borrower if successful, or
	 *         an error response if a duplicate registration occurs.
	 */
	@PostMapping("/registerBorrower")
	public ResponseEntity<?> registerBorrower(@RequestBody Borrower borrower) {
		try {
			Borrower registeredBorrower = borrowerService.registerBorrower(borrower);
			return ResponseEntity.status(HttpStatus.CREATED).body(registeredBorrower);
		} catch (DuplicateRegistrationException e) {
			ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage() + " : " + borrower.getEmail());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			logger.error("Error registering borrower", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Endpoint to get all borrowers.
	 * 
	 * @return ResponseEntity containing a list of all borrowers if successful, or
	 *         an error response if an exception occurs.
	 */
	@GetMapping("/getBorrowers")
	public ResponseEntity<List<Borrower>> getAllBorrowers() {
		try {
			List<Borrower> borrowers = borrowerService.getAllBorrowers();
			return ResponseEntity.ok(borrowers);
		} catch (Exception e) {
			logger.error("Error fetching all borrowers", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Endpoint to get a borrower by ID.
	 * 
	 * @param id The ID of the borrower to retrieve.
	 * @return ResponseEntity containing the borrower if found, or an error response
	 *         if the borrower is not found or an exception occurs.
	 */
	@GetMapping("/getBorrowerById/{id}")
	public ResponseEntity<?> getBorrowerById(@PathVariable Long id) {
		try {
			Borrower borrower = borrowerService.getBorrowerById(id);
			return ResponseEntity.ok(borrower);
		} catch (BorrowerNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		} catch (Exception e) {
			logger.error("Error fetching borrower by ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
		}
	}

	/**
	 * Endpoint to update a borrower by ID.
	 * 
	 * @param id          The ID of the borrower to update.
	 * @param newBorrower The updated borrower object.
	 * @return ResponseEntity containing the updated borrower if successful, or an
	 *         error response if the borrower is not found or an exception occurs.
	 */
	@PutMapping("/updateBorrowerById/{id}")
	public ResponseEntity<?> updateBorrower(@PathVariable Long id, @Valid @RequestBody Borrower newBorrower) {
		try {
			Borrower updatedBorrower = borrowerService.updateBorrower(id, newBorrower);
			return ResponseEntity.ok(updatedBorrower);
		} catch (BorrowerNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage() + " : " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		} catch (Exception e) {
			logger.error("Error updating borrower with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Endpoint to delete a borrower by ID.
	 * 
	 * @param id The ID of the borrower to delete.
	 * @return ResponseEntity indicating success if successful, or an error response
	 *         if an exception occurs.
	 */
	@DeleteMapping("/deleteBorrowerById/{id}")
	public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
		try {
			borrowerService.deleteBorrower(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("Error deleting borrower with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
